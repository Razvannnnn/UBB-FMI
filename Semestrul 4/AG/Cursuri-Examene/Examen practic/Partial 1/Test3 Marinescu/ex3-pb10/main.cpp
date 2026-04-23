#include <iostream>
#include <fstream>
#include <queue>

using namespace std;

ifstream fin("input.txt");

int n, m, G[100][100];

int main() {

    fin >> n >> m;
    for (int i = 0; i < m; i++) {
        int x, y;
        fin >> x >> y;
        G[x][y] = 1;
        G[y][x] = 1;
    }

    bool viz[100] = {false};

    for (int i = 1; i <= n; i++) {
        if (!viz[i]) {
            queue<int> Q;
            Q.push(i);

            viz[i] = true;

            while (!Q.empty()) {
                int nodCurent = Q.front();
                cout << nodCurent << " ";

                for (int j = 1; j <= n; j++) {
                    if (G[nodCurent][j] == 1 && !viz[j]) {
                        Q.push(j);
                        viz[j] = true;
                    }
                }
                Q.pop();
            }
            cout << endl;
        }

    }


    return 0;
}
