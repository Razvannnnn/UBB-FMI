#include <iostream>

using namespace std;

int main() {
    int x, y;
    cout <<"CMMDC\n" << "x=";
    cin >> x;
    cout << "y=";
    cin >> y;
    while (y != 0) {
        int r = x % y;
        x = y;
        y = r;
    }
    cout << "cmmdc = " << x << "\n";
}