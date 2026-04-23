#include <iostream>
#include <fstream>
#include <cmath>

using namespace std;

struct muchie {
    int i, j;
    double cost;
} x[5000];

int V, E = 0, t[5000];
double cost = 0.0;

double dist(int x1, int y1, int x2, int y2) {
    return sqrt(pow(x1 - x2, 2) + pow(y1 - y2, 2));
}

void kruskal() {

    for (int i = 0; i < E - 1; i++) {
        for (int j = i + 1; j < E; j++) {
            if (x[i].cost > x[j].cost) {
                swap(x[i], x[j]);
            }
        }
    }


    for (int i = 0; i < V; i++) {
        t[i] = i;
    }


    for (int i = 0; i < E; i++) {
        if (t[x[i].i] != t[x[i].j]) {
            cost += x[i].cost;

            int oldParent = t[x[i].j];
            int newParent = t[x[i].i];

            for (int j = 0; j < V; j++) {
                if (t[j] == oldParent) {
                    t[j] = newParent;
                }
            }
        }
    }
}

int main() {
    ifstream fin("date.in");

    fin >> V;

    pair<int, int> orase[5000];

    for (int i = 0; i < V; i++) {
        fin >> orase[i].first >> orase[i].second;
    }

    for (int i = 0; i < V; i++) {
        for (int j = i + 1; j < V; j++) {
            x[E].i = i;
            x[E].j = j;
            x[E].cost = dist(orase[i].first, orase[i].second, orase[j].first, orase[j].second);
            E++;
        }
    }

    kruskal();

    cout << cost << "\n";
    return 0;
}
