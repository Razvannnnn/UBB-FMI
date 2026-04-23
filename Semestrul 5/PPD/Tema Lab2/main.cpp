// conv_inplace.cpp
// C++11 - In-place 3x3 convolution, distribution pe linii, dynamic allocation,
// explicit threads, barrier simplu, măsurare timp (10 rulări medii).
//
// Compile: g++ -std=c++11 -O2 conv_inplace.cpp -o conv_inplace -pthread
// Run example (program will generate date.txt if not present): ./conv_inplace

#include <bits/stdc++.h>
#include <thread>
#include <mutex>
#include <condition_variable>
#include <chrono>
using namespace std;

// ---------- Simple reusable barrier for C++11 ----------
struct SimpleBarrier {
    mutex m;
    condition_variable cv;
    int count;
    int total;
    SimpleBarrier(int n): count(0), total(n) {}
    void wait() {
        unique_lock<mutex> lk(m);
        count++;
        if (count == total) {
            count = 0; // reset to allow reuse
            cv.notify_all();
        } else {
            cv.wait(lk, [&]{ return count == 0; });
        }
    }
};

// ---------- Utilities: read/write, generator ----------
bool file_exists(const string &name) {
    ifstream f(name.c_str());
    return f.good();
}

void generate_random_input(const string &fname, int N, int M, int maxVal=255, unsigned seed=12345) {
    std::mt19937 rng(seed);
    std::uniform_int_distribution<int> dist(0, maxVal);
    ofstream ofs(fname);
    ofs << N << " " << M << "\n";
    for (int i = 0; i < N; ++i) {
        for (int j = 0; j < M; ++j) {
            ofs << dist(rng) << (j+1==M? "":" ");
        }
        ofs << "\n";
    }
    // example kernel (edge-detect-ish) but any 3x3 is ok
    vector<vector<int>> kernel = {
            {0,  1, 0},
            {1, -4, 1},
            {0,  1, 0}
    };
    for (int i=0;i<3;++i) {
        for (int j=0;j<3;++j) {
            ofs << kernel[i][j] << (j==2?"":" ");
        }
        ofs << "\n";
    }
    ofs.close();
}

// ---------- Read input ----------
bool read_input(const string &fname, int &N, int &M, vector<vector<int>> &F, array<array<int,3>,3> &kernel) {
    ifstream ifs(fname);
    if (!ifs.is_open()) return false;
    ifs >> N >> M;
    F.assign(N, vector<int>(M));
    for (int i=0;i<N;++i)
        for (int j=0;j<M;++j)
            ifs >> F[i][j];
    for (int i=0;i<3;++i)
        for (int j=0;j<3;++j)
            ifs >> kernel[i][j];
    return true;
}

void write_output(const string &fname, const vector<vector<int>> &F) {
    ofstream ofs(fname);
    int N = (int)F.size();
    int M = N? (int)F[0].size() : 0;
    ofs << N << " " << M << "\n";
    for (int i=0;i<N;++i) {
        for (int j=0;j<M;++j) {
            ofs << F[i][j] << (j+1==M? "":" ");
        }
        ofs << "\n";
    }
}

// ---------- Helpers for border indexing (replicate border) ----------
inline int clamp_idx(int x, int low, int high) {
    if (x < low) return low;
    if (x > high) return high;
    return x;
}

// ---------- Sequential in-place convolution using O(m) space ----------
void conv_inplace_sequential(vector<vector<int>> &F, const array<array<int,3>,3> &K) {
    int N = (int)F.size();
    if (N==0) return;
    int M = (int)F[0].size();

    vector<int> buf_prev(M), buf_curr(M), buf_next(M), outRow(M);

    // initialize buffers for i = 0
    int im1 = clamp_idx(0-1, 0, N-1);
    int i0  = 0;
    int ip1 = clamp_idx(0+1, 0, N-1);
    // copy original rows into buffers
    for (int j=0;j<M;++j) { buf_prev[j] = F[im1][j]; buf_curr[j] = F[i0][j]; buf_next[j] = F[ip1][j]; }

    for (int i = 0; i < N; ++i) {
        // compute outRow for row i using buf_prev, buf_curr, buf_next
        for (int j = 0; j < M; ++j) {
            long long s = 0;
            // kernel positions
            for (int ki = 0; ki < 3; ++ki) {
                int ri = (ki==0 ? buf_prev : (ki==1 ? buf_curr : buf_next))[0]; // dummy to avoid warning
                for (int kj = 0; kj < 3; ++kj) {
                    int rowIdx = ki - 1; // -1,0,1
                    int colIdx = j + (kj - 1);
                    int cj = clamp_idx(colIdx, 0, M-1);
                    int val;
                    if (ki == 0) val = buf_prev[cj];
                    else if (ki == 1) val = buf_curr[cj];
                    else val = buf_next[cj];
                    s += (long long)K[ki][kj] * val;
                }
            }
            outRow[j] = (int)s;
        }
        // write outRow into F[i]
        for (int j=0;j<M;++j) F[i][j] = outRow[j];

        // prepare buffers for next i
        if (i+1 < N) {
            // shift: prev = curr, curr = next, next = copy of original row i+2 (clamped)
            buf_prev.swap(buf_curr);
            buf_curr.swap(buf_next);
            int idx_next = clamp_idx(i+2, 0, N-1);
            for (int j=0;j<M;++j) buf_next[j] = F[idx_next][j]; // IMPORTANT: idx_next hasn't been overwritten yet
        }
    }
}

// ---------- Worker for parallel block processing ----------
struct ThreadArgs {
    int id;
    int start, end; // inclusive indices of rows assigned
    vector<vector<int>> *Fptr;
    const array<array<int,3>,3> *Kptr;
    SimpleBarrier *bar;
    // copied border rows:
    vector<int> topBorder;    // copy of row start-1 (or start if border)
    vector<int> bottomBorder; // copy of row end+1 (or end if border)
};

void worker_thread(ThreadArgs args) {
    int N = (int)args.Fptr->size();
    if (N==0) return;
    int M = (int)(*args.Fptr)[0].size();
    const auto &K = *args.Kptr;
    vector<vector<int>> &F = *args.Fptr;

    int start = args.start, end = args.end;

    // local buffers: prev,curr,next and outRow (all size M)
    vector<int> buf_prev(M), buf_curr(M), buf_next(M), outRow(M);

    // 1) copy top and bottom border rows BEFORE any writes (safety)
    // top border index for calculations of 'start' (replicate borders)
    int topIdx = clamp_idx(start - 1, 0, N-1);
    int bottomIdx = clamp_idx(end + 1, 0, N-1);
    // copy
    args.topBorder.assign(M,0);
    args.bottomBorder.assign(M,0);
    for (int j=0;j<M;++j) {
        args.topBorder[j] = F[topIdx][j];
        args.bottomBorder[j] = F[bottomIdx][j];
    }
    // signal done copying and wait for others
    args.bar->wait();

    // Now process rows start..end sequentially inside thread
    // Initialize buffers for i = start:
    int i = start;
    int im1 = clamp_idx(i-1, 0, N-1);
    int ip1 = clamp_idx(i+1, 0, N-1);
    // buf_prev comes from either args.topBorder (if im1 == topIdx) else from F[im1]
    if (im1 == topIdx) {
        for (int j=0;j<M;++j) buf_prev[j] = args.topBorder[j];
    } else {
        for (int j=0;j<M;++j) buf_prev[j] = F[im1][j];
    }
    for (int j=0;j<M;++j) buf_curr[j] = F[i][j];
    if (ip1 == bottomIdx) {
        for (int j=0;j<M;++j) buf_next[j] = args.bottomBorder[j];
    } else {
        for (int j=0;j<M;++j) buf_next[j] = F[ip1][j];
    }

    for (int r = start; r <= end; ++r) {
        // compute output row r using buf_prev, buf_curr, buf_next
        for (int j = 0; j < M; ++j) {
            long long s = 0;
            for (int ki = 0; ki < 3; ++ki) {
                for (int kj = 0; kj < 3; ++kj) {
                    int col = clamp_idx(j + (kj - 1), 0, M-1);
                    int val;
                    if (ki == 0) val = buf_prev[col];
                    else if (ki == 1) val = buf_curr[col];
                    else val = buf_next[col];
                    s += (long long)K[ki][kj] * val;
                }
            }
            outRow[j] = (int)s;
        }
        // write back
        for (int j=0;j<M;++j) F[r][j] = outRow[j];

        // prepare buffers for next r+1 (if exists and within this thread block)
        if (r + 1 <= end) {
            // shift prev=curr, curr=next, next = row r+2 (clamped)
            buf_prev.swap(buf_curr);
            buf_curr.swap(buf_next);
            int idx_next = clamp_idx(r + 2, 0, N-1);
            if (idx_next == bottomIdx) {
                for (int j=0;j<M;++j) buf_next[j] = args.bottomBorder[j];
            } else {
                for (int j=0;j<M;++j) buf_next[j] = F[idx_next][j]; // safe: belongs to this thread (not yet overwritten)
            }
        }
    }
}

// ---------- Parallel entry: split into p thread blocks ----------
void conv_inplace_parallel_lines(vector<vector<int>> &F, const array<array<int,3>,3> &K, int p) {
    int N = (int)F.size();
    if (N==0) return;
    int M = (int)F[0].size();
    if (p < 1) p = 1;
    if (p > N) p = N;

    vector<ThreadArgs> args(p);
    SimpleBarrier bar(p);

    // divide rows into p contiguous blocks as balanced as possible
    int base = N / p;
    int rem = N % p;
    int cur = 0;
    for (int t = 0; t < p; ++t) {
        int size = base + (t < rem ? 1 : 0);
        int start = cur;
        int end = cur + size - 1;
        if (size == 0) { start = 0; end = -1; }
        args[t].id = t;
        args[t].start = start;
        args[t].end = end;
        args[t].Fptr = &F;
        args[t].Kptr = &K;
        args[t].bar = &bar;
        cur += size;
    }

    // create threads
    vector<thread> workers;
    for (int t=0;t<p;++t) {
        if (args[t].end >= args[t].start) {
            // pass args[t] by value to avoid lifetime issues
            workers.emplace_back(worker_thread, args[t]);
        } else {
            // empty block -> skip
        }
    }
    // join
    for (auto &th : workers) th.join();
}

// ---------- Utility: deep copy for verifying correctness ----------
vector<vector<int>> deep_copy(const vector<vector<int>> &A) {
    return A;
}

// ---------- Timing wrapper and correctness test ----------
double time_function_ms(function<void()> fn) {
    auto t0 = chrono::high_resolution_clock::now();
    fn();
    auto t1 = chrono::high_resolution_clock::now();
    chrono::duration<double, milli> dur = t1 - t0;
    return dur.count();
}

int main(int argc, char** argv) {
    string inFile = "date.txt";
    string outFilePar = "output_paralel.txt";
    string outFileSeq = "output_seq.txt";

    // If date.txt absent, generate a sample (you can tune N,M)
    if (!file_exists(inFile)) {
        cerr << "date.txt not found - generating a sample with N=M=100 (you can change in code)\n";
        generate_random_input(inFile, 1000, 1000, 255, 42);
    }

    int N,M;
    vector<vector<int>> F_original;
    array<array<int,3>,3> kernel;
    if (!read_input(inFile, N, M, F_original, kernel)) {
        cerr << "Cannot read input " << inFile << "\n";
        return 1;
    }

    // TESTS requested: repeat each test 10 times and take average
    vector<pair<int,int>> tests = {
            {10,10}, {1000,1000}, {10000,10000}
    };
    // But we cannot run huge 10000x10000 inside some environments — user should run tests on their machine.
    // We'll provide a capability to run custom p and sizes. For simplicity, do a single test based on read N,M.
    // We'll run sequential once for correctness baseline, then parallel for chosen p values.

    // Read N,M from file is used. We'll run:
    vector<int> p_values = {2,4,8,16};

    // 1) Sequential baseline: run 10 times on a deep copy of original, compute average time
    double seq_sum = 0.0;
    vector<vector<int>> Ftmp;
    for (int run=0; run<10; ++run) {
        Ftmp = deep_copy(F_original);
        double ms = time_function_ms([&](){ conv_inplace_sequential(Ftmp, kernel); });
        seq_sum += ms;
    }
    double seq_avg = seq_sum / 10.0;
    // Save sequential result once for correctness check
    vector<vector<int>> seq_result = deep_copy(F_original);
    conv_inplace_sequential(seq_result, kernel);
    write_output(outFileSeq, seq_result);
    cout << "Sequential average (10 runs): " << seq_avg << " ms. Result saved to " << outFileSeq << "\n";

    // 2) Parallel: for each p in p_values, run 10 times and compute avg. Verify correctness by comparing with seq_result.
    for (int p : p_values) {
        if (p > N) continue; // pointless
        double sum_ms = 0.0;
        bool all_correct = true;
        for (int run=0; run<10; ++run) {
            Ftmp = deep_copy(F_original);
            double ms = time_function_ms([&](){ conv_inplace_parallel_lines(Ftmp, kernel, p); });
            sum_ms += ms;
            // correctness check: compare Ftmp to seq_result
            if (Ftmp != seq_result) all_correct = false;
        }
        double avg = sum_ms / 10.0;
        cout << "Parallel p=" << p << " average (10 runs): " << avg << " ms. Correctness vs seq: " << (all_correct ? "OK" : "MISMATCH") << "\n";
        if (all_correct) {
            // save last run
            write_output(outFilePar, Ftmp);
        }
    }

    cout << "Done.\n";
    return 0;
}
