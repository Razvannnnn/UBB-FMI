package com.example.faptebune.Repository;


import com.example.faptebune.Entities.Nevoie;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class NevoieDataBaseRepository {

    private JDBCUtils jdbcUtils = new JDBCUtils();

    public NevoieDataBaseRepository(){

    }

    public long getMaxId(){
        try{
            Connection connection = jdbcUtils.getConnection();

            PreparedStatement statement = connection.prepareStatement("SELECT MAX(id) FROM nevoi");


            ResultSet resultSet = statement.executeQuery();


            int number = 0;

            if(resultSet.next()){
                number = resultSet.getInt(1);
            }

            return number;

        }catch (SQLException e){
            return -1;
        }
    }


    public List<Nevoie> getAllNevoi() {
        try {
            Connection connection = jdbcUtils.getConnection();

            PreparedStatement statement = connection.prepareStatement("SELECT * FROM nevoi");

            ResultSet resultSet = statement.executeQuery();

            List<Nevoie> nevoi = new ArrayList<>();

            while (resultSet.next()) {
                long idNevoie = resultSet.getLong(1);
                String titlu = resultSet.getString(2);
                String descriere = resultSet.getString(3);
                Timestamp deadline = resultSet.getTimestamp(4);
                long omInNevoie = resultSet.getLong(5);
                long omSalvator = resultSet.getLong(6);
                String status = resultSet.getString(7);

                Nevoie nevoie = new Nevoie(titlu, descriere, deadline.toLocalDateTime(), omInNevoie, omSalvator, status);
                nevoie.setId(idNevoie);

                nevoi.add(nevoie);
            }

            return nevoi;

        } catch (SQLException e) {
            return null;
        }
    }

    public Nevoie updateNevoie(long idNevoie, long idPersoana) {
        try {
            Connection connection = jdbcUtils.getConnection();

            PreparedStatement statement = connection.prepareStatement("UPDATE nevoi SET omsalvator = ?, status = ? WHERE id = ? AND status = ?");

            statement.setLong(1, idPersoana);
            statement.setString(2, "Erou gasit!");
            statement.setLong(3, idNevoie);
            statement.setString(4, "Caut erou!");

            int rowNumber = statement.executeUpdate();

            Nevoie nevoie = new Nevoie("", "", LocalDateTime.now(), 0, 0, "");
            nevoie.setId(0L);


            if (rowNumber > 0) {
                return nevoie;
            }

            return null;

        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public Nevoie adaugaNevoie(Nevoie nevoie) {
        try {
            Connection connection = jdbcUtils.getConnection();

            PreparedStatement statement = connection.prepareStatement("INSERT INTO nevoi VALUES (?, ?, ?, ?, ?, ?, ?)");

            statement.setLong(1, nevoie.getId());
            statement.setString(2, nevoie.getTitlu());
            statement.setString(3, nevoie.getDescriere());

            Timestamp dateTime = Timestamp.valueOf(nevoie.getDeadline());
            statement.setTimestamp(4, dateTime);

            statement.setLong(5, nevoie.getOmInNevoie());
            statement.setNull(6, Types.NULL);
            statement.setString(7, "Caut erou!");

            int rowNumber = statement.executeUpdate();

            if(rowNumber > 0){
                return nevoie;
            }
            return null;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

}
