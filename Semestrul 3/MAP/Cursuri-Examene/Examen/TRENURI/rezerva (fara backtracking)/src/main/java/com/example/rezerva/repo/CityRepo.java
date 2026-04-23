package com.example.rezerva.repo;

import com.example.rezerva.domain.City;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CityRepo implements Repository<City>{
    private String url;
    private String usernamee;
    private String passwd;

    public CityRepo(String url, String usernamee, String passwd) {
        this.url = url;
        this.usernamee = usernamee;
        this.passwd = passwd;
    }

    @Override
    public List<City> getAll() {
        List<City> all = new ArrayList<>();
        try (Connection connection = DriverManager.getConnection(url, usernamee, passwd);
             PreparedStatement statement = connection.prepareStatement("SELECT * from \"cities\"");
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                String id = resultSet.getString("id");
                String nume = resultSet.getString("nume");

                City client=new City(id,nume);
                all.add(client);
            }
            return all;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return all;
    }
}
