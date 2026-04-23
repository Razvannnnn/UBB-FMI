#include <bits/stdc++.h>

int main() {
  int n, x, sum = 0;
  std::cout << "n: ";
  std::cin >> n;
  std::cout << "Cele " << n << " numere cu spatiu: ";
  for (int i = 0; i < n; i += 1) {
    std::cin >> x;
    sum = sum + x;
  }
  std::cout << "Suma este: " << sum << "\n";
}
