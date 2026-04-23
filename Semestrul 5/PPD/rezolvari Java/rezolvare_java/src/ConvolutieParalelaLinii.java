import java.io.*;

public class ConvolutieParalelaLinii {

    public static double executa(int[] F, int[] C, int[] V, int N, int M, int k, int P, String outputFile) {
        Thread[] threads = new Thread[P];
        int linesPerThread = N / P;
        int remaining = N % P;
        int currentRow = 0;

        long start = System.nanoTime();

        for (int t = 0; t < P; t++) {
            int startRow = currentRow;
            int endRow = startRow + linesPerThread + (t < remaining ? 1 : 0);
            threads[t] = new MyThreadLinii(F, C, V, startRow, endRow, N, M, k);
            threads[t].start();
            currentRow = endRow;
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
