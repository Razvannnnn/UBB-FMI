#include <iostream>
#include <fstream>

void generateFile() {
    std::ofstream fout("words.txt");
    srand(time(0));
    int N = 10000;

    const char charset[] = "abcdefghijklmnopqrstuvwxyz";

    for(int i=0;i<N;i++) {
        int length = 1 + rand() % 20;
        std::string word = "";
        for(int j = 0; j < length; ++j) {
            word += charset[rand() % (sizeof(charset) - 1)];
        }
        fout << word << "\n";
    }
    fout.close();
    std::cout << "Fisier generat\n";
}

int main() {
    generateFile();
    return 0;
}