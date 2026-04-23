#include <iostream>
#include <fstream>
#include <algorithm>

using namespace std;

struct muchie{
    int i,j,cost;
};

int V, E, t[5000];

muchie x[5000];

int main() {
    std::ifstream fin("input.txt");

    fin >> V >> E;

    for(int i = 0 ; i < E ; ++i)
        fin >> x[i].i >> x[i].j >> x[i].cost;

    for(int i = 0; i < E - 1; i++) {
        for(int j = i + 1; j < E; j++) {
            if(x[i].cost > x[j].cost)
                swap(x[i],x[j]);
        }
    }

    for(int i = 0; i < V ; i++)
        t[i] = i;

    int cost = 0;
    muchie mArb[5000];
    int nMuchii = 1;
    for(int i = 0 ; i < E ; i++)
        if(t[x[i].i] != t[x[i].j]) {
            // daca extremitatile muchiei
            // fac parte din subarbori diferiti, aleg aceasta muchie
            cost += x[i].cost;
            mArb[nMuchii].i = x[i].i;
            mArb[nMuchii].j = x[i].j;
            mArb[nMuchii].cost = x[i].cost;
            nMuchii++;

            int ax = t[x[i].i], ay = t[x[i].j];
            for(int j = 0; j < V ; j++)
                if(t[j] == ay)
                    t[j] = ax;
        }

    cout << cost << "\n";
    cout << nMuchii - 1 << '\n';
    for(int i = 1; i < nMuchii; i++) {
        cout << mArb[i].i << " " << mArb[i].j << '\n';
    }
    return 0;
}