#include <iostream>
#include <fstream>
#include <vector>
#include <string>
#include <mpi.h>
#include <algorithm> // Pentru std::max
#include <cstdlib> // Pentru exit()

using namespace std;

vector<unsigned char> citesteNumar(const string &numeFisier) {
    ifstream fin(numeFisier);
    if (!fin) {
        cerr << "Eroare la deschiderea fisierului: " << numeFisier << endl;
        exit(1);
    }

    int N;
    string numarStr;
    fin >> N >> numarStr;
    fin.close();

    if ((int)numarStr.size() != N) {
        cerr << "Eroare: numarul de cifre (" << numarStr.size()
             << ") din fisierul " << numeFisier
             << " nu corespunde cu valoarea N (" << N << ")!" << endl;
        exit(1);
    }

    vector<unsigned char> numar(N);
    // Stocam cu LSB la index 0
    for (int i = 0; i < N; i++) {
        numar[i] = numarStr[N - 1 - i] - '0';
    }

    return numar;
}


void scrieRezultat(const string &numeFisier, const vector<unsigned char> &numar) {
    // Suprascriem fisierul (fara ios::app)
    ofstream fout(numeFisier);
    if (!fout) {
        cerr << "Eroare la scrierea in fisier: " << numeFisier << endl;
        return;
    }

    bool leadingZero = true;
    for (int i = numar.size() - 1; i >= 0; i--) {
        if (numar[i] != 0) leadingZero = false;
        if (!leadingZero)
            fout << (int)numar[i];
    }
    // Daca numarul e gol sau e format doar din zerouri, scriem "0"
    if (leadingZero) fout << "0";

    fout << endl;
    fout.close();
}


vector<unsigned char> adunaNumereMari(const vector<unsigned char> &a, const vector<unsigned char> &b) {
    int n = a.size(), m = b.size();
    int dimMax = max(n, m);
    vector<unsigned char> rezultat;
    rezultat.reserve(dimMax + 1);

    unsigned int carry = 0;
    for (int i = 0; i < dimMax; i++) {
        unsigned int cifraA = (i < n) ? a[i] : 0;
        unsigned int cifraB = (i < m) ? b[i] : 0;

        unsigned int suma = cifraA + cifraB + carry;
        rezultat.push_back(suma % 10);
        carry = suma / 10;
    }

    if (carry) rezultat.push_back(carry);
    return rezultat;
}

//============================================================= VARIANTA 0 - SECVENTIALA
void v0_secvential(string numeFisier1, string numeFisier2) {
    vector<unsigned char> numar1 = citesteNumar(numeFisier1);
    vector<unsigned char> numar2 = citesteNumar(numeFisier2);
    vector<unsigned char> suma = adunaNumereMari(numar1, numar2);
    scrieRezultat("Numar_3.txt", suma);
}

//============================================================= VARIANTA 1 - COMUNICARE STANDARD
void v1_mpi(string numeFisier1, string numeFisier2) {
    int rank, size;
    MPI_Comm_rank(MPI_COMM_WORLD, &rank);
    MPI_Comm_size(MPI_COMM_WORLD, &size);

    if (size < 2) {
        if (rank == 0)
            cerr << "V1: E nevoie de cel putin 2 procese!" << endl;
        return;
    }

    if (rank == 0) {
        // Coordonator
        vector<unsigned char> A = citesteNumar(numeFisier1);
        vector<unsigned char> B = citesteNumar(numeFisier2);

        int N = max(A.size(), B.size());
        int workers = size - 1;

        A.resize(N, 0);
        B.resize(N, 0);

        int chunk = (N + workers - 1) / workers;

        ofstream fout("Numar_3.txt");
        fout.close();

        int offset = 0;
        for (int id_proces_curent = 1; id_proces_curent <= workers; id_proces_curent++) {
            int len = min(chunk, N - offset);
            if (len <= 0) len = 0;

            MPI_Send(&len, 1, MPI_INT, id_proces_curent, 0, MPI_COMM_WORLD);

            if (len > 0) {
                MPI_Ssend(A.data() + offset, len, MPI_UNSIGNED_CHAR, id_proces_curent, 1, MPI_COMM_WORLD);
                MPI_Ssend(B.data() + offset, len, MPI_UNSIGNED_CHAR, id_proces_curent, 2, MPI_COMM_WORLD);
                offset += len;
            }
        }

        vector<unsigned char> sum_global(N);
        int finalCarry = 0;

        for (int src = 1; src <= workers; src++) {
            int len;
            MPI_Recv(&len, 1, MPI_INT, src, 3, MPI_COMM_WORLD, MPI_STATUS_IGNORE);

            if (len > 0) {
                vector<unsigned char> rezultat_local(len);
                MPI_Recv(rezultat_local.data(), len, MPI_UNSIGNED_CHAR, src, 4, MPI_COMM_WORLD, MPI_STATUS_IGNORE);

                int pos = (src - 1) * chunk;
                for (int i = 0; i < len; i++) {
                    if (pos + i < N) {
                        sum_global[pos + i] = rezultat_local[i];
                    }
                }
            }

            // worker final
            if (src == workers) {
                MPI_Recv(&finalCarry, 1, MPI_INT, workers, 5, MPI_COMM_WORLD, MPI_STATUS_IGNORE);
            }
        }

        if (finalCarry > 0) {
            sum_global.push_back((unsigned char)finalCarry);
        }

        scrieRezultat("Numar_3.txt", sum_global);

    } else {
        // workeri
        int len;
        MPI_Recv(&len, 1, MPI_INT, 0, 0, MPI_COMM_WORLD, MPI_STATUS_IGNORE);

        if (len > 0) {
            vector<unsigned char> A(len), B(len), sum(len);
            MPI_Recv(A.data(), len, MPI_UNSIGNED_CHAR, 0, 1, MPI_COMM_WORLD, MPI_STATUS_IGNORE);
            MPI_Recv(B.data(), len, MPI_UNSIGNED_CHAR, 0, 2, MPI_COMM_WORLD, MPI_STATUS_IGNORE);

            int carry_in = 0;
            if (rank > 1) {
                MPI_Recv(&carry_in, 1, MPI_INT, rank - 1, 10, MPI_COMM_WORLD, MPI_STATUS_IGNORE);
            }

            int carry_out = carry_in;
            for (int i = 0; i < len; i++) {
                int s = A[i] + B[i] + carry_out;
                sum[i] = s % 10;
                carry_out = s / 10;
            }

            if (rank < size - 1) {
                MPI_Send(&carry_out, 1, MPI_INT, rank + 1, 10, MPI_COMM_WORLD);
            }

            MPI_Send(&len, 1, MPI_INT, 0, 3, MPI_COMM_WORLD);
            MPI_Send(sum.data(), len, MPI_UNSIGNED_CHAR, 0, 4, MPI_COMM_WORLD);

            if (rank == size - 1) {
                MPI_Send(&carry_out, 1, MPI_INT, 0, 5, MPI_COMM_WORLD);
            }
        } else {
            int carry_in = 0;
            if (rank > 1) {
                MPI_Recv(&carry_in, 1, MPI_INT, rank - 1, 10, MPI_COMM_WORLD, MPI_STATUS_IGNORE);
            }
            if (rank < size - 1) {
                MPI_Send(&carry_in, 1, MPI_INT, rank + 1, 10, MPI_COMM_WORLD);
            }

            MPI_Send(&len, 1, MPI_INT, 0, 3, MPI_COMM_WORLD);

            if (rank == size - 1) {
                MPI_Send(&carry_in, 1, MPI_INT, 0, 5, MPI_COMM_WORLD);
            }
        }
    }
}


//============================================================= VARIANTA 2 - SCATTER/GATHER
void v2_scatter_gather() {
    int rank, size;
    MPI_Comm_rank(MPI_COMM_WORLD, &rank);
    MPI_Comm_size(MPI_COMM_WORLD, &size);

    int N = 0;
    int chunk = 0;
    vector<unsigned char> A_global, B_global, sum_global;

    if (rank == 0) {
        // Master
        A_global = citesteNumar("Numar_1.txt");
        B_global = citesteNumar("Numar_2.txt");

        N = max(A_global.size(), B_global.size());

        if (N % size != 0) {
            N = ((N / size) + 1) * size;
        }
        chunk = N / size;

        A_global.resize(N, 0);
        B_global.resize(N, 0);
        sum_global.resize(N);
    }

    MPI_Bcast(&N, 1, MPI_INT, 0, MPI_COMM_WORLD);
    MPI_Bcast(&chunk, 1, MPI_INT, 0, MPI_COMM_WORLD);

    vector<unsigned char> A_local(chunk);
    vector<unsigned char> B_local(chunk);
    vector<unsigned char> sum_local(chunk);

    // Scatter
    MPI_Scatter(A_global.data(), chunk, MPI_UNSIGNED_CHAR,
                A_local.data(), chunk, MPI_UNSIGNED_CHAR,
                0, MPI_COMM_WORLD);

    MPI_Scatter(B_global.data(), chunk, MPI_UNSIGNED_CHAR,
                B_local.data(), chunk, MPI_UNSIGNED_CHAR,
                0, MPI_COMM_WORLD);

    int carry_in = 0;
    if (rank > 0) {
        MPI_Recv(&carry_in, 1, MPI_INT, rank - 1, 1, MPI_COMM_WORLD, MPI_STATUS_IGNORE);
    }

    int carry_out = carry_in;
    for (int i = 0; i < chunk; i++) {
        int s = A_local[i] + B_local[i] + carry_out;
        sum_local[i] = s % 10;
        carry_out = s / 10;
    }

    if (rank < size - 1) {
        MPI_Send(&carry_out, 1, MPI_INT, rank + 1, 1, MPI_COMM_WORLD);
    }


    // Gather - adunam
    MPI_Gather(sum_local.data(), chunk, MPI_UNSIGNED_CHAR,
               sum_global.data(), chunk, MPI_UNSIGNED_CHAR,
               0, MPI_COMM_WORLD);

    if (rank == 0) {
        int finalCarry = 0;
        MPI_Recv(&finalCarry, 1, MPI_INT, size - 1, 2, MPI_COMM_WORLD, MPI_STATUS_IGNORE);

        if (finalCarry > 0) {
            sum_global.push_back(finalCarry);
        }

        scrieRezultat("Numar_3.txt", sum_global);

    } else if (rank == size - 1) {
        MPI_Send(&carry_out, 1, MPI_INT, 0, 2, MPI_COMM_WORLD);
    }
}


//============================================================= VARIANTA 3 - ASINCRONA
void v3_mpi_asincron() {
    int rank, size;
    MPI_Comm_rank(MPI_COMM_WORLD, &rank);
    MPI_Comm_size(MPI_COMM_WORLD, &size);

    if (size < 2) {
        if (rank == 0)
            cerr << "V3: E nevoie de cel putin 2 procese!" << endl;
        return;
    }

    if (rank == 0) {
        // Coordonator
        vector<unsigned char> A = citesteNumar("Numar_1.txt");
        vector<unsigned char> B = citesteNumar("Numar_2.txt");

        int N = max(A.size(), B.size());
        int workers = size - 1;

        A.resize(N, 0);
        B.resize(N, 0);

        int chunk = (N + workers - 1) / workers;

        ofstream fout("Numar_3.txt");
        fout.close();

        int offset = 0;
        vector<MPI_Request> sendRequests;

        vector<int> lengths(workers);

        for (int id_proces_curent = 1; id_proces_curent <= workers; id_proces_curent++) {
            int len = min(chunk, N - offset);
            if (len <= 0) len = 0;
            lengths[id_proces_curent-1] = len; // Salvam lungimea

            MPI_Request r1, r2, r3;
            MPI_Isend(&lengths[id_proces_curent-1], 1, MPI_INT, id_proces_curent, 0, MPI_COMM_WORLD, &r1);
            sendRequests.push_back(r1);

            if (len > 0) {
                MPI_Isend(A.data() + offset, len, MPI_UNSIGNED_CHAR, id_proces_curent, 1, MPI_COMM_WORLD, &r2);
                MPI_Isend(B.data() + offset, len, MPI_UNSIGNED_CHAR, id_proces_curent, 2, MPI_COMM_WORLD, &r3);
                sendRequests.push_back(r2);
                sendRequests.push_back(r3);
                offset += len;
            }
        }

        vector<unsigned char> sum_global(N);
        vector<MPI_Request> recvRequests;
        vector<vector<unsigned char>> results(workers);
        vector<int> lengths_recv(workers);

        for (int src = 1; src <= workers; src++) {
            MPI_Request r_len, r_data;
            MPI_Irecv(&lengths_recv[src - 1], 1, MPI_INT, src, 3, MPI_COMM_WORLD, &r_len);
            recvRequests.push_back(r_len);
        }

        int finalCarry = 0;
        MPI_Request carry_req;
        MPI_Irecv(&finalCarry, 1, MPI_INT, workers, 5, MPI_COMM_WORLD, &carry_req);
        recvRequests.push_back(carry_req);

        vector<MPI_Request> data_recv_reqs;

        // 1. Asteptam lungimile
        for(int i=0; i<workers; i++) {
            MPI_Wait(&recvRequests[i], MPI_STATUS_IGNORE);
            int len = lengths_recv[i];
            if(len > 0) {
                // Acum ca stim lungimea, alocam si postam receptia pt date
                results[i].resize(len);
                MPI_Request r;
                MPI_Irecv(results[i].data(), len, MPI_UNSIGNED_CHAR, i+1, 4, MPI_COMM_WORLD, &r);
                data_recv_reqs.push_back(r);
            }
        }

        // 2. Asteptam carry-ul final
        MPI_Wait(&carry_req, MPI_STATUS_IGNORE);

        // 3. Asteptam toate datele
        MPI_Waitall(data_recv_reqs.size(), data_recv_reqs.data(), MPI_STATUSES_IGNORE);

        // 4. Asamblam rez
        for (int i = 0; i < workers; i++) {
            int pos = i * chunk;
            for (int j = 0; j < (int)results[i].size(); j++) {
                if (pos + j < N) {
                    sum_global[pos + j] = results[i][j];
                }
            }
        }

        if (finalCarry > 0) {
            sum_global.push_back(finalCarry);
        }

        // 5. Așteptam toate trimiterile
        MPI_Waitall(sendRequests.size(), sendRequests.data(), MPI_STATUSES_IGNORE);

        scrieRezultat("Numar_3.txt", sum_global);

    } else {
        // worker
        int len;
        MPI_Request reqLen;
        MPI_Irecv(&len, 1, MPI_INT, 0, 0, MPI_COMM_WORLD, &reqLen);
        MPI_Wait(&reqLen, MPI_STATUS_IGNORE);

        if (len > 0) {
            vector<unsigned char> A(len), B(len), sum(len);
            MPI_Request reqA, reqB;
            MPI_Irecv(A.data(), len, MPI_UNSIGNED_CHAR, 0, 1, MPI_COMM_WORLD, &reqA);
            MPI_Irecv(B.data(), len, MPI_UNSIGNED_CHAR, 0, 2, MPI_COMM_WORLD, &reqB);

            int carry_in = 0;
            MPI_Request reqCarryRecv;
            bool hasCarry = (rank > 1);
            if (hasCarry) {
                MPI_Irecv(&carry_in, 1, MPI_INT, rank - 1, 10, MPI_COMM_WORLD, &reqCarryRecv);
            }

            MPI_Wait(&reqA, MPI_STATUS_IGNORE);
            MPI_Wait(&reqB, MPI_STATUS_IGNORE);
            if (hasCarry) {
                MPI_Wait(&reqCarryRecv, MPI_STATUS_IGNORE);
            }

            int carry_out = carry_in;
            for (int i = 0; i < len; i++) {
                int s = A[i] + B[i] + carry_out;
                sum[i] = s % 10;
                carry_out = s / 10;
            }

            MPI_Request reqSendCarry;
            if (rank < size - 1) {
                MPI_Isend(&carry_out, 1, MPI_INT, rank + 1, 10, MPI_COMM_WORLD, &reqSendCarry);
            }

            MPI_Request reqSendLen, reqSendData;
            MPI_Isend(&len, 1, MPI_INT, 0, 3, MPI_COMM_WORLD, &reqSendLen);
            MPI_Isend(sum.data(), len, MPI_UNSIGNED_CHAR, 0, 4, MPI_COMM_WORLD, &reqSendData);

            MPI_Request reqFinalCarry;
            if (rank == size - 1) {
                MPI_Isend(&carry_out, 1, MPI_INT, 0, 5, MPI_COMM_WORLD, &reqFinalCarry);
            }

            MPI_Wait(&reqSendLen, MPI_STATUS_IGNORE);
            MPI_Wait(&reqSendData, MPI_STATUS_IGNORE);
            if (rank < size - 1) MPI_Wait(&reqSendCarry, MPI_STATUS_IGNORE);
            if (rank == size - 1) MPI_Wait(&reqFinalCarry, MPI_STATUS_IGNORE);

        } else {
            int carry_in = 0;
            MPI_Request reqCarryRecv;
            bool hasCarry = (rank > 1);
            if (hasCarry) {
                MPI_Irecv(&carry_in, 1, MPI_INT, rank - 1, 10, MPI_COMM_WORLD, &reqCarryRecv);
                MPI_Wait(&reqCarryRecv, MPI_STATUS_IGNORE);
            }

            MPI_Request reqSendCarry;
            if (rank < size - 1) {
                MPI_Isend(&carry_in, 1, MPI_INT, rank + 1, 10, MPI_COMM_WORLD, &reqSendCarry);
            }

            MPI_Request reqSendLen;
            MPI_Isend(&len, 1, MPI_INT, 0, 3, MPI_COMM_WORLD, &reqSendLen);

            MPI_Request reqFinalCarry;
            if (rank == size - 1) {
                MPI_Isend(&carry_in, 1, MPI_INT, 0, 5, MPI_COMM_WORLD, &reqFinalCarry);
            }

            // Asteptam
            MPI_Wait(&reqSendLen, MPI_STATUS_IGNORE);
            if (rank < size - 1) MPI_Wait(&reqSendCarry, MPI_STATUS_IGNORE);
            if (rank == size - 1) MPI_Wait(&reqFinalCarry, MPI_STATUS_IGNORE);
        }
    }
}

int main(int argc, char **argv) {
    //generate_number_in_file("C:\\Users\\razva\\Desktop\\PPD\\TemaMPI\\Numar_1_1000.txt", 1000);
    //generate_number_in_file("C:\\Users\\razva\\Desktop\\PPD\\TemaMPI\\Numar_2_16.txt", 100);

    //v0_secvential("Numar_1.txt", "Numar_2.txt");

    MPI_Init(&argc, &argv);
    //v1_mpi("Numar_1.txt", "Numar_2.txt");
    //v2_scatter_gather();
    v3_mpi_asincron();
    MPI_Finalize();

    return 0;
}
