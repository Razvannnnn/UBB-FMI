#include <iostream>
#include <fstream>

using namespace std;

ifstream fin("graf.txt");

int n, m, G[11][100];
struct muchie{
    int x,y;
}lm[100];

int main() {

    fin>>n>>m;
    for(int i=1; i<=m; i++){
        fin>>lm[i].x>>lm[i].y;
    }

    for(int i=1; i<=m; i++){
        G[lm[i].x][i]=1;
        G[lm[i].y][i]=1;
    }

    for (int i = 1; i <= n; i++) {
        for (int j = 1; j <= m; j++) {
            cout << G[i][j] << " ";
        }
        cout << endl;
    }

    return 0;
}
