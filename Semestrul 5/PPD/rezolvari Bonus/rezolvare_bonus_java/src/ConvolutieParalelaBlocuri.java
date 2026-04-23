import java.io.*;

public class ConvolutieParalelaBlocuri {

    private static int getF(int[] F, int i, int j, int N, int M) {
        if (i < 0) i = 0;
        if (j < 0) j = 0;
        if (i >= N) i = N - 1;
        if (j >= M) j = M - 1;
        return F[i * M + j];
    }

    public static double executa(int[] F, int[] C, int[] V,
                                 int N, int M, int k, int P, String outputFile) {
        if (P <= 0) P = 1;

        // Diviziunea optima in blocuri (r pe linii, c pe coloane)
        int r = (int) Math.floor(Math.sqrt(P));
        while (r > 0 && P % r != 0) r--;
        if (r == 0) r = 1;
        int c = (P + r - 1) / r;

        int rowsPerBlock = N / r;
        int extraRows = N % r;
        int colsPerBlock = M / c;
        int extraCols = M % c;

        Thread[] threads = new Thread[P];
        long start = System.nanoTime();

        int threadIndex = 0;
        int rowStart = 0;
        for (int bi = 0; bi < r; bi++) {
            int thisBlockRows = rowsPerBlock + (bi < extraRows ? 1 : 0);
            int rowEnd = rowStart + thisBlockRows;
            int colStart = 0;
            for (int bj = 0; bj < c && threadIndex < P; bj++) {
                int thisBlockCols = colsPerBlock + (bj < extraCols ? 1 : 0);
                int colEnd = colStart + thisBlockCols;

                threads[threadIndex] = new MyThreadBloc(F, C, V, N, M, k,
                        rowStart, rowEnd, colStart, colEnd);
                threads[threadIndex].start();

                threadIndex++;
                colStart = colEnd;
            }
            rowStart = rowEnd;
        }

        // daca avem mai putine blocuri decat P
        for (int t = threadIndex; t < P; t++) {
            threads[t] = new Thread(() -> {});
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
