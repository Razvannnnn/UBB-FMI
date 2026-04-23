package com.example.vacanta_var_2.repo;

import com.example.vacanta_var_2.domain.Client;
import com.example.vacanta_var_2.domain.Hobbies;
import com.example.vacanta_var_2.domain.Hotel;
import com.example.vacanta_var_2.domain.HotelType;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ClientRepo implements Repo<Client>{
    private String url;
    private String username;
    private String passwd;

    public ClientRepo(String url, String username, String passwd) {
        this.url = url;
        this.username = username;
        this.passwd = passwd;
    }

    @Override
    public List<Client> getAll() {
        List<Client> all = new ArrayList<>();
        try (Connection connection = DriverManager.getConnection(url, username, passwd);
             PreparedStatement statement = connection.prepareStatement("SELECT * from \"client\"");
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                Long id = resultSet.getLong("id");
                String name = resultSet.getString("name");
                Integer fidelitygrade = resultSet.getInt("fidelitygrade");
                Integer varsta = resultSet.getInt("varsta");
                Hobbies hobby = Hobbies.valueOf(resultSet.getString("hobby"));

                Client client=new Client(id,name,fidelitygrade,varsta,hobby);
                all.add(client);
            }
            return all;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return all;
    }
}
