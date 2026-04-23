package iss.parkingapp.repository;

import iss.parkingapp.domain.Rezervare;
import iss.parkingapp.domain.Utilizator;
import iss.parkingapp.utils.JdbcUtils;
import javafx.collections.ObservableList;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.Properties;

public class RepoRezervare implements IRepoRezervare {
    private JdbcUtils jdbcUtils;
    private static final Logger logger = LogManager.getLogger();

    public RepoRezervare(Properties properties) {
        logger.info("Initializing RezervareDBRepository with properties: {} ", properties);
        jdbcUtils = new JdbcUtils(properties);
    }

    @Override
    public void add(Rezervare entity) {
        logger.info("Adding Rezervare: {}", entity);
        Connection connection = jdbcUtils.getConnection();
        try (PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO Rezervare (id_utilizator, id_loc_parcare, id_masina, data_rezervare, data_sfarsit_rezervare, status) VALUES (?, ?, ?, ?, ?, ?)")) {
            preparedStatement.setLong(1, entity.getId_utilizator());
            preparedStatement.setLong(2, entity.getId_loc_parcare());
            preparedStatement.setLong(3, entity.getId_masina());
            preparedStatement.setTimestamp(4, java.sql.Timestamp.valueOf(entity.getData_rezervare()));
            preparedStatement.setTimestamp(5, java.sql.Timestamp.valueOf(entity.getData_sfarsit_rezervare()));
            preparedStatement.setString(6, entity.getStatus());
            int rowsAffected = preparedStatement.executeUpdate();
            if (rowsAffected > 0) {
                logger.info("Rezervare added successfully: {}", entity);
            } else {
                logger.warn("No rows affected when adding Rezervare: {}", entity);
            }
        } catch (SQLException e) {
            logger.error(e);
            System.err.println("Error DB " + e);
        }
    }

    @Override
    public void delete(Long aLong) {

    }

    @Override
    public void update(Rezervare entity) {

    }

    @Override
    public Rezervare findOne(Long aLong) {
        logger.info("Finding Rezervare by id: {}", aLong);
        Connection connection = jdbcUtils.getConnection();
        try (PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM Rezervare WHERE id_rezervare = ?")) {
            preparedStatement.setLong(1, aLong);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    Long id_utilizator = resultSet.getLong("id_utilizator");
                    Long id_loc_parcare = resultSet.getLong("id_loc_parcare");
                    Long id_masina = resultSet.getLong("id_masina");
                    LocalDateTime data_rezervare = resultSet.getTimestamp("data_rezervare").toLocalDateTime();
                    LocalDateTime data_sfarsit_rezervare = resultSet.getTimestamp("data_sfarsit_rezervare").toLocalDateTime();
                    String status = resultSet.getString("status");
                    Rezervare rezervare = new Rezervare(id_utilizator, id_loc_parcare, id_masina, data_rezervare, data_sfarsit_rezervare, status);
                    rezervare.setId_rezervare(aLong);
                    logger.traceExit(rezervare);
                    return rezervare;
                }
            }
        } catch (SQLException e) {
            logger.error(e);
            System.err.println("Error DB " + e);
        }
        return null;
    }

    @Override
    public ObservableList<Rezervare> findAll() {
        logger.info("Finding all Rezervari");
        Connection connection = jdbcUtils.getConnection();
        ObservableList<Rezervare> rezervari = javafx.collections.FXCollections.observableArrayList();
        try (PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM Rezervare")) {
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    Long id_rezervare = resultSet.getLong("id_rezervare");
                    Long id_utilizator = resultSet.getLong("id_utilizator");
                    Long id_loc_parcare = resultSet.getLong("id_loc_parcare");
                    Long id_masina = resultSet.getLong("id_masina");
                    LocalDateTime data_rezervare = resultSet.getTimestamp("data_rezervare").toLocalDateTime();
                    LocalDateTime data_sfarsit_rezervare = resultSet.getTimestamp("data_sfarsit_rezervare").toLocalDateTime();
                    String status = resultSet.getString("status");
                    Rezervare rezervare = new Rezervare(id_utilizator, id_loc_parcare, id_masina, data_rezervare, data_sfarsit_rezervare, status);
                    rezervare.setId_rezervare(id_rezervare);
                    rezervari.add(rezervare);
                }
            }
        } catch (SQLException e) {
            logger.error(e);
            System.err.println("Error DB " + e);
        }
        logger.traceExit(rezervari);
        return rezervari;
    }

    @Override
    public int size() {
        return 0;
    }

    @Override
    public ObservableList<Rezervare> getAllRezervariForUser(Utilizator user) {
        logger.info("Getting all reservations for user: {}", user);
        Connection connection = jdbcUtils.getConnection();
        ObservableList<Rezervare> rezervari = javafx.collections.FXCollections.observableArrayList();
        try (PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM Rezervare WHERE id_utilizator = ? AND status = 'active'")) {
            preparedStatement.setLong(1, user.getId_utilizator());
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    Long id_rezervare = resultSet.getLong("id_rezervare");
                    Long id_loc_parcare = resultSet.getLong("id_loc_parcare");
                    Long id_masina = resultSet.getLong("id_masina");
                    LocalDateTime data_rezervare = resultSet.getTimestamp("data_rezervare").toLocalDateTime();
                    LocalDateTime data_sfarsit_rezervare = resultSet.getTimestamp("data_sfarsit_rezervare").toLocalDateTime();
                    String status = resultSet.getString("status");
                    Rezervare rezervare = new Rezervare(user.getId_utilizator(), id_loc_parcare, id_masina, data_rezervare, data_sfarsit_rezervare, status);
                    rezervare.setId_rezervare(id_rezervare);
                    rezervari.add(rezervare);
                }
            }
        } catch (SQLException e) {
            logger.error(e);
            System.err.println("Error DB " + e);
        }
        logger.traceExit(rezervari);
        return rezervari;
    }

    @Override
    public void updateExpiredRezervari() {
        logger.info("Updating expired reservations");
        Connection connection = jdbcUtils.getConnection();
        try (PreparedStatement preparedStatement = connection.prepareStatement("UPDATE Rezervare SET status = 'expired' WHERE data_sfarsit_rezervare < ? AND status = 'active'")) {
            preparedStatement.setTimestamp(1, java.sql.Timestamp.valueOf(LocalDateTime.now()));
            int rowsAffected = preparedStatement.executeUpdate();
            if (rowsAffected > 0) {
                logger.info("Expired reservations updated successfully, rows affected: {}", rowsAffected);
            } else {
                logger.info("No expired reservations to update");
            }
        } catch (SQLException e) {
            logger.error(e);
            System.err.println("Error DB " + e);
        }
    }

    @Override
    public ObservableList<Rezervare> getHistoryRezervariUser(Utilizator user) {
        logger.info("Getting reservation history for user: {}", user);
        Connection connection = jdbcUtils.getConnection();
        ObservableList<Rezervare> rezervari = javafx.collections.FXCollections.observableArrayList();
        try (PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM Rezervare WHERE id_utilizator = ?")) {
            preparedStatement.setLong(1, user.getId_utilizator());
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    Long id_rezervare = resultSet.getLong("id_rezervare");
                    Long id_loc_parcare = resultSet.getLong("id_loc_parcare");
                    Long id_masina = resultSet.getLong("id_masina");
                    LocalDateTime data_rezervare = resultSet.getTimestamp("data_rezervare").toLocalDateTime();
                    LocalDateTime data_sfarsit_rezervare = resultSet.getTimestamp("data_sfarsit_rezervare").toLocalDateTime();
                    String status = resultSet.getString("status");
                    Rezervare rezervare = new Rezervare(user.getId_utilizator(), id_loc_parcare, id_masina, data_rezervare, data_sfarsit_rezervare, status);
                    rezervare.setId_rezervare(id_rezervare);
                    rezervari.add(rezervare);
                }
            }
        } catch (SQLException e) {
            logger.error(e);
            System.err.println("Error DB " + e);
        }
        return rezervari;
    }
}
