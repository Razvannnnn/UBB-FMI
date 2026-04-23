package com.example.vacanta_var_2.repo;

import com.example.vacanta_var_2.domain.Hotel;
import com.example.vacanta_var_2.domain.HotelType;
import com.example.vacanta_var_2.domain.Offers;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class OffRepo implements Repo<Offers> {
    private String url;
    private String username;
    private String passwd;

    public OffRepo(String url, String username, String passwd) {
        this.url = url;
        this.username = username;
        this.passwd = passwd;
    }

    @Override
    public List<Offers> getAll() {
        List<Offers> all = new ArrayList<>();
        try (Connection connection = DriverManager.getConnection(url, username, passwd);
             PreparedStatement statement = connection.prepareStatement("SELECT * from \"offers\"");
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                Double id = resultSet.getDouble("id");
                Double hotel = resultSet.getDouble("hotel");
                LocalDate start = resultSet.getDate("start").toLocalDate();
                LocalDate end = resultSet.getDate("end").toLocalDate();
                Integer percent = resultSet.getInt("percent");

                Offers offer=new Offers(id,hotel,start,end,percent);
                all.add(offer);
            }
            return all;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return all;
    }
}
