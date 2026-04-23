#include <iostream>
#include <fstream>
#include <thread>
#include <chrono>
using namespace std;
using namespace std::chrono;

// Bordare virtuala
static int getF(int* F, int i, int j, int N, int M) {
    if (i < 0) i = 0;
    if (j < 0) j = 0;
    if (i >= N) i = N - 1;
    if (j >= M) j = M - 1;
    return *(F + i * M + j);
}


static void convolutie_interval_coloane(int* F, int* C, int* V, int N, int M, int k, int start_col, int end_col) {
    int offset = k / 2;
    for (int i = 0; i < N; i++) {
        for (int j = start_col; j < end_col; j++) {
            int sum = 0;
            for (int u = 0; u < k; u++) {
                for (int v = 0; v < k; v++) {
                    int fi = i + u - offset;
                    int fj = j + v - offset;
                    sum += getF(F, fi, fj, N, M) * *(C + u * k + v);
                }
            }
            *(V + i * M + j) = sum;
        }
    }
}


double convolutie_paralel_coloane(int* F, int* C, int* V, int N, int M, int k, int P, const char* outputFile) {

    thread* threads = new thread[P];
    int cols_per_thread = M / P;
    int remaining = M % P;
    int current_col = 0;

    auto start_time = high_resolution_clock::now();

    for (int t = 0; t < P; t++) {
        int start_col = current_col;
        int end_col = start_col + cols_per_thread + (t < remaining ? 1 : 0);
        threads[t] = thread(convolutie_interval_coloane, F, C, V, N, M, k, start_col, end_col);
        current_col = end_col;
    }

    for (int t = 0; t < P; t++) threads[t].join();

    auto end_time = high_resolution_clock::now();
    chrono::duration<double, milli> delta = end_time - start_time;

    delete[] threads;

    ofstream fout(outputFile);
    if (!fout.is_open()) {
        cerr << "Eroare la scrierea fisierului " << outputFile << endl;
        return 0;
    }
    for (int i = 0; i < N; i++) {
        for (int j = 0; j < M; j++) {
            fout << *(V + i * M + j) << " ";
        }
        fout << "\n";
    }
    fout.close();

    return delta.count();
}
