package com.example.clinicaprivata.Repository;

import com.example.clinicaprivata.Entities.Consultatie;

import java.sql.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class RepositoryConsultatii implements GenericRepository<Consultatie>{
    private String url;
    private String username;
    private String password;
    private List<Consultatie> consultatii;


    public RepositoryConsultatii(String url, String username, String password)
    {
        this.url=url;
        this.username=username;
        this.password=password;
        consultatii=new ArrayList<>();
        getData();
    }

    @Override
    public void getData()
    {
        consultatii.clear();
        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement statement = connection.prepareStatement("SELECT * from consultatii");
             ResultSet resultSet = statement.executeQuery())
        {
            while (resultSet.next())
            {
                Long id=resultSet.getLong("id");
                Long idMedic=resultSet.getLong("idmedic");
                String cnpPacient=resultSet.getString("cnppacient");
                String numePacient=resultSet.getString("numepacient");
                String data_str=resultSet.getString("data");
                String ora_str=resultSet.getString("ora");

                LocalDate data=LocalDate.parse(data_str);
                LocalTime ora=LocalTime.parse(ora_str);
                consultatii.add(new Consultatie(id,idMedic,cnpPacient,numePacient,data,ora));
            }
        } catch (SQLException e)
        {
            e.printStackTrace();
        }
    }

    public void adaugaConsultatie(Long id,Long idMedic,String cnp,String nume,String data,String ora)
    {
        String sql="insert into consultatii (id,idmedic,cnppacient,numepacient,data,ora) values (?,?,?,?,?,?)";
        try (Connection connection=DriverManager.getConnection(url, username, password);
             PreparedStatement ps = connection.prepareStatement(sql))
        {
            ps.setLong(1,id);
            ps.setLong(2,idMedic);
            ps.setString(3,cnp);
            ps.setString(4,nume);
            ps.setString(5,data);
            ps.setString(6,ora);
            ps.executeUpdate();
        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }
    }


    @Override
    public List<Consultatie> findAll()
    {
        getData();
        return consultatii;
    }
}