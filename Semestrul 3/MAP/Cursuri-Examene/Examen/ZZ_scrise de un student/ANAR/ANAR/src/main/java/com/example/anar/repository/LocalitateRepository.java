package com.example.anar.repository;

import com.example.anar.domain.Localitate;
import com.example.anar.domain.Rau;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class LocalitateRepository {

    private String url = "jdbc:postgresql://localhost:5432/ANAR";
    private String username = "postgres";
    private String password = "postgres";
    private List<Localitate> localitati = new ArrayList<>();

    public LocalitateRepository() {
        localitati.addAll(findAll());
    }

    public List<Localitate> getAll() {
        return localitati;
    }

    public List<Localitate> findAll() {
        List<Localitate> localitati = new ArrayList<>();
        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement statement = connection.prepareStatement("SELECT * from localitate");
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                Integer id = resultSet.getInt("id");
                String name = resultSet.getString("nume");
                Integer rauId = resultSet.getInt("rau_id");
                Integer cmdr = resultSet.getInt("cmdr");
                Integer cma = resultSet.getInt("cma");

                Localitate localitate = new Localitate(id, name, rauId, cmdr, cma);
                localitati.add(localitate);
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return localitati;
    }

    public Localitate findByName(String name) {
        return localitati
                .stream()
                .filter(localitate -> localitate.getNume().equals(name))
                .findFirst()
                .orElseThrow();
    }

    public Localitate findById(int id) {
        return localitati
                .stream()
                .filter(localitate -> localitate.getId() == id)
                .findFirst()
                .orElseThrow();
    }
}
