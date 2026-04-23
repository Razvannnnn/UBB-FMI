package org.example.ajut.Repository;

import org.example.ajut.Domain.Nevoie;
import org.example.ajut.Domain.Oras;
import org.example.ajut.Domain.Persoana;

import java.sql.Connection;
import java.sql.DriverManager;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class NevoieRepo implements Repository<Nevoie> {
    private String url;
    private String user;
    private String password;

    public NevoieRepo(String url, String user, String password) {
        this.url = url;
        this.user = user;
        this.password = password;
    }

    public void save(Nevoie nevoie) {
        try (Connection connection = DriverManager.getConnection(url, user, password);
             var statement = connection.prepareStatement("INSERT INTO nevoie(titlu, descriere, deadline, omInNevoie, omSalvator, status) VALUES (?, ?, ?, ?, ?, ?)")) {
            statement.setString(1, nevoie.getTitlu());
            statement.setString(2, nevoie.getDescriere());
            statement.setTimestamp(3, java.sql.Timestamp.valueOf(nevoie.getDeadline()));
            statement.setLong(4, nevoie.getOmInNevoie());
            if(nevoie.getOmSalvator() != null) {
                statement.setLong(5, nevoie.getOmSalvator());
            } else {
                statement.setNull(5, java.sql.Types.BIGINT);
            }
            statement.setString(6, nevoie.getStatus());
            statement.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Iterable<Nevoie> nevoiDinOras(Persoana persoana) {
        Set<Nevoie> nevoi = new HashSet<>();
        try (Connection connection = DriverManager.getConnection(url, user, password);
             var statement = connection.prepareStatement("SELECT * FROM nevoie WHERE omInNevoie IN (SELECT id FROM persoana WHERE oras = ?::Oras) AND omInNevoie != ? AND status = ?") ){
            statement.setString(1, persoana.getOras().name());
            statement.setLong(2, persoana.getId());
            statement.setString(3, "Caut Erou!");
            var resultSet = statement.executeQuery();
            while (resultSet.next()) {
                var id = resultSet.getLong("id");
                var titlu = resultSet.getString("titlu");
                var descriere = resultSet.getString("descriere");
                LocalDateTime deadline = resultSet.getTimestamp("deadline").toLocalDateTime();
                var omInNevoie = resultSet.getLong("omInNevoie");
                var omSalvator = resultSet.getLong("omSalvator");
                var status = resultSet.getString("status");
                Nevoie nevoie = new Nevoie(titlu, descriere, deadline, omInNevoie, omSalvator, status);
                nevoie.setId(id);
                nevoi.add(nevoie);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return nevoi;
    }

    @Override
    public Iterable<Nevoie> findAll() {
        Set<Nevoie> nevoi = new HashSet<>();
        try (Connection connection = DriverManager.getConnection(url, user, password);
             var statement = connection.prepareStatement("SELECT * FROM nevoie");
             var resultSet = statement.executeQuery()) {
            while (resultSet.next()) {
                var id = resultSet.getLong("id");
                var titlu = resultSet.getString("titlu");
                var descriere = resultSet.getString("descriere");
                LocalDateTime deadline = resultSet.getTimestamp("deadline").toLocalDateTime();
                var omInNevoie = resultSet.getLong("omInNevoie");
                var omSalvator = resultSet.getLong("omSalvator");
                var status = resultSet.getString("status");
                Nevoie nevoie = new Nevoie(titlu, descriere, deadline, omInNevoie, omSalvator, status);
                nevoie.setId(id);
                nevoi.add(nevoie);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return nevoi;
    }

    public void update(Nevoie nevoie, Persoana persoana) {
        try (Connection connection = DriverManager.getConnection(url, user, password);
             var statement = connection.prepareStatement("UPDATE nevoie SET status = ?, omsalvator = ? WHERE id = ?")) {
            statement.setString(1, "Erou gasit!");
            statement.setLong(2, persoana.getId());
            statement.setLong(3, nevoie.getId());
            statement.executeUpdate();
        }catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Iterable<Nevoie> nevoiPeCareLeRezolv(Persoana persoana) {
        Set<Nevoie> nevoi = new HashSet<>();
        try (Connection connection = DriverManager.getConnection(url, user, password);
             var statement = connection.prepareStatement("SELECT * FROM nevoie WHERE omSalvator = ?")) {
            statement.setLong(1, persoana.getId());
            var resultSet = statement.executeQuery();
            while (resultSet.next()) {
                var id = resultSet.getLong("id");
                var titlu = resultSet.getString("titlu");
                var descriere = resultSet.getString("descriere");
                LocalDateTime deadline = resultSet.getTimestamp("deadline").toLocalDateTime();
                var omInNevoie = resultSet.getLong("omInNevoie");
                var omSalvator = resultSet.getLong("omSalvator");
                var status = resultSet.getString("status");
                Nevoie nevoie = new Nevoie(titlu, descriere, deadline, omInNevoie, omSalvator, status);
                nevoie.setId(id);
                nevoi.add(nevoie);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return nevoi;
    }
}
