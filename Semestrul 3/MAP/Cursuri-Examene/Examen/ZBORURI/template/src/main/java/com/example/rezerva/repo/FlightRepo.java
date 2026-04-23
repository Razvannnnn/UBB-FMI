package com.example.rezerva.repo;

import com.example.rezerva.domain.Client;
import com.example.rezerva.domain.Flight;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class FlightRepo implements Repository<Flight>{
    private String url;
    private String username;
    private String passwd;

    public FlightRepo(String url, String username, String passwd) {
        this.url = url;
        this.username = username;
        this.passwd = passwd;
    }

    @Override
    public List<Flight> getAll() {
        List<Flight> all = new ArrayList<>();
        try (Connection connection = DriverManager.getConnection(url, username, passwd);
             PreparedStatement statement = connection.prepareStatement("SELECT * from \"flight\"");
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                Long id=resultSet.getLong("id");
                String from = resultSet.getString("from");
                String to = resultSet.getString("to");
                LocalDateTime departure=resultSet.getTimestamp("departure").toLocalDateTime();
                LocalDateTime landing=resultSet.getTimestamp("landing").toLocalDateTime();
                Integer seats=resultSet.getInt("seats");

                Flight fl=new Flight(id,from,to,departure,landing,seats);
                all.add(fl);
            }
            return all;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return all;
    }
}
