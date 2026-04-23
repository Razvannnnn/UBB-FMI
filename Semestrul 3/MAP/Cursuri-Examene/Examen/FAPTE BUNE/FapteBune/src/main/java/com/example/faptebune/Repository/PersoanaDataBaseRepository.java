package com.example.faptebune.Repository;


import com.example.faptebune.Entities.Oras;
import com.example.faptebune.Entities.Persoana;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class PersoanaDataBaseRepository {

    private JDBCUtils jdbcUtils = new JDBCUtils();

    public PersoanaDataBaseRepository() {

    }

    public Persoana addPersoana(Persoana persoana) {
        try {
            Connection connection = jdbcUtils.getConnection();

            PreparedStatement statement = connection.prepareStatement("INSERT INTO persoane VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)");

            statement.setLong(1, persoana.getId());
            statement.setString(2, persoana.getNume());
            statement.setString(3, persoana.getPrenume());
            statement.setString(4, persoana.getUsername());
            statement.setString(5, persoana.getParola());
            statement.setString(6, persoana.getOras().toString());
            statement.setString(7, persoana.getStrada());
            statement.setString(8, persoana.getNumarStrada());
            statement.setString(9, persoana.getTelefon());

            int rowNumber = statement.executeUpdate();

            if (rowNumber > 0) {
                return persoana;
            }

            return null;


        } catch (SQLException e) {
            return null;
        }
    }

    public boolean verifyIfAPersonExist(String username){
        try{
            Connection connection = jdbcUtils.getConnection();

            PreparedStatement statement = connection.prepareStatement("SELECT COUNT(*) FROM persoane WHERE username = ?");

            statement.setString(1, username);

            ResultSet resultSet = statement.executeQuery();


            int number = -1;

            if(resultSet.next()){
                number = resultSet.getInt(1);
            }

            return number > 0;

        }catch (SQLException e){
            return false;
        }
    }

    public long getMaxId(){
        try{
            Connection connection = jdbcUtils.getConnection();

            PreparedStatement statement = connection.prepareStatement("SELECT MAX(id) FROM persoane");


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

    public List<Persoana> getAllPersoane() {
        try {
            Connection connection = jdbcUtils.getConnection();

            PreparedStatement statement = connection.prepareStatement("SELECT * FROM persoane");
            ResultSet resultSet = statement.executeQuery();

            List<Persoana> persoane = new ArrayList<>();

            while (resultSet.next()) {
                long idPersoana = resultSet.getLong(1);
                String nume = resultSet.getString(2);
                String prenume = resultSet.getString(3);
                String username = resultSet.getString(4);
                String parola = resultSet.getString(5);
                String oras = resultSet.getString(6);
                String strada = resultSet.getString(7);
                String numarStrada = resultSet.getString(8);
                String telefon = resultSet.getString(9);

                Oras orasel = Oras.valueOf(oras);

                Persoana persoana1 = new Persoana(nume, prenume, username, parola, orasel, strada, numarStrada, telefon);
                persoana1.setId(idPersoana);

                persoane.add(persoana1);
            }

            return persoane;

        } catch (SQLException e) {
            return null;
        }
    }

    public Persoana findOnePersoana(long idPersoana){
        try{
            Connection connection = jdbcUtils.getConnection();

            PreparedStatement statement = connection.prepareStatement("SELECT * FROM persoane WHERE id = ?");

            statement.setLong(1, idPersoana);

            ResultSet resultSet = statement.executeQuery();

            if(resultSet.next()){
                long idP = resultSet.getLong(1);
                String nume = resultSet.getString(2);
                String prenume = resultSet.getString(3);
                String username = resultSet.getString(4);
                String parola = resultSet.getString(5);
                String oras = resultSet.getString(6);
                String strada = resultSet.getString(7);
                String numarStrada = resultSet.getString(8);
                String telefon = resultSet.getString(9);

                Oras orasel = Oras.valueOf(oras);

                Persoana persoana = new Persoana(nume, prenume, username, parola, orasel, strada, numarStrada, telefon);
                persoana.setId(idP);

                return persoana;
            }

            return null;

        }catch (SQLException e){
            System.out.println("Eroare");
            return null;
        }
    }


}
