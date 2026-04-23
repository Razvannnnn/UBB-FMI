#include <iostream>
#include <fstream>

using namespace std;

ifstream fin("input.txt");

int dist[100], n, m;

struct bellman{
    int x, y, c;
}muchie [100];

void relax(int u, int v, int c){
    if(dist[v] > dist[u] + c){
        dist[v] = dist[u] + c;
    }
}

bool BellmanFord(int src){
    for(int i=1; i<=n; i++)
        dist[i] = 999;

    dist[src]=0;

    for(int i=1; i<=n; i++){
        for(int j=1; j<=m; j++){
            relax(muchie[j].x, muchie[j].y, muchie[j].c);
        }
    }

    for(int j=1; j<=m; j++){
        if(dist[muchie[j].y] > dist[muchie[j].x] + muchie[j].c){
            return false;
        }
    }

    return true;
}

int main() {

    fin>>n>>m;

    for(int i=1; i<=m; i++){
        int x,y,c;
        fin>>x>>y>>c;
        muchie[i].x = x;
        muchie[i].y = y;
        muchie[i].c = c;
    }

    int src;
    cin>>src;

    if(BellmanFord(src)){
        for(int i=1; i<=n; i++){
            cout<<dist[i]<<" ";
        }
    }


    return 0;
}
