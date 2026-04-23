#include <iostream>
#include <fstream>
#include <thread>
#include <chrono>
#include <cmath>
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


static void convolutie_bloc(int* F, int* C, int* V, int N, int M, int k,
    int start_row, int end_row, int start_col, int end_col) {
    if (start_row >= end_row || start_col >= end_col) return;
    int offset = k / 2;
    for (int i = start_row; i < end_row; i++) {
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


double convolutie_paralel_blocuri(int* F, int* C, int* V, int N, int M, int k, int P, const char* outputFile) {
    if (P <= 0) P = 1;

    // Alegere optima pentru r (numar blocuri pe linii) si c (pe coloane)
    int r = (int)floor(sqrt((double)P));
    while (r > 0 && P % r != 0) r--; // cauta divizor optim
    if (r == 0) r = 1;
    int c = (P + r - 1) / r;

    // Distributie linii si coloane
    int rows_per_block = N / r;
    int extra_rows = N % r;
    int cols_per_block = M / c;
    int extra_cols = M % c;

    thread* threads = new thread[P];
    auto start_time = high_resolution_clock::now();

    int thread_index = 0;
    int row_start = 0;
    for (int bi = 0; bi < r; bi++) {
        int this_block_rows = rows_per_block + (bi < extra_rows ? 1 : 0);
        int row_end = row_start + this_block_rows;
        int col_start = 0;
        for (int bj = 0; bj < c && thread_index < P; bj++) {
            int this_block_cols = cols_per_block + (bj < extra_cols ? 1 : 0);
            int col_end = col_start + this_block_cols;

            threads[thread_index] = thread(convolutie_bloc, F, C, V, N, M, k,
                row_start, row_end, col_start, col_end);
            thread_index++;

            col_start = col_end;
        }
        row_start = row_end;
    }

    for (int t = 0; t < thread_index; t++) threads[t].join();

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
