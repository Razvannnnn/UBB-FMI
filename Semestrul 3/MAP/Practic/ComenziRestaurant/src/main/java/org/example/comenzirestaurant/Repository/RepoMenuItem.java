package org.example.comenzirestaurant.Repository;

import org.example.comenzirestaurant.Domain.MenuItem;

import java.sql.Connection;
import java.sql.DriverManager;
import java.util.ArrayList;
import java.util.List;

public class RepoMenuItem implements Repository<MenuItem> {
    private String url;
    private String username;
    private String password;

    public RepoMenuItem(String url, String username, String password) {
        this.url = url;
        this.username = username;
        this.password = password;
    }

    @Override
    public List<MenuItem> getAll() {
        List<MenuItem> menuItems = new ArrayList<>();
        try (Connection connection = DriverManager.getConnection(url, username, password);
             var statement = connection.prepareStatement("SELECT * from \"menu_items\"");
             var resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                String category = resultSet.getString("category");
                String item = resultSet.getString("item");
                float price = resultSet.getFloat("price");
                String currency = resultSet.getString("currency");

                MenuItem menuItem = new MenuItem(id, category, item, price, currency);
                menuItems.add(menuItem);
            }
            return menuItems;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return menuItems;
    }
}
