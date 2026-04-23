package org.example.reteasocializare.Repository.DB;

import org.example.reteasocializare.Domain.Prietenie;
import org.example.reteasocializare.Domain.Validators.Validator;
import org.example.reteasocializare.Domain.Validators.ValidatorException;
import org.example.reteasocializare.Repository.Repository;

import java.sql.*;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

public class PrietenieDBRepository implements Repository<Long, Prietenie> {
    private String url;
    private String username;
    private String password;
    private Validator<Prietenie> validator;

    public PrietenieDBRepository(String url, String username, String password, Validator<Prietenie> validator) {
        this.url = url;
        this.username = username;
        this.password = password;
        this.validator = validator;
    }

    @Override
    public Optional<Prietenie> findOne(Long id) {
        Prietenie prietenie = null;
        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement statement = connection.prepareStatement("SELECT * FROM Prietenie WHERE id = ?");) {
            statement.setLong(1, id);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                Long idPrietenie = resultSet.getLong("id");
                Long idUtilizator1 = resultSet.getLong("id_user1");
                Long idUtilizator2 = resultSet.getLong("id_user2");

                prietenie = new Prietenie(idUtilizator1, idUtilizator2);
                prietenie.setId(idPrietenie);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return Optional.ofNullable(prietenie);
    }

    @Override
    public Iterable<Prietenie> findAll() {
        Set<Prietenie> prietenii = new HashSet<>();
        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement statement = connection.prepareStatement("SELECT * FROM Prietenie");
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                Long id = resultSet.getLong("id");
                Long idUtilizator1 = resultSet.getLong("id_user1");
                Long idUtilizator2 = resultSet.getLong("id_user2");

                Prietenie prietenie = new Prietenie(idUtilizator1, idUtilizator2);
                prietenie.setId(id);
                prietenii.add(prietenie);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return prietenii;
    }

    @Override
    public Optional<Prietenie> save(Prietenie entity) throws ValidatorException {
        validator.validate(entity);
        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement statement = connection.prepareStatement("INSERT INTO Prietenie (id_user1, id_user2) VALUES (?, ?)", Statement.RETURN_GENERATED_KEYS)) {
            statement.setLong(1, entity.getIdUtilizator1());
            statement.setLong(2, entity.getIdUtilizator2());
            int rowsInserted = statement.executeUpdate();
            if (rowsInserted > 0) {
                ResultSet resultSet = statement.getGeneratedKeys();
                if (resultSet.next()) {
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
    public Optional<Prietenie> delete(Long id) {
        Optional<Prietenie> prietenie = findOne(id);
        if (prietenie.isPresent()) {
            try (Connection connection = DriverManager.getConnection(url, username, password);
                 PreparedStatement statement = connection.prepareStatement("DELETE FROM Prietenie WHERE id = ?")) {
                statement.setLong(1, id);
                statement.executeUpdate();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return prietenie;
    }

    @Override
    public Optional<Prietenie> update(Prietenie entity) throws ValidatorException {
        validator.validate(entity);
        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement statement = connection.prepareStatement("UPDATE Prietenie SET id_user1 = ?, if_user2 = ? WHERE id = ?")) {
            statement.setLong(1, entity.getIdUtilizator1());
            statement.setLong(2, entity.getIdUtilizator2());
            statement.setLong(3, entity.getId());

            int rowsUpdated = statement.executeUpdate();
            if (rowsUpdated > 0) {
                return Optional.empty();
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return Optional.of(entity);
        }
        return Optional.empty();
    }
}