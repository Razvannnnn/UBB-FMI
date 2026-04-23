cmake_minimum_required(VERSION 3.28)
project(Test)

set(CMAKE_CXX_STANDARD 20)

find_package(MPI REQUIRED)

add_executable(Test main.cpp
        generator.cpp
        generator.cpp)

target_link_libraries(Test PRIVATE MPI::MPI_CXX)
