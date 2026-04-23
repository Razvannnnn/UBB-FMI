package com.example.rezerva.repo;

import com.example.rezerva.domain.City;
import com.example.rezerva.domain.TrainStation;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class TrainStationRepo implements Repository<TrainStation> {
    private String url;
    private String usernamee;
    private String passwd;

    public TrainStationRepo(String url, String usernamee, String passwd) {
        this.url = url;
        this.usernamee = usernamee;
        this.passwd = passwd;
    }

    @Override
    public List<TrainStation> getAll() {
        List<TrainStation> all = new ArrayList<>();
        try (Connection connection = DriverManager.getConnection(url, usernamee, passwd);
             PreparedStatement statement = connection.prepareStatement("SELECT * from \"trainstation\"");
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                String id = resultSet.getString("id");
                String departure = resultSet.getString("departure");
                String destination = resultSet.getString("destination");

                TrainStation ts=new TrainStation(id,departure,destination);
                all.add(ts);
            }
            return all;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return all;
    }
}
