package com.example.clinicaprivata.Repository;

import com.example.clinicaprivata.Entities.Medic;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class RepositoryMedici implements GenericRepository<Medic>{
    private String url;
    private String username;
    private String password;
    private List<Medic> medici;


    public RepositoryMedici(String url, String username, String password)
    {
        this.url=url;
        this.username=username;
        this.password=password;
        medici=new ArrayList<>();
        getData();
    }

    @Override
    public void getData()
    {
        medici.clear();
        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement statement = connection.prepareStatement("SELECT * from medici");
             ResultSet resultSet = statement.executeQuery())
        {
            while (resultSet.next())
            {
                Long id=resultSet.getLong("id");
                Long idSectie=resultSet.getLong("idsectie");
                Long vechime=resultSet.getLong("vechime");
                String nume=resultSet.getString("nume");
                String rezident=resultSet.getString("rezident");

                medici.add(new Medic(id,idSectie,vechime,nume,rezident));
            }
        } catch (SQLException e)
        {
            e.printStackTrace();
        }
    }

    @Override
    public List<Medic> findAll()
    {
        getData();
        return medici;
    }
}