#include <iostream>
#include <fstream>
#include <vector>
#include <algorithm>
#include <chrono>
#include <random>
#include <string>
#include <cuda_runtime.h>

#include <sys/stat.h>
#if defined(_WIN32)
#include <direct.h>
#endif

// verificarea erorilor CUDA
#define CHECK_CUDA(call)                                                                                    \
    {                                                                                                       \
        cudaError_t err = call;                                                                             \
        if (err != cudaSuccess)                                                                             \
        {                                                                                                   \
            std::cerr << "CUDA Error: " << cudaGetErrorString(err) << " at line " << __LINE__ << std::endl; \
            exit(1);                                                                                        \
        }                                                                                                   \
    }

void create_directory(const std::string &path)
{
#if defined(_WIN32)
    _mkdir(path.c_str());
#else
    mkdir(path.c_str(), 0777);
#endif
}

// MANAGEMENT MEMORIE HOST (CPU)
int **allocMatrix(int N, int M)
{
    int **F = new int *[N];
    for (int i = 0; i < N; ++i)
        F[i] = new int[M];
    return F;
}

void freeMatrix(int **F, int N)
{
    for (int i = 0; i < N; ++i)
        delete[] F[i];
    delete[] F;
}

// Genereaza fisierul de intrare cu numere aleatoare
void generateMatrixFile(const std::string &filename, int N, int M)
{
    create_directory("data");
    create_directory("data/inputs");
    std::ofstream fout("data/inputs/" + filename + ".txt");
    if (!fout.is_open())
        return;

    std::mt19937 gen(std::random_device{}());
    std::uniform_int_distribution<int> dist(0, 9);
    fout << N << " " << M << "\n";
    for (int i = 0; i < N; ++i)
    {
        for (int j = 0; j < M; ++j)
            fout << dist(gen) << " ";
        fout << "\n";
    }
}

int **readMatrix(const std::string &filename, int &N, int &M)
{
    std::ifstream fin("data/inputs/" + filename + ".txt");
    if (!fin.is_open())
    {
        std::cerr << "Nu s-a putut deschide fisierul " << filename << "\n";
        exit(1);
    }
    fin >> N >> M;
    int **F = allocMatrix(N, M);
    for (int i = 0; i < N; ++i)
        for (int j = 0; j < M; ++j)
            fin >> F[i][j];
    return F;
}

// Salveaza rezultatul
void writeMatrix(const std::string &filename, int **F, int N, int M)
{
    create_directory("data/outputs");
    std::ofstream fout("data/outputs/" + filename + ".txt");
    fout << N << " " << M << "\n";
    for (int i = 0; i < N; ++i)
    {
        for (int j = 0; j < M; ++j)
            fout << F[i][j] << " ";
        fout << "\n";
    }
}

// IMPLEMENTARE SECVENTIALA (CPU)
void convolutionSequentialInPlace(int **F, int N, int M)
{
    int kernel[3][3] = {{0, 1, 0}, {1, 1, 1}, {0, 1, 0}};
    std::vector<int> prev(M), curr(M), next(M);

    // bordare initiala
    for (int j = 0; j < M; ++j)
        prev[j] = F[0][j];
    for (int j = 0; j < M; ++j)
        curr[j] = F[std::min(1, N - 1)][j];

    for (int i = 0; i < N; ++i)
    {
        for (int j = 0; j < M; ++j)
            next[j] = F[std::min(i + 1, N - 1)][j];

        for (int j = 0; j < M; ++j)
        {
            int sum = 0;
            for (int di = -1; di <= 1; ++di)
            {
                const std::vector<int> &row = (di == -1 ? prev : di == 0 ? curr
                                                                         : next);
                for (int dj = -1; dj <= 1; ++dj)
                {
                    int jj = j + dj;
                    if (jj < 0)
                        jj = 0;
                    if (jj >= M)
                        jj = M - 1;
                    sum += kernel[di + 1][dj + 1] * row[jj];
                }
            }
            F[i][j] = sum;
        }
        prev = curr;
        curr = next;
    }
}

// IMPLEMENTARE PARALELA (CUDA)

// un thread per pixel
__global__ void convolutionKernel(const int *d_in, int *d_out, int N, int M)
{
    // Calcul indici globali pe baza grid-ului 2D
    int col = blockIdx.x * blockDim.x + threadIdx.x;
    int row = blockIdx.y * blockDim.y + threadIdx.y;

    if (row < N && col < M)
    {
        // Matricea de convolutie 3x3
        int k[3][3] = {{0, 1, 0}, {1, 1, 1}, {0, 1, 0}};

        int sum = 0;
        for (int i = -1; i <= 1; ++i)
        {
            for (int j = -1; j <= 1; ++j)
            {
                int r_idx = row + i;
                int c_idx = col + j;

                // Bordare virtuala
                if (r_idx < 0)
                    r_idx = 0;
                if (r_idx >= N)
                    r_idx = N - 1;
                if (c_idx < 0)
                    c_idx = 0;
                if (c_idx >= M)
                    c_idx = M - 1;

                // Acces liniarizat: row * Width + col
                sum += d_in[r_idx * M + c_idx] * k[i + 1][j + 1];
            }
        }
        d_out[row * M + col] = sum;
    }
}

void convolutionCUDA(int **F, int N, int M, int blockSizeParam)
{
    size_t sizeBytes = N * M * sizeof(int);
    int *d_in, *d_out;

    // Alocare pe GPU
    CHECK_CUDA(cudaMalloc((void **)&d_in, sizeBytes));
    CHECK_CUDA(cudaMalloc((void **)&d_out, sizeBytes));

    // Copiere date pe GPU: Host -> Device
    for (int i = 0; i < N; ++i)
    {
        CHECK_CUDA(cudaMemcpy(d_in + i * M, F[i], M * sizeof(int), cudaMemcpyHostToDevice));
    }

    // Configurare thread-uri si blocuri
    if (blockSizeParam <= 0)
        blockSizeParam = 16;
    dim3 dimBlock(blockSizeParam, blockSizeParam);
    dim3 dimGrid((M + dimBlock.x - 1) / dimBlock.x, (N + dimBlock.y - 1) / dimBlock.y);

    // Lansare Kernel
    convolutionKernel<<<dimGrid, dimBlock>>>(d_in, d_out, N, M);

    CHECK_CUDA(cudaGetLastError());
    CHECK_CUDA(cudaDeviceSynchronize());

    for (int i = 0; i < N; ++i)
    {
        CHECK_CUDA(cudaMemcpy(F[i], d_out + i * M, M * sizeof(int), cudaMemcpyDeviceToHost));
    }

    CHECK_CUDA(cudaFree(d_in));
    CHECK_CUDA(cudaFree(d_out));
}

int main(int argc, char *argv[])
{
    if (argc < 3)
    {
        std::cerr << "Usage: " << argv[0] << " <size> <seq|cuda> [block_size_dim]\n";
        return 1;
    }

    int N = std::stoi(argv[1]);
    int M = N;
    std::string type = argv[2];
    int P = 16;

    if (type == "cuda" || type == "par")
    {
        if (argc >= 4)
            P = std::stoi(argv[3]);
        if (P * P > 1024)
            P = 32;
    }

    std::string name = "mat_" + std::to_string(N);
    generateMatrixFile(name, N, M);

    int N1, M1;
    int **F = readMatrix(name, N1, M1);

    auto start = std::chrono::high_resolution_clock::now();

    if (type == "seq")
    {
        convolutionSequentialInPlace(F, N1, M1);
    }
    else
    {
        convolutionCUDA(F, N1, M1, P);
    }

    auto end = std::chrono::high_resolution_clock::now();
    long long elapsed_ns = std::chrono::duration_cast<std::chrono::nanoseconds>(end - start).count();

    if (type == "seq")
    {
        writeMatrix(name + "_seq", F, N1, M1);
    }
    else
    {
        writeMatrix(name + "_cuda_b" + std::to_string(P), F, N1, M1);
    }

    std::cout << elapsed_ns << std::endl;

    freeMatrix(F, N1);
    return 0;
}