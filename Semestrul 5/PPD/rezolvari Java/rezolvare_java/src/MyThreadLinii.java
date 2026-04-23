public class MyThreadLinii extends Thread {
    private final int[] F, C, V;
    private final int startRow, endRow;
    private final int N, M, k;

    public MyThreadLinii(int[] F, int[] C, int[] V, int startRow, int endRow, int N, int M, int k) {
        this.F = F;
        this.C = C;
        this.V = V;
        this.startRow = startRow;
        this.endRow = endRow;
        this.N = N;
        this.M = M;
        this.k = k;
    }

    private int getF(int i, int j) {
        if (i < 0) i = 0;
        if (j < 0) j = 0;
        if (i >= N) i = N - 1;
        if (j >= M) j = M - 1;
        return F[i * M + j];
    }

    @Override
    public void run() {
        int offset = k / 2;
        for (int i = startRow; i < endRow; i++) {
            for (int j = 0; j < M; j++) {
                int sum = 0;
                for (int u = 0; u < k; u++) {
                    for (int v = 0; v < k; v++) {
                        int fi = i + u - offset;
                        int fj = j + v - offset;
                        sum += getF(fi, fj) * C[u * k + v];
                    }
                }
                V[i * M + j] = sum;
            }
        }
    }
}
