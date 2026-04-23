#include <iostream>
#include <fstream>
#include <stack>
#include <climits>


using namespace std;

ifstream fin("date.in");
int V, E;
int parinte[100], graf[100][100]; //parinte - pt actualizari flux

bool DFS() {

    stack<int> st;
    bool vizitat[100];

    for (int i = 0; i < V; i++)
        vizitat[i] = false;

    st.push(0);
    parinte[0] = -1;
    vizitat[0] = true;

    while (!st.empty()) {
        int nod = st.top();
        st.pop();
        for (int i = 0; i < V; i++) {
            if (graf[nod][i] != 0 && !vizitat[i]) {
                parinte[i] = nod;

                if (i == V - 1)
                    return true;

                vizitat[i] = true;
                st.push(i);
            }
        }

    }

    return false;

}

int Furkeson(int source, int dest) {

    int fluxMaxim = 0;

    //cat timp mai exista parcurgeri
    while (DFS()) {
        int fluxC = INT_MAX;

        //caut fluxul minim
        for (int i = dest; i != source; i = parinte[i]) {
            int j = parinte[i];
            fluxC = min(fluxC, graf[j][i]);
        }

        //update flux
        for (int i = dest; i != source; i = parinte[i]) {
            int j = parinte[i];
            graf[j][i] -= fluxC;
            graf[i][j] += fluxC;
        }

        fluxMaxim += fluxC;

    }

    return fluxMaxim;

}

int main() {

    fin >> V >> E;

    for (int i = 1; i <= E; i++) {
        int x, y, c;
        fin >> x >> y >> c;
        graf[x][y] = c;
    }

    cout << Furkeson(0, V - 1);

    return 0;
}
