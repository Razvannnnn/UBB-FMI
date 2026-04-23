package ppd;

import java.io.*;
import java.nio.file.*;
import java.util.*;
import java.util.concurrent.*;

public class ConvolutionMatrix {

    // ===================== Alocare Dinamica =====================

    static int[][] allocMatrix(int N, int M) {
        return new int[N][M];
    }

    // ===================== Citire / Scriere =====================

    static void generateMatrixFile(String filename, int N, int M) throws IOException {
        Path dir = Paths.get("data/inputs/");
        Files.createDirectories(dir);
        Random rand = new Random();
        try (PrintWriter fout = new PrintWriter(Files.newBufferedWriter(dir.resolve(filename + ".txt")))) {
            fout.println(N + " " + M);
            for (int i = 0; i < N; ++i) {
                for (int j = 0; j < M; ++j)
                    fout.print(rand.nextInt(10) + " ");
                fout.println();
            }
        }
    }

    static int[][] readMatrix(String filename, int[] dims) throws IOException {
        Path path = Paths.get("data/inputs/" + filename + ".txt");
        try (Scanner sc = new Scanner(path)) {
            int N = sc.nextInt();
            int M = sc.nextInt();
            dims[0] = N;
            dims[1] = M;
            int[][] F = allocMatrix(N, M);
            for (int i = 0; i < N; ++i)
                for (int j = 0; j < M; ++j)
                    F[i][j] = sc.nextInt();
            return F;
        }
    }

    static void writeMatrix(String filename, int[][] F, int N, int M) throws IOException {
        Path dir = Paths.get("data/outputs/");
        Files.createDirectories(dir);
        try (PrintWriter fout = new PrintWriter(Files.newBufferedWriter(dir.resolve(filename + ".txt")))) {
            fout.println(N + " " + M);
            for (int i = 0; i < N; ++i) {
                for (int j = 0; j < M; ++j)
                    fout.print(F[i][j] + " ");
                fout.println();
            }
        }
    }

    // ===================== Functie Auxiliara =====================

    static int clamp(int val, int min, int max) {
        return Math.max(min, Math.min(max, val));
    }

    // ===================== Convolutie Secventiala =====================

    static void convolutionSequentialInPlace(int[][] F, int N, int M) {
        int[][] kernel = {{0,1,0}, {1,1,1}, {0,1,0}};
        int[] prev = new int[M];
        int[] curr = new int[M];
        int[] next = new int[M];

        for (int j = 0; j < M; ++j) prev[j] = F[0][j];
        for (int j = 0; j < M; ++j) curr[j] = F[Math.min(1, N - 1)][j];

        for (int i = 0; i < N; ++i) {
            for (int j = 0; j < M; ++j)
                next[j] = F[Math.min(i + 1, N - 1)][j];

            for (int j = 0; j < M; ++j) {
                int sum = 0;
                for (int di = -1; di <= 1; ++di) {
                    int[] row = (di == -1 ? prev : di == 0 ? curr : next);
                    for (int dj = -1; dj <= 1; ++dj) {
                        int jj = clamp(j + dj, 0, M - 1);
                        sum += kernel[di + 1][dj + 1] * row[jj];
                    }
                }
                F[i][j] = sum;
            }

            prev = curr.clone();
            curr = next.clone();
        }
    }

    // ===================== Convolutie Paralela =====================

    static void convolutionParallelLinesInPlace(int[][] F, int N, int M, int P) throws InterruptedException {
        int[][] kernel = {{0,1,0}, {1,1,1}, {0,1,0}};
        int rowsPerThread = (N + P - 1) / P;
        ExecutorService executor = Executors.newFixedThreadPool(P);

        for (int t = 0; t < P; ++t) {
            int start = t * rowsPerThread;
            int end = Math.min(start + rowsPerThread, N);
            if (start >= end) break;
            executor.execute(() -> {
                int[] prev = new int[M];
                int[] curr = new int[M];
                int[] next = new int[M];
                for (int i = start; i < end; ++i) {
                    for (int j = 0; j < M; ++j) {
                        prev[j] = F[Math.max(i - 1, 0)][j];
                        curr[j] = F[i][j];
                        next[j] = F[Math.min(i + 1, N - 1)][j];
                    }
                    for (int j = 0; j < M; ++j) {
                        int sum = 0;
                        for (int di = -1; di <= 1; ++di) {
                            int[] row = (di == -1 ? prev : di == 0 ? curr : next);
                            for (int dj = -1; dj <= 1; ++dj) {
                                int jj = clamp(j + dj, 0, M - 1);
                                sum += kernel[di + 1][dj + 1] * row[jj];
                            }
                        }
                        F[i][j] = sum;
                    }
                }
            });
        }

        executor.shutdown();
        executor.awaitTermination(1, TimeUnit.HOURS);
    }

    // ===================== MAIN =====================

    public static void main(String[] args) throws Exception {
        if (args.length < 2) {
            System.err.println("Usage: java ppd.ConvolutionMatrix <size> <seq|par> [num_threads]");
            return;
        }

        int N = Integer.parseInt(args[0]);
        int M = N; // păstrează matrice pătrată
        String type = args[1];
        int P = 1; // default 1 thread pentru secvențial

        if (!type.equals("seq") && !type.equals("par")) {
            System.err.println("Tip invalid: " + type + ". Folositi 'seq' sau 'par'.");
            return;
        }

        if (type.equals("par")) {
            if (args.length < 3) {
                System.err.println("Trebuie sa specifici numarul de fire pentru paralel.");
                return;
            }
            P = Integer.parseInt(args[2]);
        }

        // generează matrice
        String name = "mat_" + N;
        generateMatrixFile(name, N, M);

        int[] dims = new int[2];
        int[][] F = readMatrix(name, dims);

        long start = System.nanoTime();

        if (type.equals("seq")) {
            convolutionSequentialInPlace(F, dims[0], dims[1]);
            writeMatrix(name + "_seq", F, dims[0], dims[1]);
        } else {
            convolutionParallelLinesInPlace(F, dims[0], dims[1], P);
            writeMatrix(name + "_par_p" + P, F, dims[0], dims[1]);
        }

        long end = System.nanoTime();
        long elapsed_ns = end - start;

        // **AFISEAZA DOAR TIMPUL IN NS**, gata pentru scripturile Bash
        System.out.println(elapsed_ns);
    }
}
