package com.example.vacanta_var_2.repo;

import com.example.vacanta_var_2.domain.Hotel;
import com.example.vacanta_var_2.domain.HotelType;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class HotelRepo implements Repo<Hotel>{
    private String url;
    private String username;
    private String passwd;

    public HotelRepo(String url, String username, String passwd) {
        this.url = url;
        this.username = username;
        this.passwd = passwd;
    }

    @Override
    public List<Hotel> getAll() {
        List<Hotel> all = new ArrayList<>();
        try (Connection connection = DriverManager.getConnection(url, username, passwd);
             PreparedStatement statement = connection.prepareStatement("SELECT * from \"hotel\"");
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                Double id = resultSet.getDouble("id");
                Double idl = resultSet.getDouble("idl");
                String hnume = resultSet.getString("hname");
                Integer rooms = resultSet.getInt("rooms");
                Integer price = resultSet.getInt("price");
//!!!!!!                HotelType htype = Hotel.stringToType(resultSet.getString("htype"));
                HotelType htype = HotelType.valueOf(resultSet.getString("htype"));

                //HotelType htype2 = HotelType.FAMILY;
                Hotel hotel=new Hotel(id,idl,hnume,rooms,price,htype);
                all.add(hotel);
            }
            return all;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return all;
    }
}
