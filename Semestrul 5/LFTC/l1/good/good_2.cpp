#include <bits/stdc++.h>
int main() {
  int x, y;
  std::cout << "2 numere cu spatiu: ";
  std::cin >> x >> y;
  while (y != 0) {
    int r = x % y;
    x = y;
    y = r;
  }
  std::cout << "cmmdc = " << x << "\n";
}
