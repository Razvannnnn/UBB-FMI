#include <iostream>
#include <fstream>
#include <string>
#include <vector>
#include <thread>
#include <mutex>
#include <atomic>
#include <chrono>
#include <random>
#include <queue>
#include <sstream>

using namespace std;

// --- 1. Structuri de Date ---

struct Pereche {
    int id;
    int nota;
};

// Nod pentru Lista Inlantuita
struct Nod {
    int id;
    int nota;
    Nod* next;

    Nod(int _id, int _nota) : id(_id), nota(_nota), next(nullptr) {}
};

// Lista Inlantuita Thread-Safe (Sincronizare la nivel de lista)
class ListaStudenti {
private:
    Nod* head;
    std::mutex mtx; // Mutex pentru intreaga lista

public:
    ListaStudenti() : head(nullptr) {}

    ~ListaStudenti() {
        Nod* current = head;
        while (current != nullptr) {
            Nod* next = current->next;
            delete current;
            current = next;
        }
    }

    // Adauga sau actualizeaza nota (Thread-safe)
    void adaugaNota(int id, int nota) {
        std::lock_guard<std::mutex> lock(mtx); // Blocare lista

        Nod* current = head;
        while (current != nullptr) {
            if (current->id == id) {
                current->nota += nota; // Actualizare
                return;
            }
            current = current->next;
        }

        // Daca nu exista, adaugam la inceput (mai rapid)
        Nod* nou = new Nod(id, nota);
        nou->next = head;
        head = nou;
    }

    // Salvare in fisier (Thread-safe teoretic, dar apelat secvential la final)
    void salveaza(const string& filename) {
        ofstream fout(filename);
        Nod* current = head;
        while (current != nullptr) {
            fout << current->id << "," << current->nota << "\n";
            current = current->next;
        }
        fout.close();
    }

    // Pentru debug/verificare
    int getCount() {
        int c = 0;
        Nod* cur = head;
        while(cur) { c++; cur = cur->next; }
        return c;
    }
};

// Coada Thread-Safe fara Variabile Conditionale (doar protectie date)
class Coada {
private:
    std::queue<Pereche> q;
    std::mutex mtx;

public:
    void push(int id, int nota) {
        std::lock_guard<std::mutex> lock(mtx);
        q.push({id, nota});
    }

    // Returneaza true daca a scos ceva, false daca e goala
    bool tryPop(Pereche& rez) {
        std::lock_guard<std::mutex> lock(mtx);
        if (q.empty()) {
            return false;
        }
        rez = q.front();
        q.pop();
        return true;
    }

    bool isEmpty() {
        std::lock_guard<std::mutex> lock(mtx);
        return q.empty();
    }
};

// --- 2. Utilitare ---

void genereazaDateTest() {
    random_device rd;
    mt19937 gen(rd());
    uniform_int_distribution<> distId(1000, 1199); // 200 studenti
    uniform_int_distribution<> distNota(1, 10);
    uniform_int_distribution<> distCount(80, 120); // Minim 80 note per fisier

    for (int i = 1; i <= 10; ++i) {
        string fname = "proiect" + to_string(i) + ".txt";
        ofstream fout(fname);
        int count = distCount(gen);
        for (int j = 0; j < count; ++j) {
            fout << distId(gen) << "," << distNota(gen) << "\n";
        }
        fout.close();
    }
    cout << "[INFO] Generate 10 fisiere cu date de test.\n";
}

// Citire fisier helper
void citesteFisierInLista(string filename, ListaStudenti& lista) {
    ifstream fin(filename);
    if (!fin.is_open()) return;
    int id, nota;
    char virgula;
    while (fin >> id >> virgula >> nota) {
        lista.adaugaNota(id, nota);
    }
    fin.close();
}

// --- 3. Implementare Secventiala ---

void rezolvareSecventiala() {
    ListaStudenti lista;
    for (int i = 1; i <= 10; ++i) {
        citesteFisierInLista("proiect" + to_string(i) + ".txt", lista);
    }
    lista.salveaza("rezultate.txt");
}

// --- 4. Implementare Paralela ---

std::atomic<int> indexFisierGlobal(1); // Pentru a imparti fisierele intre Readeri
std::atomic<int> readersActive(0);     // Cati Readeri inca lucreaza

void readerFunc(Coada* coada) {
    while (true) {
        // Luam urmatorul fisier disponibil
        int fileIdx = indexFisierGlobal.fetch_add(1);
        if (fileIdx > 10) break;

        string fname = "proiect" + to_string(fileIdx) + ".txt";
        ifstream fin(fname);
        if (fin.is_open()) {
            int id, nota;
            char virgula;
            while (fin >> id >> virgula >> nota) {
                coada->push(id, nota);
            }
            fin.close();
        }
    }
    readersActive.fetch_sub(1);
}

void workerFunc(Coada* coada, ListaStudenti* lista) {
    Pereche p;
    while (true) {
        // Incercam sa scoatem din coada
        if (coada->tryPop(p)) {
            // Procesam
            lista->adaugaNota(p.id, p.nota);
        } else {
            // Coada pare goala. Verificam daca mai sunt Readeri activi.
            if (readersActive.load() == 0 && coada->isEmpty()) {
                // Readerii au terminat SI coada e goala -> STOP
                break;
            } else {
                // Busy-waiting cu yield (pentru a nu bloca procesorul complet 100%, dar fara wait condition)
                // Aceasta este cerinta: "busy-waiting"
                std::this_thread::yield();
            }
        }
    }
}

void rezolvareParalela(int P, int Pr) {
    ListaStudenti lista;
    Coada coada;

    // Reset global
    indexFisierGlobal.store(1);
    readersActive.store(Pr);

    vector<thread> threads;

    // Pornire Readeri
    for (int i = 0; i < Pr; ++i) {
        threads.emplace_back(readerFunc, &coada);
    }

    // Pornire Workers (P_ww = P - Pr)
    int P_ww = P - Pr;
    for (int i = 0; i < P_ww; ++i) {
        threads.emplace_back(workerFunc, &coada, &lista);
    }

    // Join
    for (auto& t : threads) {
        if (t.joinable()) t.join();
    }

    lista.salveaza("rezultate.txt");
}

// --- 5. Main si Analiza ---

int main() {
    genereazaDateTest();
    cout << "--------------------------------------------------\n";

    // i. Secvential
    auto start = chrono::high_resolution_clock::now();
    rezolvareSecventiala();
    auto end = chrono::high_resolution_clock::now();
    double tSeq = chrono::duration<double, milli>(end - start).count();
    cout << "Secvential: \t\t" << tSeq << " ms" << endl;
    cout << "--------------------------------------------------\n";

    // Vectori pentru configuratiile cerute
    // Format: {TotalThreads, Readers}
    vector<pair<int, int>> configs = {
            {4, 1}, {8, 1}, {16, 1}, // ii
            {4, 2}, {8, 2}, {16, 2}  // iii
    };

    cout << "Paralel (P=Total, Pr=Readers):" << endl;
    cout << "P\tPr\tPw\tTimp(ms)\tSpeedup(Ts/Tp)" << endl;

    for (auto& cfg : configs) {
        int P = cfg.first;
        int Pr = cfg.second;
        int Pw = P - Pr;

        start = chrono::high_resolution_clock::now();
        rezolvareParalela(P, Pr);
        end = chrono::high_resolution_clock::now();

        double tPar = chrono::duration<double, milli>(end - start).count();
        double speedup = tSeq / tPar;

        cout << P << "\t" << Pr << "\t" << Pw << "\t"
             << tPar << "\t\t" << speedup << endl;
    }

    return 0;
}