#include <iostream>
#include <fstream>

using namespace std;

ifstream fin("graf.txt");

int G[11][11], M[100][2], n, lg;

bool verify(int x, int y) {
    for (int i = 1; i <= lg; i++) {
        if (M[i][0] == x && M[i][1] == y)
            return false;
        if (M[i][0] == y && M[i][1] == x)
            return false;
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
            if (G[i][j] == 1) {
                if (verify(i, j)) {
                    M[++lg][0] = i;
                    M[lg][1] = j;
                }
            }
        }
    }


    for (int i = 1; i <= lg; i++) {
        cout << M[i][0] << " " << M[i][1] << endl;
    }

    return 0;
}
