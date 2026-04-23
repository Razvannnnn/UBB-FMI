#include <iostream>
#include <fstream>

using namespace std;

ifstream fin("graf.txt");

int G[11][11], n, lg, I[11][100];

bool verify(int x, int y) {

    for (int j = 1; j <= lg; j++) {
        if (I[x][j] == 1 && I[y][j]==1){
            return false;
        }
    }

    return true;
}

int main() {

    fin >> n;
    for (int i = 1; i <= n; i++) {
        for (int j = 1; j <= n; j++) {
            fin >> G[i][j];
        }
    }


    for (int i = 1; i <= n; i++) {
        for (int j = 1; j <= n; j++) {
            if (G[i][j] == 1 && verify(i, j)) {
                I[i][++lg] = 1;
                I[j][lg] = 1;
            }
        }
    }

    for (int i = 1; i <= n; i++) {
        for (int j = 1; j <= lg; j++) {
            cout << I[i][j] << " ";
        }
        cout << endl;
    }


    return 0;
}
