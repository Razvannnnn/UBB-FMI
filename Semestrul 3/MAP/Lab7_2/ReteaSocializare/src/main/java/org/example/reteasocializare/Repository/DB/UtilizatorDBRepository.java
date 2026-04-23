
package org.example.reteasocializare.Repository.DB;

import org.example.reteasocializare.Domain.Entity;
import org.example.reteasocializare.Domain.Utilizator;
import org.example.reteasocializare.Domain.Validators.Validator;
import org.example.reteasocializare.Domain.Validators.ValidatorException;
import org.example.reteasocializare.Repository.Repository;

import java.sql.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.temporal.TemporalAccessor;
import java.util.*;
import java.util.Date;

public class UtilizatorDBRepository implements Repository<Long, Utilizator> {
    private String url;
    private String username;
    private String password;
    private Validator<Utilizator> validator;

    public UtilizatorDBRepository(String url, String username, String password, Validator<Utilizator> validator) {
        this.url = url;
        this.username = username;
        this.password = password;
        this.validator = validator;
    }

    @Override
    public Optional<Utilizator> findOne(Long aLong) {
        Utilizator utilizator = null;
        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement statement = connection.prepareStatement("SELECT * from utilizator WHERE id = ?")) {
            statement.setLong(1, aLong);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                Long id = resultSet.getLong("id");
                String nume = resultSet.getString("nume");
                String prenume = resultSet.getString("prenume");
                String username = resultSet.getString("username");
                String password = resultSet.getString("password");

                utilizator = new Utilizator(username, nume, prenume, password);
                utilizator.setId(id);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return Optional.ofNullable(utilizator);
    }

    @Override
    public Iterable<Utilizator> findAll() {
        Set<Utilizator> users = new HashSet<>();
        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement statement = connection.prepareStatement("SELECT * from utilizator");
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                Long id = resultSet.getLong("id");
                String nume = resultSet.getString("nume");
                String prenume = resultSet.getString("prenume");
                String username = resultSet.getString("username");
                String password = resultSet.getString("password");

                Utilizator utilizator = new Utilizator(username, nume, prenume, password);
                utilizator.setId(id);
                users.add(utilizator);
            }
            return users;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return users;
    }

    @Override
    public Optional<Utilizator> save(Utilizator entity) throws ValidatorException {
        validator.validate(entity);
        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement statement = connection.prepareStatement("INSERT INTO utilizator (username, nume, prenume, password) VALUES (?, ?, ?, ?)")) {
            statement.setString(1, entity.getNumeUtilizator());
            statement.setString(2, entity.getNume());
            statement.setString(3, entity.getPrenume());
            statement.setString(4, entity.getPassword());
            int rowsInserted = statement.executeUpdate();
            if(rowsInserted > 0) {
                ResultSet resultSet = statement.getGeneratedKeys();
                if(resultSet.next()) {
                    entity.setId(resultSet.getLong(1));
                }
                return Optional.empty();
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return Optional.of(entity);
        }
        return Optional.of(entity);
    }

    @Override
    public Optional<Utilizator> delete(Long aLong) {
        Optional<Utilizator> utilizator = findOne(aLong);
        if(utilizator.isPresent()) {
            try (Connection connection = DriverManager.getConnection(url, username, password);
                 PreparedStatement statement = connection.prepareStatement("DELETE FROM utilizator WHERE id = ?")) {
                statement.setLong(1, aLong);
                statement.executeUpdate();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return utilizator;
    }

    @Override
    public Optional<Utilizator> update(Utilizator entity) throws ValidatorException {
        validator.validate(entity);
        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement statement = connection.prepareStatement("UPDATE utilizator SET nume = ?, prenume = ? WHERE id = ?")) {
            statement.setString(1, entity.getNume());
            statement.setString(2, entity.getPrenume());
            statement.setLong(3, entity.getId());

            int rowsUpdated = statement.executeUpdate();
            if(rowsUpdated > 0) {
                return Optional.empty();
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return Optional.of(entity);
        }
        return Optional.empty();
    }
}
