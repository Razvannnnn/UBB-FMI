package com.example.anar.repository;

import com.example.anar.domain.Rau;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class RauRepository {

    private String url = "jdbc:postgresql://localhost:5432/ANAR";
    private String username = "postgres";
    private String password = "postgres";
    private List<Rau> rauri = new ArrayList<>();

    public RauRepository() {
        rauri.addAll(findAll());
    }

    public List<Rau> getAll() {
        return rauri;
    }

    public List<Rau> findAll() {
        List<Rau> rauri = new ArrayList<>();
        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement statement = connection.prepareStatement("SELECT * from rauri");
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                Integer id = resultSet.getInt("id");
                String name = resultSet.getString("nume");
                Integer cota = resultSet.getInt("cota");

                Rau rau = new Rau(id, name, cota);
                rauri.add(rau);
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return rauri;
    }

    public Rau findById(int id) {
        return rauri
                .stream()
                .filter(rau -> rau.getId() == id)
                .findFirst()
                .orElseThrow();
    }

    public void update(Rau rau) {
        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement statement = connection.prepareStatement("UPDATE rauri SET cota = ? WHERE id = ?")) {
            statement.setInt(1, rau.getCota());
            statement.setInt(2, rau.getId());
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
