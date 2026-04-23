package org.example.comenzi2.Repository;

import org.example.comenzi2.Domain.Table;

import java.sql.*;
import java.util.ArrayList;
import java.util.Comparator;

public class TableRepo implements Repository<Table> {
    private String url;
    private String user;
    private String password;

    public TableRepo(String url, String user, String password) {
        this.url = url;
        this.user = user;
        this.password = password;
    }

    @Override
    public Iterable<Table> getAll() {
        Iterable<Table> tables = new ArrayList<>();
        try (Connection connection = DriverManager.getConnection(url, user, password);
             PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM masa");
             ResultSet resultSet = preparedStatement.executeQuery()) {
            while (resultSet.next()) {
                Table table = new Table(resultSet.getInt("id"));
                ((ArrayList<Table>) tables).add(table);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return tables;
    }
}
