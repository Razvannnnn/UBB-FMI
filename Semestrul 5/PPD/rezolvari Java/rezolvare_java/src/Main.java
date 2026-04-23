import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.Scanner;


public class Main {
    private static final int N = 10;
    private static final int M = 10;
    private static final int k = 3;
    private static final int P = 16;

    private static int[] F = new int[N*M];
    private static int[] C = new int[k*k];
    private static int[] V_secv = new int[N*M];
    private static int[] V_par_linii = new int[N*M];
    private static int[] V_par_col = new int[N*M];

    private static void read_from_file(String filename, int[] F, int[] C) {
        try (Scanner sc = new Scanner(new BufferedInputStream(new FileInputStream(filename)))) {
            // citire matrice F
            for (int i = 0; i < N; i++) {
                for (int j = 0; j < M; j++) {
                    if (!sc.hasNextInt()) throw new IOException("Format fisier incorect (F)");
                    F[i * M + j] = sc.nextInt();
                }
            }

            // citire matrice C
            for (int i = 0; i < k; i++) {
                for (int j = 0; j < k; j++) {
                    if (!sc.hasNextInt()) throw new IOException("Format fisier incorect (C)");
                    C[i * k + j] = sc.nextInt();
                }
            }
        } catch (IOException e) {
            System.err.println("Eroare la deschiderea/citirea fisierului: " + e.getMessage());
            System.exit(1);
        }
    }

    public static void main(String[] args) {
        int iteratie = 0;
        if(args.length > 0) iteratie = Integer.parseInt(args[0]);

        String filename = "C:\\Users\\razva\\Desktop\\PPD\\matrici\\matrici\\N"
                + N + "M" + M + "k" + k + ".txt";

        read_from_file(filename, F, C);

        String output_folder = "C:\\Users\\razva\\Desktop\\PPD\\rezolvari Java\\rezolvare_java\\outputs_lab\\rez_"
                + "N" + N + "M" + M + "k" + k + "\\";


        // --- SECVENTIAL ----
        String output_filename_secv = output_folder + "output_secventialN"
                + N + "M" + M + "k" + k
                + "_iteratia" + iteratie + ".txt";
        double delta_secv = ConvolutieSecventiala.executa(F, C, V_secv, N, M, k, output_filename_secv);
        //System.out.println(delta_secv);


        // --- PARALEL LINII ---
        String output_filename_par_linii = output_folder + "output_paralel_linii_" + P + "threads_N"
                + N + "M" + M + "k" + k
                + "_iteratia" + iteratie + ".txt";
        double delta_par_linii = ConvolutieParalelaLinii.executa(F, C, V_par_linii, N, M, k, P, output_filename_par_linii);
        System.out.println(delta_par_linii);


        // --- PARALEL COLOANE ---
        String output_filename_par_coloane = output_folder + "output_paralel_coloane_" + P + "threads_N"
                + N + "M" + M + "k" + k
                + "_iteratia" + iteratie + ".txt";
        double delta_par_coloane = ConvolutieParalelaColoane.executa(F, C, V_par_col, N, M, k, P, output_filename_par_coloane);
        System.out.println(delta_par_coloane);

        // --- VERIFICARE REZULTATE ---
//        if(Arrays.equals(V_secv,V_par_linii) && Arrays.equals(V_secv,V_par_col)){
//            System.out.println("Equal");
//        }
//        else{
//            System.out.println("Not Equal");
//        }
        try{
            VerificareFisiere.comparaToateFisierele(output_folder);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}