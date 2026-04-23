package com.example.ati.Repository;

import com.example.ati.Entities.Pacient;
import com.example.ati.Entities.Pat;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PaturiRepository implements GenericRepository<Pat>{
    private String url;
    private String username;
    private String password;
    private List<Pat> paturi;


    public PaturiRepository(String url, String username, String password)
    {
        this.url=url;
        this.username=username;
        this.password=password;
        paturi=new ArrayList<>();
        getData();
    }

    @Override
    public void getData()
    {
        paturi.clear();
        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement statement = connection.prepareStatement("SELECT * from paturi");
             ResultSet resultSet = statement.executeQuery())
        {
            while (resultSet.next())
            {
                Long id=resultSet.getLong("id");
                String tip=resultSet.getString("tip");
                String ventilatie=resultSet.getString("ventilatie");
                String pacient=resultSet.getString("pacient");

                paturi.add(new Pat(id,tip,ventilatie,pacient));
            }
        } catch (SQLException e)
        {
            e.printStackTrace();
        }
    }


    @Override
    public List<Pat> findAll()
    {
        getData();
        return paturi;
    }


    public Pat update(String cnp, Long idPat) {
        String sql="update \"paturi\" set pacient=? where id=?";
        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1,cnp);
            ps.setLong(2,idPat);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }


    public void eliberarePat(Long idPat) {
        String sql="update \"paturi\" set pacient=? where id=?";
        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1,null);
            ps.setLong(2,idPat);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}