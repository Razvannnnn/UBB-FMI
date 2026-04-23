import java.io.*;
import java.sql.*;
import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

public class Main {
    private static final String DB_URL = "jdbc:sqlite:projects.db";

    private static final String RESULT_SEQ = "rezultate_secvential.txt";
    private static final String RESULT_PAR = "rezultate_paralel.txt";

    private static final String CHEATERS_SEQ = "studenti_copiatori_seq.txt";
    private static final String CHEATERS_PAR = "studenti_copiatori_par.txt";

    public static void main(String[] args) throws InterruptedException, IOException {
        try { Class.forName("org.sqlite.JDBC"); } catch (ClassNotFoundException e) { e.printStackTrace(); }

        List<String> tables = new ArrayList<>();
        for (int i = 1; i <= 10; i++) tables.add("project" + i);

        // Secventiala
        long startSeq = System.nanoTime();
        runSequentialDB(tables, RESULT_SEQ, CHEATERS_SEQ);
        long endSeq = System.nanoTime();
        double timeSeq = (endSeq - startSeq) / 1_000_000.0;
        System.out.println("Timp Secvential: " + timeSeq + " ms");

        // Rulare Paralela
        int p_r = 4; // Readers
        int p_w = 8; // Workers
        long startPar = System.nanoTime();
        runParallelDB(tables, p_r, p_w, RESULT_PAR, CHEATERS_PAR);
        long endPar = System.nanoTime();
        double timePar = (endPar - startPar) / 1_000_000.0;
        System.out.println("Timp Paralel:    " + timePar + " ms");

        // Validare
        System.out.println("\n--- Verificare ---");

        boolean resultsOk = verifyFiles(RESULT_SEQ, RESULT_PAR);
        boolean cheatersOk = verifyFiles(CHEATERS_SEQ, CHEATERS_PAR);

        if (resultsOk && cheatersOk) {
            System.out.println("\n>> REZULTATELE SUNT EGALE! <<");
        } else {
            System.out.println("\n>> REZULTATELE SUNT DIFERITE! <<");
        }
    }

    private static void runSequentialDB(List<String> tables, String resultFile, String cheatersFile) throws IOException {
        Map<Integer, Integer> students = new HashMap<>();
        Set<Integer> cheaters = new HashSet<>();

        try (Connection conn = DriverManager.getConnection(DB_URL)) {
            for (String table : tables) {
                try (Statement stmt = conn.createStatement();
                     ResultSet rs = stmt.executeQuery("SELECT stud_id, nota FROM " + table)) {
                    while (rs.next()) {
                        int id = rs.getInt("stud_id");
                        int grade = rs.getInt("nota");

                        if (grade == -1) {
                            cheaters.add(id);
                        } else {
                            students.put(id, students.getOrDefault(id, 0) + grade);
                        }
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        // Elimin studentii care au copiat din map-ul de rezultate
        for (Integer cheaterId : cheaters) {
            students.remove(cheaterId);
        }

        List<Map.Entry<Integer, Integer>> sortedList = new ArrayList<>(students.entrySet());
        sortedList.sort((a, b) -> {
            int cmp = b.getValue().compareTo(a.getValue());
            if (cmp == 0) return a.getKey().compareTo(b.getKey());
            return cmp;
        });

        try (BufferedWriter bw = new BufferedWriter(new FileWriter(resultFile))) {
            for (Map.Entry<Integer, Integer> entry : sortedList) {
                bw.write(entry.getKey() + "," + entry.getValue());
                bw.newLine();
            }
        }


        List<Integer> sortedCheaters = new ArrayList<>(cheaters);
        Collections.sort(sortedCheaters);
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(cheatersFile))) {
            for (Integer id : sortedCheaters) {
                bw.write(String.valueOf(id));
                bw.newLine();
            }
        }
    }


    private static void runParallelDB(List<String> tables, int numReaders, int numWorkers,
                                      String resultFile, String cheatersFile) throws InterruptedException {
        FineGrainedList idList = new FineGrainedList();
        TaskQueue queue = new TaskQueue();

        ExecutorService readerPool = Executors.newFixedThreadPool(numReaders);
        for (String table : tables) {
            readerPool.submit(() -> {
                try (Connection conn = DriverManager.getConnection(DB_URL);
                     Statement stmt = conn.createStatement();
                     ResultSet rs = stmt.executeQuery("SELECT stud_id, nota FROM " + table)) {
                    while (rs.next()) {
                        queue.push(rs.getInt("stud_id"), rs.getInt("nota"));
                    }
                } catch (SQLException e) {
                    System.out.println("Eroare SQL: " + e.getMessage());
                }
            });
        }

        Thread[] workers = new Thread[numWorkers];
        for (int i = 0; i < numWorkers; i++) {
            workers[i] = new Thread(() -> {
                while (true) {
                    Node n = queue.pop();
                    if (n == null) break;
                    idList.addOrUpdate(n.id, n.grade);
                }
            });
            workers[i].start();
        }

        readerPool.shutdown();
        readerPool.awaitTermination(2, TimeUnit.MINUTES);

        for (int i = 0; i < numWorkers; i++) queue.push(-1, -1);
        for (Thread w : workers) w.join();

        SortedList sortedList = new SortedList();
        List<Integer> finalCheatersList = Collections.synchronizedList(new ArrayList<>());

        List<Node> nodesToProcess = new ArrayList<>();
        Node curr = idList.head.next;
        while (curr != idList.tail) {
            nodesToProcess.add(curr);
            curr = curr.next;
        }

        AtomicInteger sharedIndex = new AtomicInteger(0);
        Thread[] sortWorkers = new Thread[numWorkers];
        for (int i = 0; i < numWorkers; i++) {
            sortWorkers[i] = new Thread(() -> {
                while (true) {
                    int idx = sharedIndex.getAndIncrement();
                    if (idx >= nodesToProcess.size()) break;

                    Node sourceNode = nodesToProcess.get(idx);

                    if (sourceNode.isCheater) {
                        finalCheatersList.add(sourceNode.id);
                    } else {
                        sortedList.insert(sourceNode.id, sourceNode.grade);
                    }
                }
            });
            sortWorkers[i].start();
        }
        for (Thread w : sortWorkers) w.join();

        sortedList.writeToFile(resultFile);
        writeCheatersFile(finalCheatersList, cheatersFile);
    }

    private static void writeCheatersFile(List<Integer> cheaters, String filename) {
        Collections.sort(cheaters);
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(filename))) {
            for (Integer id : cheaters) {
                bw.write(String.valueOf(id));
                bw.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static boolean verifyFiles(String file1, String file2) {
        try (BufferedReader br1 = new BufferedReader(new FileReader(file1));
             BufferedReader br2 = new BufferedReader(new FileReader(file2))) {

            String line1, line2;
            int lineNum = 1;

            while (true) {
                line1 = br1.readLine();
                line2 = br2.readLine();

                if ((line1 == null && line2 != null) || (line1 != null && line2 == null)) {
                    System.out.println("   [Eroare] Lungimi diferite la fisiere!");
                    return false;
                }

                if (line1 == null && line2 == null) {
                    return true;
                }

                if (!line1.trim().equals(line2.trim())) {
                    System.out.println("   [Eroare] Diferenta la linia " + lineNum + ":");
                    System.out.println("     Seq: " + line1);
                    System.out.println("     Par: " + line2);
                    return false;
                }

                lineNum++;
            }

        } catch (IOException e) {
            System.out.println("   [Eroare] Nu s-au putut deschide fisierele: " + e.getMessage());
            return false;
        }
    }
}