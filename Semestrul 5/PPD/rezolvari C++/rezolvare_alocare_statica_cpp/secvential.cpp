#include<iostream>
#include<fstream>
#include<thread>
#include<cassert>
#include<chrono>
#include<cmath>
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


double convolutie_secventiala(int* F, int* C, int* V, int N, int M, int k, const char* outputFile) {

    int offset = k / 2;

    auto start_time = high_resolution_clock::now();

    for (int i = 0; i < N; i++) {
        for (int j = 0; j < M; j++) {
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

    auto end_time = high_resolution_clock::now();
    duration<double, milli> delta_secv = end_time - start_time;


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

    return delta_secv.count();
}

