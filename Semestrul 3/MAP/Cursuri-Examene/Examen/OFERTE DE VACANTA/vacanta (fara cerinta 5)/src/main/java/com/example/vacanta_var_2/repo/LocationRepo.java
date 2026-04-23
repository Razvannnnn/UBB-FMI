package com.example.vacanta_var_2.repo;

import com.example.vacanta_var_2.domain.Location;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class LocationRepo implements Repo<Location>{

    private String url;
    private String username;
    private String passwd;

    public LocationRepo(String url, String username, String passwd) {
        this.url = url;
        this.username = username;
        this.passwd = passwd;
    }

    @Override
    public List<Location> getAll() {
        List<Location> all = new ArrayList<>();
        try (Connection connection = DriverManager.getConnection(url, username, passwd);
             PreparedStatement statement = connection.prepareStatement("SELECT * from \"location\"");
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                Double id = resultSet.getDouble("id");
                String nume = resultSet.getString("name");

                Location location=new Location(id,nume);
                all.add(location);
            }
            return all;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return all;
    }
}
