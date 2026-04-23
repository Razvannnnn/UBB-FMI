cmake_minimum_required(VERSION 3.28)
project(TemaMPI)

set(CMAKE_CXX_STANDARD 20)

find_package(MPI REQUIRED)

add_executable(TemaMPI main.cpp)

target_link_libraries(TemaMPI PRIVATE MPI::MPI_CXX)