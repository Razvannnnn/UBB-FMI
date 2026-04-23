#include <iostream>
#include <fstream>
#include <thread>
#include <chrono>
#include <vector>
#include <functional>
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


static void convolutie_delta_worker(int* F, int* C, int* V, int N, int M, int k,
    const vector<pair<int, int>>& coords)
{
    int offset = k / 2;
    for (auto& p : coords) {
        int i = p.first;
        int j = p.second;
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

enum DeltaType { LINEAR, CYCLIC };

double convolutie_paralel_delta(int* F, int* C, int* V,
    int N, int M, int k, int P,
    DeltaType type,
    const char* outputFile)
{
    if (P <= 0) P = 1;
    vector<vector<pair<int, int>>> assignments(P);

    int total = N * M;

    if (type == LINEAR) {
        int chunk = total / P; 
        int extra = total % P;
        int idx = 0;
        for (int t = 0; t < P; t++) {
            int size = chunk + (t < extra ? 1 : 0);
            for (int cnt = 0; cnt < size; cnt++) {
                int i = idx / M;
                int j = idx % M;
                assignments[t].push_back({ i, j });
                idx++;
            }
        }
    }
    else {
        for (int idx = 0; idx < total; idx++) {
            int t = idx % P;
            int i = idx / M;
            int j = idx % M;
            assignments[t].push_back({ i, j });
        }
    }

    vector<thread> threads;
    threads.reserve(P);

    auto start_time = high_resolution_clock::now();

    for (int t = 0; t < P; t++) {
        threads.emplace_back(convolutie_delta_worker, F, C, V, N, M, k, std::ref(assignments[t]));
    }

    for (auto& th : threads) th.join();

    auto end_time = high_resolution_clock::now();
    chrono::duration<double, milli> delta = end_time - start_time;

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
