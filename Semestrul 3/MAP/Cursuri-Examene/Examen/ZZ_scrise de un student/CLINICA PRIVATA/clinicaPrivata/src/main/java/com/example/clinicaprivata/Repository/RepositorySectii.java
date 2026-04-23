package com.example.clinicaprivata.Repository;

import com.example.clinicaprivata.Entities.Sectie;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class RepositorySectii implements GenericRepository<Sectie>{
    private String url;
    private String username;
    private String password;
    private List<Sectie> sectii;


    public RepositorySectii(String url, String username, String password)
    {
        this.url=url;
        this.username=username;
        this.password=password;
        sectii=new ArrayList<>();
        getData();
    }

    @Override
    public void getData()
    {
        sectii.clear();
        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement statement = connection.prepareStatement("SELECT * from sectii");
             ResultSet resultSet = statement.executeQuery())
        {
            while (resultSet.next())
            {
                Long id=resultSet.getLong("id");
                String nume=resultSet.getString("nume");
                Long idSefDeSectie=resultSet.getLong("idsefdesectie");
                Long pretPerConsultatie=resultSet.getLong("pretperconsultatie");
                Long durataMaximaConsultatie=resultSet.getLong("duratamaximaconsultatie");

                sectii.add(new Sectie(id,idSefDeSectie,pretPerConsultatie,durataMaximaConsultatie,nume));
            }
        } catch (SQLException e)
        {
            e.printStackTrace();
        }
    }

    @Override
    public List<Sectie> findAll()
    {
        getData();
        return sectii;
    }
}