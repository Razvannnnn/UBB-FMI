#include<iostream>
#include<fstream>
#include<thread>
#include<cassert>
#include<chrono>
#include<cmath>
#include<string>
#include<vector>
#include <windows.h>
using namespace std;
using namespace std::chrono;

extern double convolutie_secventiala(int* F, int* C, int* V, int N, int M, int k, const char* outputFile);
extern double convolutie_paralel_linii(int* F, int* C, int* V, int N, int M, int k, int P, const char* outputFile);
extern double convolutie_paralel_coloane(int* F, int* C, int* V, int N, int M, int k, int P, const char* outputFile);


const int N = 10;
const int M = 10;
const int k = 3;
int P = 16;

int F[N*M];
int C[k*k];
int V_secvential[N*M];
int V_paralel_linii[N * M];
int V_paralel_coloane[N * M];

static void read_from_file(const string& filename) {
    ifstream fin(filename);
    if (!fin.is_open()) {
        cerr << "Eroare la deschiderea fisierului " << filename << endl;
        exit(1);
    }

    // Citire matrice F (N x M) in vector
    for (int i = 0; i < N; i++) {
        for (int j = 0; j < M; j++) {
            fin >> F[i*M + j];
        }
    }

    // Citire matrice C (k x k) in vector
    for (int i = 0; i < k; i++) {
        for (int j = 0; j < k; j++) {
            fin >> C[i*k + j];
        }
    }

    fin.close();
}


// METODE PENTRU TESTARE
vector<int> citesteFisier(const string& path) {
    ifstream fin(path);
    vector<int> data;
    int val;
    while (fin >> val) data.push_back(val);
    return data;
}

bool comparaFisiere(const string& f1, const string& f2, int N, int M) {
    vector<int> A = citesteFisier(f1);
    vector<int> B = citesteFisier(f2);

    if (A.size() != B.size()) {
        return false;
    }

    for (size_t idx = 0; idx < A.size(); idx++) {
        if (A[idx] != B[idx]) {
            int i = idx / M;
            int j = idx % M;
            return false;
        }
    }

    return true;
}


vector<string> listaFisiereTXT(const string& folderPath) {
    vector<string> fisiere;
    WIN32_FIND_DATAA fd;
    HANDLE hFind = FindFirstFileA((folderPath + "\\*.txt").c_str(), &fd);
    if (hFind != INVALID_HANDLE_VALUE) {
        do {
            if (!(fd.dwFileAttributes & FILE_ATTRIBUTE_DIRECTORY))
                fisiere.push_back(folderPath + "\\" + fd.cFileName);
        } while (FindNextFileA(hFind, &fd));
        FindClose(hFind);
    }
    return fisiere;
}

void comparaToateFisierele(const string& folderPath, int N, int M) {
    vector<string> fisiere = listaFisiereTXT(folderPath);

    if (fisiere.empty()) {
        return;
    }

    string fisierParinte = fisiere[0];

    for (size_t i = 1; i < fisiere.size(); i++) {
        bool crt = comparaFisiere(fisierParinte, fisiere[i], N, M);
        if (crt == false) {
            cout << "FISIERLE NU SUNT EGALE!";
            return;
        }
    }
    cout << "FISIERLE SUNT EGALE!\n\n";
}




int main(int argc, char* argv[]) {
    int iteratie = 0;
    if (argc > 1) {
        iteratie = stoi(argv[1]);
    }
    
    string filename = "C:\\Users\\razva\\Desktop\\PPD\\matrici\\matrici\\N" 
        + to_string(N) + "M" + to_string(M) + "k" + to_string(k) + ".txt";

    read_from_file(filename);

    string output_folder = string(R"(C:\\Users\\razva\\Desktop\\PPD\\rezolvari C++\\rezolvare_alocare_statica_cpp\\outputs_lab\\rez_)")
        + "N" + to_string(N) + "M" + to_string(M) + "k" + to_string(k) + "\\";

    
    
    // --- SECVENTIAL ---
    string output_filename_secv = output_folder + "output_secventialN"
        + to_string(N) + "M" + to_string(M) + "k" + to_string(k)
        + "_iteratia" + to_string(iteratie) + ".txt";

    double delta_secv = convolutie_secventiala(F, C, V_secvential, N, M, k, output_filename_secv.c_str());
    cout << delta_secv << endl;
    

    
    // --- PARALEL LINII ---
    string output_filename_par_linii = output_folder + "output_paralel_linii_" + to_string(P) + "thareds_N"
        + to_string(N) + "M" + to_string(M) + "k" + to_string(k)
        + "_iteratia" + to_string(iteratie) + ".txt";

    double delta_par_linii = convolutie_secventiala(F, C, V_paralel_linii, N, M, k, output_filename_par_linii.c_str());
    cout << delta_par_linii << endl;


    // --- PARALEL COLOANE ---
    string output_filename_par_col = output_folder + "output_paralel_coloane_" + to_string(P) + "thareds_N"
        + to_string(N) + "M" + to_string(M) + "k" + to_string(k)
        + "_iteratia" + to_string(iteratie) + ".txt";

    double delta_par_coloane = convolutie_paralel_coloane(F, C, V_paralel_coloane, N, M, k, P, output_filename_par_col.c_str());
    cout << delta_par_coloane << endl;
    

    // --- VERIFICARE REZULTATE ---
    for (int i = 0; i < N * M; i++) {
        assert(V_secvential[i] == V_paralel_linii[i]);
        assert(V_secvential[i] == V_paralel_coloane[i]);
    }

    comparaToateFisierele(output_folder, N, M);

    return 0;
}