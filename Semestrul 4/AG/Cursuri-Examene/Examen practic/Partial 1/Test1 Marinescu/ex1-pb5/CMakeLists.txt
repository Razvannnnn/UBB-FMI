cmake_minimum_required(VERSION 3.27)
project(transformari)

set(CMAKE_CXX_STANDARD 17)

add_executable(transformari main.cpp)

# Adaugă fișierul "graf.txt" ca o resursă în proiectul tău
configure_file(graf.txt graf.txt COPYONLY)
