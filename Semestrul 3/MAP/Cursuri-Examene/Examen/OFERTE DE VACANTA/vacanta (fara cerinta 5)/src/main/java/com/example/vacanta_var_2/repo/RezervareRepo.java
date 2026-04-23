package com.example.vacanta_var_2.repo;

import com.example.vacanta_var_2.domain.Client;
import com.example.vacanta_var_2.domain.Hobbies;
import com.example.vacanta_var_2.domain.Location;
import com.example.vacanta_var_2.domain.Rezervare;

import java.sql.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class RezervareRepo implements Repo<Rezervare>{
    private String url;
    private String username;
    private String passwd;

    public RezervareRepo(String url, String username, String passwd) {
        this.url = url;
        this.username = username;
        this.passwd = passwd;
    }

    @Override
    public List<Rezervare> getAll() {
        List<Rezervare> all = new ArrayList<>();
        try (Connection connection = DriverManager.getConnection(url, username, passwd);
             PreparedStatement statement = connection.prepareStatement("SELECT * from \"rezervare\"");
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                Double id = resultSet.getDouble("id");
                Long idClient = resultSet.getLong("clientid");
                Double idHotel = resultSet.getDouble("hotelid");
                LocalDate dataStart = resultSet.getDate("datastart").toLocalDate();
                Integer noNights = resultSet.getInt("nonights");

                Rezervare r=new Rezervare(id,idClient,idHotel,dataStart,noNights);
                all.add(r);
            }
            return all;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return all;
    }

    //@Override
    public void adauga(Rezervare rezervare) {

        String sql = "insert into \"rezervare\" (id,clientid,hotelid,datastart,nonights) values (?,?,?,?,?)";

        try (Connection connection = DriverManager.getConnection(url, username, passwd);
             PreparedStatement ps = connection.prepareStatement(sql)) {

            ps.setDouble(1, rezervare.getId());
            ps.setLong(2, rezervare.getIdClient());
            ps.setDouble(3, rezervare.getIdHotel());
            ps.setDate(4,Date.valueOf(rezervare.getDataStart()));
            ps.setInt(5, rezervare.getNoNights());

            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
