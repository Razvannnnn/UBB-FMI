cmake_minimum_required(VERSION 3.27)
project(ex2_pb5)

set(CMAKE_CXX_STANDARD 17)

add_executable(ex2_pb5 main.cpp)

configure_file(graf.txt graf.txt COPYONLY)