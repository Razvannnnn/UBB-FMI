package com.example.rezerva.repo;

import com.example.rezerva.domain.MenuItem;
import com.example.rezerva.domain.Table;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MenuItemRepo implements Repository<MenuItem> {
    private String url;
    private String usernamee;
    private String passwd;

    public MenuItemRepo(String url, String usernamee, String passwd) {
        this.url = url;
        this.usernamee = usernamee;
        this.passwd = passwd;
    }

    @Override
    public List<MenuItem> getAll() {
        List<MenuItem> all = new ArrayList<>();
        try (Connection connection = DriverManager.getConnection(url, usernamee, passwd);
             PreparedStatement statement = connection.prepareStatement("SELECT * from menuitem");
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                Integer id = resultSet.getInt("id");
                String category = resultSet.getString("category");
                String item = resultSet.getString("item");
                Float price = resultSet.getFloat("price");
                String currency = resultSet.getString("currency");

                MenuItem mi=new MenuItem(id,category,item,price,currency);
                all.add(mi);
            }
            return all;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return all;
    }
}
