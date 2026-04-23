package com.example.ati.Repository;

import com.example.ati.Entities.Pacient;
import com.example.ati.Entities.Pat;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class PacientiRepository implements GenericRepository<Pacient>{
    private String url;
    private String username;
    private String password;
    private List<Pacient> pacienti;


    public PacientiRepository(String url, String username, String password)
    {
        this.url=url;
        this.username=username;
        this.password=password;
        pacienti=new ArrayList<>();
        getData();
    }

    @Override
    public void getData()
    {
        pacienti.clear();
        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement statement = connection.prepareStatement("SELECT * from pacienti");
             ResultSet resultSet = statement.executeQuery())
        {
            while (resultSet.next())
            {
                String cnp=resultSet.getString("cnp");
                Long varsta=resultSet.getLong("varsta");
                String prematur=resultSet.getString("prematur");
                String diagnostic_principal=resultSet.getString("diagnostic");
                Long gravitate=resultSet.getLong("gravitate");

                Pacient pacient=new Pacient(cnp,varsta,prematur,diagnostic_principal,gravitate);
                pacienti.add(pacient);
            }
        } catch (SQLException e)
        {
            e.printStackTrace();
        }
    }

    @Override
    public List<Pacient> findAll()
    {
        getData();
        return pacienti;
    }


    public void eliberarePat(String cnp) {
        String sql="delete from \"pacienti\" where cnp=?";
        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1,cnp);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}
