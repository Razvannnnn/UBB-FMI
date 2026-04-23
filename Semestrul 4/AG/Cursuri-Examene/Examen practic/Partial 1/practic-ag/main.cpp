#include <iostream>
#include <fstream>

using namespace std;

ifstream fin("input.txt");

int a[101][101], n, vizitat[101], m;

void dfs(int inceput) {
    int i;

    vizitat[inceput] = 1;
    for (i = 1; i <= n; i++) {
        if (vizitat[i] == 0 && a[inceput][i] == 1)
            dfs(i);
    }

}


int main() {

    int i, j, inceput;
    fin >> n >> m;
    while (fin >> i >> j) {
        a[i][j] = 1;
        a[j][i] = 1;
    }

    cin >> inceput;
    int dest;
    cin >> dest;

    a[inceput][dest] = 0;
    a[dest][inceput] = 0;

    dfs(inceput);
    if (vizitat[dest] == 1) cout << "DA";
    else {
        cout << "NU";
    };


    return 0;
}
