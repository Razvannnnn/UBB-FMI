import java.sql.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class DatabaseGenerator {

    private static final String DB_URL = "jdbc:sqlite:projects.db";
    private static final int NUM_STUDENTS = 500;
    private static final int NUM_PROJECTS = 10;

    public static void main(String[] args) {
        System.out.println("Se initializeaza generarea bazei de date...");

        try {
            Class.forName("org.sqlite.JDBC");
        } catch (ClassNotFoundException e) {
            System.err.println("Eroare: Nu s-a gasit driverul SQLite. Asigura-te ca ai adaugat sqlite-jdbc.jar in proiect!");
            return;
        }

        try (Connection conn = DriverManager.getConnection(DB_URL)) {
            if (conn != null) {
                conn.setAutoCommit(false);

                for (int i = 1; i <= NUM_PROJECTS; i++) {
                    createAndPopulateTable(conn, i);
                }

                conn.commit();
                System.out.println("\nGenerare completa!");
            }
        } catch (SQLException e) {
            System.err.println("Eroare SQL: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private static void createAndPopulateTable(Connection conn, int projectIndex) throws SQLException {
        String tableName = "project" + projectIndex;
        Statement stmt = conn.createStatement();

        stmt.execute("DROP TABLE IF EXISTS " + tableName);

        String createSQL = "CREATE TABLE " + tableName + " (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "stud_id INTEGER, " +
                "nota INTEGER)";
        stmt.execute(createSQL);
        stmt.close();

        List<Integer> allStudentIds = new ArrayList<>();
        for (int id = 1; id <= NUM_STUDENTS; id++) {
            allStudentIds.add(id);
        }
        Collections.shuffle(allStudentIds);

        Random rand = new Random();
        int submissionsCount = 350 + rand.nextInt(NUM_STUDENTS - 350 + 1);

        String insertSQL = "INSERT INTO " + tableName + " (stud_id, nota) VALUES (?, ?)";

        try (PreparedStatement pstmt = conn.prepareStatement(insertSQL)) {
            for (int k = 0; k < submissionsCount; k++) {
                int studId = allStudentIds.get(k);


                int grade;
                if (rand.nextDouble() < 0.05) {
                    grade = -1;
                } else {
                    grade = rand.nextInt(101);
                }

                pstmt.setInt(1, studId);
                pstmt.setInt(2, grade);
                pstmt.addBatch();
            }
            pstmt.executeBatch();
        }

        System.out.println("Tabelul " + tableName + " generat: " + submissionsCount + " inregistrari.");
    }
}