import java.io.*;

public class ConvolutieParalelaColoane {

    public static double executa(int[] F, int[] C, int[] V, int N, int M, int k, int P, String outputFile) {
        Thread[] threads = new Thread[P];
        int colsPerThread = M / P;
        int remaining = M % P;
        int currentCol = 0;

        long start = System.nanoTime();

        for (int t = 0; t < P; t++) {
            int startCol = currentCol;
            int endCol = startCol + colsPerThread + (t < remaining ? 1 : 0);
            threads[t] = new MyThreadColoane(F, C, V, startCol, endCol, N, M, k);
            threads[t].start();
            currentCol = endCol;
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
