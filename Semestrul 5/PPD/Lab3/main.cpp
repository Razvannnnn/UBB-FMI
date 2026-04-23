#include <iostream>
#include <vector>
#include <thread>
#include <chrono>
#include <cstdlib>

using namespace std;

vector<int> generateArray(int n, int max) {
    vector<int> vec;
    for(int i = 0; i < n; i++) {
        vec.push_back(rand() % max);
    }
    return vec;
}

int sumArray(const vector<int>& a, const vector<int>& b, vector<int>& c) {
    int len = a.size();
    auto t0 = chrono::high_resolution_clock::now();
    for(int i = 0; i < len; i++) {
        c[i] = a[i] + b[i];
    }
    auto t1 = chrono::high_resolution_clock::now();
    return chrono::duration_cast<chrono::nanoseconds>(t1 - t0).count();
}

int runCyc(const vector<int>& A, const vector<int>& B, vector<int>& C, int p) {
    vector<thread> threads;
    auto t0 = chrono::high_resolution_clock::now();
    for(int id = 0; id < p; id++) {
        threads.emplace_back([id, p, &A, &B, &C] () {
            for(int i = id; i < A.size(); i += p) {
                C[i] = A[i] + B[i];
            }
        });
    }
    for(auto& th : threads) {
        th.join();
    }
    auto t1 = chrono::high_resolution_clock::now();
    return chrono::duration_cast<chrono::nanoseconds>(t1 - t0).count();
}

int runBlock(const vector<int>& a, const vector<int>& b, vector<int>& c, int p) {
    vector<thread> pool;
    auto t1 = chrono::high_resolution_clock::now();
    int len = a.size() / p;
    for(int id = 0; id < p; id++) {
        int min = id * len;
        int max = min + len;
        if(id == p - 1) {
            max = a.size();
        }
        pool.emplace_back([&a, &b, &c, min, max] () {
            for(int i = min; i < max; i++) {
                c[i] = a[i] + b[i];
            }
        });
    }
    for(auto& th : pool) {
        th.join();
    }
    return chrono::duration_cast<chrono::nanoseconds>(chrono::high_resolution_clock::now() - t1).count();
}

bool verifyEqual(const vector<int>& X, const vector<int>& Y) {
    if (X.size() != Y.size()) return false;
    for (size_t i = 0; i < X.size(); i++) {
        if (X[i] != Y[i]) return false;
    }
    return true;
}

vector<int> v1(1000000, 0);
vector<int> v2(1000000, 0);
vector<int> v3(1000000, 0);
vector<int> a, b;

int main() {
    int p = 3;
    a = generateArray(1000000, 50000);
    b = generateArray(1000000, 50000);

    int t1 = sumArray(a, b, v1);
    int t2 = runCyc(a, b, v2, p);
    int t3 = runBlock(a, b, v3, p);

    cout<<verifyEqual(v1, v2)<<" "<<verifyEqual(v1, v3)<<endl;

    cout<<"Single Thread Time: "<<t1<<" ns"<<endl;
    cout<<"Cyclic Multi Thread Time: "<<t2<<" ns"<<endl;
    cout<<"Block Multi Thread Time: "<<t3<<" ns"<<endl;

    return 0;
}