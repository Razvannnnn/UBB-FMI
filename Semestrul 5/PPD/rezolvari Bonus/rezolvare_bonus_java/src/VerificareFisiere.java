import java.io.*;
import java.util.*;

public class VerificareFisiere {

    public static List<Integer> citesteFisier(String path) throws IOException {
        List<Integer> data = new ArrayList<>();
        try (Scanner sc = new Scanner(new File(path))) {
            while (sc.hasNextInt()) {
                data.add(sc.nextInt());
            }
        }
        return data;
    }

    public static boolean comparaFisiere(String f1, String f2) throws IOException {
        List<Integer> A = citesteFisier(f1);
        List<Integer> B = citesteFisier(f2);

        if (A.size() != B.size()) return false;

        for (int i = 0; i < A.size(); i++) {
            if (!A.get(i).equals(B.get(i))) {
                return false;
            }
        }

        return true;
    }

    public static List<File> listaFisiereTXT(String folderPath) {
        File folder = new File(folderPath);
        File[] files = folder.listFiles((dir, name) -> name.toLowerCase().endsWith(".txt"));
        if (files == null) return new ArrayList<>();
        return Arrays.asList(files);
    }

    public static void comparaToateFisierele(String folderPath) throws IOException {
        List<File> fisiere = listaFisiereTXT(folderPath);

        if (fisiere.isEmpty()) return;

        File fisierParinte = fisiere.get(0);

        for (int i = 1; i < fisiere.size(); i++) {
            boolean identic = comparaFisiere(fisierParinte.getAbsolutePath(), fisiere.get(i).getAbsolutePath());
            if (!identic) {
                System.out.println("FISIERLE NU SUNT EGALE! (" + fisiere.get(i).getName() + ")");
                return;
            }
        }
        System.out.println("TOATE FISIERELE SUNT EGALE!");
    }

}
