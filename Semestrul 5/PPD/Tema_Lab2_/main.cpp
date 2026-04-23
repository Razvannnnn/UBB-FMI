#include <iostream>
#include <fstream>
#include <thread>
#include <vector>
#include <algorithm>
#include <chrono>
#include <random>
#include <filesystem>

namespace fs = std::filesystem;


int** allocMatrix(int N, int M) {
    int** F = new int*[N];
    for (int i = 0; i < N; ++i)
        F[i] = new int[M];
    return F;
}

void freeMatrix(int** F, int N) {
    for (int i = 0; i < N; ++i)
        delete[] F[i];
    delete[] F;
}



void generateMatrixFile(const std::string& filename, int N, int M) {
    fs::create_directories("data/inputs/");
    std::ofstream fout("data/inputs/" + filename + ".txt");
    std::mt19937 gen(std::random_device{}());
    std::uniform_int_distribution<int> dist(0, 9);
    fout << N << " " << M << "\n";
    for (int i = 0; i < N; ++i) {
        for (int j = 0; j < M; ++j)
            fout << dist(gen) << " ";
        fout << "\n";
    }
}

int** readMatrix(const std::string& filename, int& N, int& M) {
    std::ifstream fin("data/inputs/" + filename + ".txt");
    if (!fin.is_open()) {
        std::cerr << "Nu s-a putut deschide fisierul " << filename << "\n";
        exit(1);
    }
    fin >> N >> M;
    int** F = allocMatrix(N, M);
    for (int i = 0; i < N; ++i)
        for (int j = 0; j < M; ++j)
            fin >> F[i][j];
    return F;
}

void writeMatrix(const std::string& filename, int** F, int N, int M) {
    fs::create_directories("data/outputs/");
    std::ofstream fout("data/outputs/" + filename + ".txt");
    fout << N << " " << M << "\n";
    for (int i = 0; i < N; ++i) {
        for (int j = 0; j < M; ++j)
            fout << F[i][j] << " ";
        fout << "\n";
    }
}




void convolutionSequentialInPlace(int** F, int N, int M) {
    int kernel[3][3] = {{0,1,0},{1,1,1},{0,1,0}};
    std::vector<int> prev(M), curr(M), next(M);

    for (int j = 0; j < M; ++j) prev[j] = F[0][j];
    for (int j = 0; j < M; ++j) curr[j] = F[std::min(1, N - 1)][j];

    for (int i = 0; i < N; ++i) {
        for (int j = 0; j < M; ++j)
            next[j] = F[std::min(i + 1, N - 1)][j];

        for (int j = 0; j < M; ++j) {
            int sum = 0;
            for (int di = -1; di <= 1; ++di) {
                const std::vector<int>& row = (di == -1 ? prev : di == 0 ? curr : next);
                for (int dj = -1; dj <= 1; ++dj) {
                    int jj = std::clamp(j + dj, 0, M - 1);
                    sum += kernel[di + 1][dj + 1] * row[jj];
                }
            }
            F[i][j] = sum;
        }

        prev = curr;
        curr = next;
    }
}




void convolutionParallelLinesInPlace(int** F, int N, int M, int P) {
    int kernel[3][3] = {{0,1,0},{1,1,1},{0,1,0}};
    std::vector<std::thread> threads(P);
    int rowsPerThread = (N + P - 1) / P;

    auto worker = [&](int startRow, int endRow) {
        std::vector<int> prev(M), curr(M), next(M);
        for (int i = startRow; i < endRow; ++i) {
            for (int j = 0; j < M; ++j) {
                prev[j] = F[std::max(i - 1, 0)][j];
                curr[j] = F[i][j];
                next[j] = F[std::min(i + 1, N - 1)][j];
            }

            for (int j = 0; j < M; ++j) {
                int sum = 0;
                for (int di = -1; di <= 1; ++di) {
                    const std::vector<int>& row = (di == -1 ? prev : di == 0 ? curr : next);
                    for (int dj = -1; dj <= 1; ++dj) {
                        int jj = std::clamp(j + dj, 0, M - 1);
                        sum += kernel[di + 1][dj + 1] * row[jj];
                    }
                }
                F[i][j] = sum;
            }
        }
    };

    for (int t = 0; t < P; ++t) {
        int start = t * rowsPerThread;
        int end = std::min(start + rowsPerThread, N);
        if (start >= end) break;
        threads[t] = std::thread(worker, start, end);
    }

    for (auto& th : threads)
        if (th.joinable()) th.join();
}




int main(int argc, char* argv[]) {
    if (argc < 3) {
        std::cerr << "Usage: " << argv[0] << " <size> <seq|par> [num_threads]\n";
        return 1;
    }

    int N = std::stoi(argv[1]);
    int M = N; // matrice pătrată
    std::string type = argv[2];
    int P = 1; // default threads = 1 pentru secvențial

    if (type != "seq" && type != "par") {
        std::cerr << "Tip invalid: " << type << ". Folositi 'seq' sau 'par'.\n";
        return 1;
    }

    if (type == "par") {
        if (argc < 4) {
            std::cerr << "Trebuie sa specifici numarul de fire pentru paralel.\n";
            return 1;
        }
        P = std::stoi(argv[3]);
    }

    fs::create_directories("data/");
    std::string name = "mat_" + std::to_string(N);
    generateMatrixFile(name, N, M);

    int N1, M1;
    int** F = readMatrix(name, N1, M1);

    auto start = std::chrono::high_resolution_clock::now();

    if (type == "seq") {
        convolutionSequentialInPlace(F, N1, M1);
        writeMatrix(name + "_seq", F, N1, M1);
    } else {
        convolutionParallelLinesInPlace(F, N1, M1, P);
        writeMatrix(name + "_par_p" + std::to_string(P), F, N1, M1);
    }

    auto end = std::chrono::high_resolution_clock::now();
    long long elapsed_ns = std::chrono::duration_cast<std::chrono::nanoseconds>(end - start).count();

    // **AFISEAZA NUMAI TIMPUL**
    std::cout << elapsed_ns << std::endl;

    freeMatrix(F, N1);
    return 0;
}
