#include <iostream>
#include <fstream>
#include <vector>

using namespace std;

ifstream fin("input.txt");

int n, m;
const int maxn = 101;

vector<int> G[maxn];
bool visited[maxn];

bool dfs(int node, int parent) {
    visited[node] = true;
    for (int i = 0; i < G[node].size(); i++) {
        int neighbor = G[node][i];
        if (!visited[neighbor]) {
            if (dfs(neighbor, node)) {
                return true;
            }
        } else if (neighbor != parent) {
            return true;
        }
    }
    return false;
}



bool verifyCycle(){
    for(int nod=1; nod<=n; nod++){
        if(!visited[nod]){
            if(dfs(nod, -1)){
                return false;
            }
        }
    }
    return true;
}

int main() {

    fin >> n >> m;
    int x, y;
    while (fin >> x >> y) {
        G[x].push_back(y);
        G[y].push_back(x);
    }

    for (int i = 1; i <= n; i++) {
        visited[i] = false;
    }
    
    if (verifyCycle()) {
        cout << "DA";
    } else {
        cout << "NU";
    }


    return 0;
}
