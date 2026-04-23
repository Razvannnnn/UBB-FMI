package org.example.zboruri.Repository;

import org.example.zboruri.Domain.Client;

import java.sql.*;
import java.util.ArrayList;

public class RepoClient implements Repository<Client> {
    private String url;
    private String user;
    private String password;

    public RepoClient(String url, String user, String password) {
        this.url = url;
        this.user = user;
        this.password = password;
    }

    @Override
    public Iterable<Client> getAll() {
        Iterable<Client> clients = new ArrayList<>();
        try (Connection connection = DriverManager.getConnection(url, user, password);
             PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM client");
             ResultSet resultSet = preparedStatement.executeQuery()) {
            while (resultSet.next()) {
                Long id = resultSet.getLong("id_client");
                String username = resultSet.getString("username");
                String name = resultSet.getString("name");
                Client client = new Client(id, username, name);
                ((ArrayList<Client>) clients).add(client);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return clients;
    }

    public boolean login(String username) {
        try (Connection connection = DriverManager.getConnection(url, user, password);
             PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM client WHERE username = ?");
        ) {
            preparedStatement.setString(1, username);
            ResultSet resultSet = preparedStatement.executeQuery();
            return resultSet.next();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public Client getClient(String username) {
        try (Connection connection = DriverManager.getConnection(url, user, password);
             PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM client WHERE username = ?");
        ) {
            preparedStatement.setString(1, username);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                Long id = resultSet.getLong("id_client");
                String name = resultSet.getString("name");
                return new Client(id, username, name);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}
