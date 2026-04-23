cmake_minimum_required(VERSION 3.28)
project(PentruMPI)

set(CMAKE_CXX_STANDARD 17)

find_package(MPI REQUIRED)

add_executable(PentruMPI main.cpp)

target_link_libraries(PentruMPI PRIVATE MPI::MPI_CXX)