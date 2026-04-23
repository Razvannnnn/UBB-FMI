#include <iostream>

using namespace std;

float pi = 3.14;

struct Cerc {
    float raza;
    float perim;
    float aria;
};

int main() {
    struct Cerc cerc;
    cout << "Raza: ";
    cin >> cerc.raza;
    if (cerc.raza < 0) {
        cout << "Raza invalida!!";
    }
    else {
        cerc.perim = 2 * pi * cerc.raza;
        cerc.aria = pi * cerc.raza * cerc.raza;
        cout << "Perimetrul: " << cerc.perim << "\n";
        cout << "Aria: " << cerc.aria << "\n";
    }
}