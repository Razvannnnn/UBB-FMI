package org.example.ajut.Repository;

import org.example.ajut.Domain.Oras;
import org.example.ajut.Domain.Persoana;

import java.sql.Connection;
import java.sql.DriverManager;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class PersoanaRepo implements Repository<Persoana> {
    private String url;
    private String user;
    private String password;

    public PersoanaRepo(String url, String user, String password) {
        this.url = url;
        this.user = user;
        this.password = password;
    }

    @Override
    public Iterable<Persoana> findAll() {
        Set<Persoana> persoane = new HashSet<>();
        try (Connection connection = DriverManager.getConnection(url, user, password);
             var statement = connection.prepareStatement("SELECT * FROM persoana");
             var resultSet = statement.executeQuery()) {
            while (resultSet.next()) {
                var id = resultSet.getLong("id");
                var nume = resultSet.getString("nume");
                var prenume = resultSet.getString("prenume");
                var username = resultSet.getString("username");
                var parola = resultSet.getString("parola");
                Oras oras = resultSet.getString("oras") != null ? Oras.valueOf(resultSet.getString("oras")) : null;
                var strada = resultSet.getString("strada");
                var numarStrada = resultSet.getString("numarStrada");
                var telefon = resultSet.getString("telefon");
                var persoana = new Persoana(nume, prenume, username, parola, oras, strada, numarStrada, telefon);
                persoana.setId(id);
                persoane.add(persoana);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return persoane;
    }

    public void save(Persoana persoana) {
        try (Connection connection = DriverManager.getConnection(url, user, password);
             var statement = connection.prepareStatement("INSERT INTO persoana(nume, prenume, username, parola, oras, strada, numarStrada, telefon) VALUES (?, ?, ?, ?, ?::Oras, ?, ?, ?)")) {
            statement.setString(1, persoana.getNume());
            statement.setString(2, persoana.getPrenume());
            statement.setString(3, persoana.getUsername());
            statement.setString(4, persoana.getParola());
            statement.setString(5, persoana.getOras().name());
            statement.setString(6, persoana.getStrada());
            statement.setString(7, persoana.getNumarStrada());
            statement.setString(8, persoana.getTelefon());
            statement.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Persoana findByUsername(String username) {
        Persoana persoana = null;
        try (Connection connection = DriverManager.getConnection(url, user, password);
             var statement = connection.prepareStatement("SELECT * FROM persoana WHERE username = ?")) {
            statement.setString(1, username);
            var resultSet = statement.executeQuery();
            if (resultSet.next()) {
                var id = resultSet.getLong("id");
                var nume = resultSet.getString("nume");
                var prenume = resultSet.getString("prenume");
                var parola = resultSet.getString("parola");
                Oras oras = resultSet.getString("oras") != null ? Oras.valueOf(resultSet.getString("oras")) : null;
                var strada = resultSet.getString("strada");
                var numarStrada = resultSet.getString("numarStrada");
                var telefon = resultSet.getString("telefon");
                persoana = new Persoana(nume, prenume, username, parola, oras, strada, numarStrada, telefon);
                persoana.setId(id);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return persoana;
    }
}
