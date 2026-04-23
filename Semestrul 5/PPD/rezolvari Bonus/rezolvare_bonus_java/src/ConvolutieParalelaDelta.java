import java.io.*;
import java.util.*;

public class ConvolutieParalelaDelta {

    public enum DeltaType { LINEAR, CYCLIC }

    private static int getF(int[] F, int i, int j, int N, int M) {
        if (i < 0) i = 0;
        if (j < 0) j = 0;
        if (i >= N) i = N - 1;
        if (j >= M) j = M - 1;
        return F[i * M + j];
    }

    public static double executa(int[] F, int[] C, int[] V,
                                 int N, int M, int k, int P,
                                 DeltaType type, String outputFile) {
        if (P <= 0) P = 1;
        List<List<int[]>> assignments = new ArrayList<>(P);
        for (int i = 0; i < P; i++) assignments.add(new ArrayList<>());

        int total = N * M;

        if (type == DeltaType.LINEAR) {
            int chunk = total / P;
            int extra = total % P;
            int idx = 0;
            for (int t = 0; t < P; t++) {
                int size = chunk + (t < extra ? 1 : 0);
                for (int cnt = 0; cnt < size; cnt++) {
                    int i = idx / M;
                    int j = idx % M;
                    assignments.get(t).add(new int[]{i, j});
                    idx++;
                }
            }
        } else { // CYCLIC
            for (int idx = 0; idx < total; idx++) {
                int t = idx % P;
                int i = idx / M;
                int j = idx % M;
                assignments.get(t).add(new int[]{i, j});
            }
        }

        Thread[] threads = new Thread[P];
        long start = System.nanoTime();

        for (int t = 0; t < P; t++) {
            List<int[]> coords = assignments.get(t);
            threads[t] = new MyThreadDelta(F, C, V, N, M, k, coords);
            threads[t].start();
        }

        try {
            for (Thread th : threads) th.join();
        } catch (InterruptedException e) {
            System.err.println("Thread intrerupt: " + e.getMessage());
        }

        long end = System.nanoTime();
        double ms = (end - start) / 1e6;

        try (BufferedWriter bw = new BufferedWriter(new FileWriter(outputFile))) {
            for (int i = 0; i < N; i++) {
                for (int j = 0; j < M; j++) {
                    bw.write(V[i * M + j] + " ");
                }
                bw.newLine();
            }
        } catch (IOException e) {
            System.err.println("Eroare la scrierea fisierului " + outputFile + ": " + e.getMessage());
        }

        return ms;
    }
}
