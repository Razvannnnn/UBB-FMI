#include <iostream>
#include <vector>
#include <string>
#include <fstream>
#include <stdexcept>
#include <mpi.h>
#include <cstring>

using namespace std;

const int TAG_DATA = 200;
const int TAG_END = 201;
const int MAX_LEN = 30;

void processWord(string& s, int Y) {
    int len = s.length();
    if (len <= Y) {
        s += "@";
    }
    else {
        s = "*" + s;
    }
}

vector<string> readWords(const string& filename) {
    ifstream file(filename);
    if (!file.is_open()) throw runtime_error("Eroare deschidere: " + filename);
    vector<string> words;
    string w;
    while (file >> w) words.push_back(w);
    return words;
}

struct RezultatSecvential {
    vector<string> cuvinte;
    int totalCuvinte;
};

RezultatSecvential runSequential(int Y) {
    vector<string> words = readWords("words_12.txt");
    for (auto& w : words) {
        processWord(w, Y);
    }
    return { words, (int)words.size() };
}

void runAll(int rank, int p, int Y_input) {
    int Y = Y_input;

    MPI_Bcast(&Y, 1, MPI_INT, 0, MPI_COMM_WORLD);

    if (rank == 0) {
        ifstream fin("words_12.txt");
        if (!fin.is_open()) throw runtime_error("Eroare deschidere fisier");

        ofstream fout("words_out.txt");
        string word_str;
        vector<int> sender_history;

        int current_worker = 1;
        int total_words = 0;

        while (fin >> word_str) {
            char buffer[MAX_LEN];
            strcpy(buffer, word_str.c_str());

            MPI_Send(buffer, MAX_LEN, MPI_CHAR, current_worker, TAG_DATA, MPI_COMM_WORLD);

            sender_history.push_back(current_worker);
            total_words++;

            // trece la urmatorul proces circular
            current_worker++;
            if (current_worker >= p) {
                current_worker = 1;
            }
        }
        fin.close();


        for (int i = 1; i < p; i++) {
            MPI_Send(NULL, 0, MPI_CHAR, i, TAG_END, MPI_COMM_WORLD);
        }


        for (int worker_id : sender_history) {
            char recv_buffer[MAX_LEN];
            MPI_Status status;

            MPI_Recv(recv_buffer, MAX_LEN, MPI_CHAR, worker_id, TAG_DATA, MPI_COMM_WORLD, &status);

            fout << recv_buffer << "\n";
        }
        fout.close();

        cout << "P 0 nr cuv citite = " << total_words << endl;

    } else {
        char buffer[MAX_LEN];
        MPI_Status status;
        long local_sum_lengths = 0;

        // bucla infinita pentru procesele worker
        while (true) {
            MPI_Recv(buffer, MAX_LEN, MPI_CHAR, 0, MPI_ANY_TAG, MPI_COMM_WORLD, &status);

            // oprire
            if (status.MPI_TAG == TAG_END) {
                break;
            }

            string s(buffer);
            // modifica cuv
            processWord(s, Y);
            strcpy(buffer, s.c_str());

            local_sum_lengths += s.length();

            // trimite rez la master
            MPI_Send(buffer, MAX_LEN, MPI_CHAR, 0, TAG_DATA, MPI_COMM_WORLD);
        }
        cout << "Procesul " << rank << " are suma lungimilor: " << local_sum_lengths << endl;
    }
}

int main(int argc, char* argv[]) {
    MPI_Init(&argc, &argv);
    int rank, p;
    MPI_Comm_rank(MPI_COMM_WORLD, &rank);
    MPI_Comm_size(MPI_COMM_WORLD, &p);

    if (p < 2) {
        if (rank == 0) cerr << "Minim 2 procese" << endl;
        MPI_Finalize();
        return 1;
    }

    int Y = 5;

    if (rank == 0) {
        if(argc > 1) Y = atoi(argv[1]);
    }

    try {
        runAll(rank, p, Y);

        if (rank == 0) {
            RezultatSecvential seq = runSequential(Y);
            vector<string> mpi_results = readWords("words_out.txt");

            bool ok = (seq.cuvinte.size() == mpi_results.size());
            if (ok) {
                for (size_t i = 0; i < seq.cuvinte.size(); i++) {
                    if (seq.cuvinte[i] != mpi_results[i]) {
                        ok = false;
                        break;
                    }
                }
            }
            if (ok) cout << "Fisiere identice" << endl;
            else cout << "gresit" << endl;
        }
    }
    catch (const exception& e) {
        if (rank == 0) cerr << "Eroare " << e.what() << endl;
    }

    MPI_Finalize();
    return 0;
}
