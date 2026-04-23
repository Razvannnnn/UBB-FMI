#include <iostream>
#include <fstream>
#include <cmath>

#define inf 999
using namespace std;

ifstream fin("input.txt");
int n, m;
int coord[100][3], G[100][100];
int dist[100];


struct bellman {
    int x, y, c;
} muchie[100];


void relax(int u, int v, int c) {
    if (dist[v] > dist[u] + c)
        dist[v] = dist[u] + c;
}

bool BellmanFord(int src) {
    for (int i = 1; i <= n; i++)
        dist[i] = inf;

    dist[src] = 0;

    for (int i = 1; i <= n; i++) {
        for (int j = 1; j <= m; j++) {
            relax(muchie[j].x, muchie[j].y, muchie[j].c);
        }
    }

    for (int j = 1; j <= m; j++) {
        //ciclu negativ
        if (dist[muchie[j].y] > dist[muchie[j].x] + muchie[j].c)
            return false;
    }

    return true;
}

void find_way() {
    int src, dest;

    cin >> src >> dest;

    if (BellmanFord(src)) {
        cout << dist[dest];
    } else {
        cout << "NO WAY";
    }


}


int main() {

    fin >> n >> m;
    for (int i = 1; i <= n; i++)
        fin >> coord[i][1] >> coord[i][2];

    for (int i = 1; i <= m; i++) {
        int x, y;
        fin >> x >> y;
        int xA = coord[x][1], yA = coord[x][2], xB = coord[y][1], yB = coord[y][2];
        muchie[i].x = x;
        muchie[i].y = y;
        muchie[i].c = sqrt((xB - xA) * (xB - xA) + (yB - yA) * (yB - yA));


    }

    find_way();

    return 0;
}
