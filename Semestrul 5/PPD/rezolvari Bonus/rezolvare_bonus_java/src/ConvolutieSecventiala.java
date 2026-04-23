import java.io.*;

public class ConvolutieSecventiala {

    // Bordare virtuala
    private static int getF(int[] F, int i, int j, int N, int M) {
        if (i < 0) i = 0;
        if (j < 0) j = 0;
        if (i >= N) i = N - 1;
        if (j >= M) j = M - 1;
        return F[i * M + j];
    }

    public static double executa(int[] F, int[] C, int[] V, int N, int M, int k, String outputFile) {
        int offset = k / 2;
        long start = System.nanoTime();

        for (int i = 0; i < N; i++) {
            for (int j = 0; j < M; j++) {
                int sum = 0;
                for (int u = 0; u < k; u++) {
                    for (int v = 0; v < k; v++) {
                        int fi = i + u - offset;
                        int fj = j + v - offset;
                        sum += getF(F, fi, fj, N, M) * C[u * k + v];
                    }
                }
                V[i * M + j] = sum;
            }
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
