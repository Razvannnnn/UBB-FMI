package org.example.comenzi2.Repository;

import org.example.comenzi2.Domain.MenuItem;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MenuItemRepo implements Repository<MenuItem> {
    private String url;
    private String user;
    private String password;

    public MenuItemRepo(String url, String user, String password) {
        this.url = url;
        this.user = user;
        this.password = password;
    }

    @Override
    public Iterable<MenuItem> getAll() {
        List<MenuItem> menuItems = new ArrayList<>();
        try (Connection connection = DriverManager.getConnection(url, user, password);
             PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM meniu");
             ResultSet resultSet = preparedStatement.executeQuery()) {
            while (resultSet.next()) {
                MenuItem menuItem = new MenuItem(resultSet.getInt("id"),
                        resultSet.getString("category"),
                        resultSet.getString("item"),
                        resultSet.getFloat("price"),
                        resultSet.getString("currency"));
                menuItems.add(menuItem);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return menuItems;
    }

    public Iterable<String> categorii() {
        List<String> categorii = new ArrayList<>();
        try (Connection connection = DriverManager.getConnection(url, user, password);
             PreparedStatement preparedStatement = connection.prepareStatement("SELECT DISTINCT category FROM meniu");
             ResultSet resultSet = preparedStatement.executeQuery()) {
            while (resultSet.next()) {
                categorii.add(resultSet.getString("category"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return categorii;
    }

    public Iterable<MenuItem> menuItemDupaCategorie(String categorie) {
        List<MenuItem> menuItems = new ArrayList<>();
        try (Connection connection = DriverManager.getConnection(url, user, password);
             PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM meniu WHERE category = ?");
        ) {
            preparedStatement.setString(1, categorie);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                MenuItem menuItem = new MenuItem(resultSet.getInt("id"),
                        resultSet.getString("category"),
                        resultSet.getString("item"),
                        resultSet.getFloat("price"),
                        resultSet.getString("currency"));
                menuItems.add(menuItem);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return menuItems;
    }

    public List<MenuItem> menuItemsDupaId(List<Integer> ids) {
        List<MenuItem> menuItems = new ArrayList<>();
        try (Connection connection = DriverManager.getConnection(url, user, password);
             PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM meniu WHERE id = ?");
        ) {
            for (int id : ids) {
                preparedStatement.setInt(1, id);
                ResultSet resultSet = preparedStatement.executeQuery();
                while (resultSet.next()) {
                    MenuItem menuItem = new MenuItem(resultSet.getInt("id"),
                            resultSet.getString("category"),
                            resultSet.getString("item"),
                            resultSet.getFloat("price"),
                            resultSet.getString("currency"));
                    menuItems.add(menuItem);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return menuItems;
    }
}
