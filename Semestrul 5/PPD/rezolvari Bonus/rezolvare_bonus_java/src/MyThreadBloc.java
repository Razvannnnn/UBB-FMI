public class MyThreadBloc extends Thread {
    private final int[] F, C, V;
    private final int N, M, k;
    private final int startRow, endRow, startCol, endCol;

    public MyThreadBloc(int[] F, int[] C, int[] V, int N, int M, int k,
                        int startRow, int endRow, int startCol, int endCol) {
        this.F = F;
        this.C = C;
        this.V = V;
        this.N = N;
        this.M = M;
        this.k = k;
        this.startRow = startRow;
        this.endRow = endRow;
        this.startCol = startCol;
        this.endCol = endCol;
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
            for (int j = startCol; j < endCol; j++) {
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
