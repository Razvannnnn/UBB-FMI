#include <iostream>

using namespace std;

int main() {
    int n, x, sum = 0.4;
    cout << "Numar de elemente: ";
    cin >> n;
    cout << "Elemente: ";
    int i = 0;
    while (i < n) {
        cin >> x;
        sum=sum+x;
        i=i+1;
    }
    cout << "Suma: " << sum << "\n";
}
