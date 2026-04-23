package org.example.reteasocializare.Repository.DB;

import org.example.reteasocializare.Domain.Prietenie;
import org.example.reteasocializare.Domain.Validators.Validator;
import org.example.reteasocializare.Domain.Validators.ValidatorException;
import org.example.reteasocializare.Repository.Paging.Page;
import org.example.reteasocializare.Repository.Paging.Pageable;
import org.example.reteasocializare.Repository.Paging.PrieteniePagingRepository;
import org.example.reteasocializare.Repository.Repository;

import java.sql.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

public class PrietenieDBRepository implements PrieteniePagingRepository<Long, Prietenie> {
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
                Integer status = resultSet.getInt("status");
                LocalDateTime date = resultSet.getTimestamp("data").toLocalDateTime();

                prietenie = new Prietenie(idUtilizator1, idUtilizator2, status);
                prietenie.setId(idPrietenie);
                prietenie.setDate(date);
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
                Integer status = resultSet.getInt("status");
                LocalDateTime date = resultSet.getTimestamp("data").toLocalDateTime();

                Prietenie prietenie = new Prietenie(idUtilizator1, idUtilizator2, status);
                prietenie.setId(id);
                prietenie.setDate(date);
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
             PreparedStatement statement = connection.prepareStatement("INSERT INTO Prietenie (id_user1, id_user2, status, data) VALUES (?, ?, ?, ?)", Statement.RETURN_GENERATED_KEYS)) {
            statement.setLong(1, entity.getIdUtilizator1());
            statement.setLong(2, entity.getIdUtilizator2());
            statement.setInt(3, entity.getStatus());
            statement.setTimestamp(4, Timestamp.valueOf(entity.getDate()));
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
             PreparedStatement statement = connection.prepareStatement("UPDATE Prietenie SET id_user1 = ?, id_user2 = ?, status = ?, data = ? WHERE id = ?")) {
            statement.setLong(1, entity.getIdUtilizator1());
            statement.setLong(2, entity.getIdUtilizator2());
            statement.setInt(3, entity.getStatus());
            statement.setTimestamp(4, Timestamp.valueOf(entity.getDate()));
            statement.setLong(5, entity.getId());

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

    public Optional<Prietenie> updateStatus(Prietenie entity, Integer newStatus) throws ValidatorException {
        validator.validate(entity);
        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement statement = connection.prepareStatement("UPDATE Prietenie SET id_user1 = ?, id_user2 = ?, status = ?, data = ? WHERE id = ?")) {
            statement.setLong(1, entity.getIdUtilizator1());
            statement.setLong(2, entity.getIdUtilizator2());
            statement.setInt(3, newStatus);
            statement.setTimestamp(4, Timestamp.valueOf(entity.getDate()));
            statement.setLong(5, entity.getId());

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

    @Override
    public Page<Prietenie> findAllFriendRequests(Pageable pageable) {
        List<Prietenie> prietenii = new ArrayList<>();
        try(Connection connection = DriverManager.getConnection(url, username, password);
            PreparedStatement pageStatement = connection.prepareStatement("SELECT * FROM Prietenie WHERE (status = 1 AND id_user1 = ?) OR (status = 1 AND id_user2 = ?)");
            PreparedStatement countStatement = connection.prepareStatement("SELECT COUNT(*) AS total FROM Prietenie WHERE status = 1");
        ) {
            pageStatement.setInt(1, pageable.getPageSize());
            pageStatement.setInt(2, pageable.getPageNumber() * pageable.getPageSize());
            try(ResultSet pageResultSet = pageStatement.executeQuery();
                ResultSet countResultSet = countStatement.executeQuery()) {
                while (pageResultSet.next()) {
                    Long id = pageResultSet.getLong("id");
                    Long idUtilizator1 = pageResultSet.getLong("id_user1");
                    Long idUtilizator2 = pageResultSet.getLong("id_user2");
                    Integer status = pageResultSet.getInt("status");
                    LocalDateTime date = pageResultSet.getTimestamp("data").toLocalDateTime();

                    Prietenie prietenie = new Prietenie(idUtilizator1, idUtilizator2, status);
                    prietenie.setId(id);
                    prietenie.setDate(date);
                    prietenii.add(prietenie);
                }
                int totalCount = 0;
                if(countResultSet.next()) {
                    totalCount = countResultSet.getInt("total");
                }
                return new Page<>(prietenii, totalCount);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Page<Prietenie> findAllUserFriends(Pageable pageable, Long idUser) {
        List<Prietenie> prietenii = new ArrayList<>();
        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement pageStatement = connection.prepareStatement("SELECT * FROM Prietenie WHERE (status = 3 AND id_user1 = ?) OR (status = 3 AND id_user2 = ?)" +  "LIMIT ? OFFSET ?");
             PreparedStatement countStatement = connection.prepareStatement("SELECT COUNT(*) AS total FROM Prietenie WHERE status = 3 ");
        ) {
            pageStatement.setLong(1, idUser);
            pageStatement.setLong(2, idUser);
            pageStatement.setInt(3, pageable.getPageSize());
            pageStatement.setInt(4, pageable.getPageNumber() * pageable.getPageSize());
            try(ResultSet pageResultSet = pageStatement.executeQuery();
                ResultSet countResultSet = countStatement.executeQuery()) {
                while (pageResultSet.next()) {
                    Long id = pageResultSet.getLong("id");
                    Long idUtilizator1 = pageResultSet.getLong("id_user1");
                    Long idUtilizator2 = pageResultSet.getLong("id_user2");
                    Integer status = pageResultSet.getInt("status");
                    LocalDateTime date = pageResultSet.getTimestamp("data").toLocalDateTime();

                    if(idUtilizator1.equals(idUser) || idUtilizator2.equals(idUser)) {
                        Prietenie prietenie = new Prietenie(idUtilizator1, idUtilizator2, status);
                        prietenie.setId(id);
                        prietenie.setDate(date);
                        prietenii.add(prietenie);
                    }
                }
                int totalCount = 0;
                if(countResultSet.next()) {
                    totalCount = countResultSet.getInt("total");
                }
                return new Page<>(prietenii, totalCount);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error DB" + e);
        }
    }

    @Override
    public Page<Prietenie> findAllUserFriendRequests(Pageable pageable, Long idUser) {
        List<Prietenie> prietenii = new ArrayList<>();
        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement pageStatement = connection.prepareStatement("SELECT * FROM Prietenie WHERE (status = 1 AND id_user1 = ?) OR (status = 1 AND id_user2 = ?)");
             PreparedStatement countStatement = connection.prepareStatement("SELECT COUNT(*) AS total FROM Prietenie WHERE status = 1");
        ) {
            try(ResultSet pageResultSet = pageStatement.executeQuery();
                ResultSet countResultSet = countStatement.executeQuery()) {
                while (pageResultSet.next()) {
                    Long id = pageResultSet.getLong("id");
                    Long idUtilizator1 = pageResultSet.getLong("id_user1");
                    Long idUtilizator2 = pageResultSet.getLong("id_user2");
                    Integer status = pageResultSet.getInt("status");
                    LocalDateTime date = pageResultSet.getTimestamp("data").toLocalDateTime();
                    if(idUtilizator1.equals(idUser) || idUtilizator2.equals(idUser)) {
                        Prietenie prietenie = new Prietenie(idUtilizator1, idUtilizator2, status);
                        prietenie.setId(id);
                        prietenie.setDate(date);
                        prietenii.add(prietenie);
                    }
                }
                int totalCount = 0;
                if(countResultSet.next()) {
                    totalCount = countResultSet.getInt("total");
                }
                return new Page<>(prietenii, totalCount);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error DB" + e);
        }
    }

    @Override
    public Page<Prietenie> findAll(Pageable pageable) {
        List<Prietenie> prietenii = new ArrayList<>();
        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement pageStatement = connection.prepareStatement("SELECT * FROM Prietenie WHERE status = 3");
             PreparedStatement countStatement = connection.prepareStatement("SELECT COUNT(*) AS total FROM Prietenie WHERE status = 3");
        ) {
            try(ResultSet pageResultSet = pageStatement.executeQuery();
                ResultSet countResultSet = countStatement.executeQuery()) {
                while (pageResultSet.next()) {
                    Long id = pageResultSet.getLong("id");
                    Long idUtilizator1 = pageResultSet.getLong("id_user1");
                    Long idUtilizator2 = pageResultSet.getLong("id_user2");
                    Integer status = pageResultSet.getInt("status");
                    LocalDateTime date = pageResultSet.getTimestamp("data").toLocalDateTime();

                    Prietenie prietenie = new Prietenie(idUtilizator1, idUtilizator2, status);
                    prietenie.setId(id);
                    prietenie.setDate(date);
                    prietenii.add(prietenie);
                }
                int totalCount = 0;
                if(countResultSet.next()) {
                    totalCount = countResultSet.getInt("total");
                }
                return new Page<>(prietenii, totalCount);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error DB" + e);
        }
    }
}