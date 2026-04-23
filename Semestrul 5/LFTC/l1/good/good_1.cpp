#include <bits/stdc++.h>
float PI = 3.14;

struct Circle {
  float radius;
  float perim;
  float area;
};

int main() {
  struct Circle circle;
  std::cout << "Raza cercului: ";
  std::cin >> circle.radius;
  if (circle.radius < 0) {
    std::cout << "Raza gresita!";
  } else {
    circle.perim = 2 * PI * circle.radius;
    circle.area = PI * circle.radius * circle.radius;
    std::cout << "Perimetru: " << circle.perim << "\n";
    std::cout << "Arie: " << circle.area << "\n";
  }
}
