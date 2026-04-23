package iss.parkingapp.repository;

import iss.parkingapp.domain.Masina;
import iss.parkingapp.domain.Utilizator;
import iss.parkingapp.utils.JdbcUtils;
import javafx.collections.ObservableList;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.Properties;

public class RepoMasina implements IRepoMasina{
    private JdbcUtils jdbcUtils;
    private static final Logger logger = LogManager.getLogger();

    public RepoMasina(Properties properties) {
        logger.info("Initializing CarDBRepository with properties: {} ", properties);
        jdbcUtils = new JdbcUtils(properties);
    }

    @Override
    public void add(Masina entity) {
        logger.info("Adding Car: {}", entity);
        Connection connection = jdbcUtils.getConnection();
        try (PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO Masina (id_utilizator, nr_inmatriculare, marca, model) VALUES (?, ?, ?, ?)")) {
            preparedStatement.setLong(1, entity.getId_utilizator());
            preparedStatement.setString(2, entity.getNr_inmatriculare());
            preparedStatement.setString(3, entity.getMarca());
            preparedStatement.setString(4, entity.getModel());
            int rowsAffected = preparedStatement.executeUpdate();
            if (rowsAffected > 0) {
                logger.info("Car added successfully: {}", entity);
            } else {
                logger.warn("No rows affected when adding Car: {}", entity);
            }
        } catch (SQLException e) {
            logger.error(e);
            System.err.println("Error DB " + e);
        }
    }

    @Override
    public void delete(Long aLong) {
        logger.info("Deleting Car with id: {}", aLong);
        Connection connection = jdbcUtils.getConnection();
        try (PreparedStatement preparedStatement = connection.prepareStatement("DELETE FROM Masina WHERE id_masina = ?")) {
            preparedStatement.setLong(1, aLong);
            int rowsAffected = preparedStatement.executeUpdate();
            if (rowsAffected > 0) {
                logger.info("Car deleted successfully with id: {}", aLong);
            } else {
                logger.warn("No Car found with id: {}", aLong);
            }
        } catch (SQLException e) {
            logger.error(e);
            System.err.println("Error DB " + e);
        }
    }

    @Override
    public void update(Masina entity) {

    }

    @Override
    public Masina findOne(Long aLong) {
        logger.info("Finding Car by id: {}", aLong);
        Connection connection = jdbcUtils.getConnection();
        try (PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM Masina WHERE id_masina = ?")) {
            preparedStatement.setLong(1, aLong);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    Long id_utilizator = resultSet.getLong("id_utilizator");
                    String nr_inmatriculare = resultSet.getString("nr_inmatriculare");
                    String marca = resultSet.getString("marca");
                    String model = resultSet.getString("model");
                    Masina masina = new Masina(id_utilizator, nr_inmatriculare, marca, model);
                    masina.setId_masina(aLong);
                    logger.traceExit(masina);
                    return masina;
                }
            }
        } catch (SQLException e) {
            logger.error(e);
            System.err.println("Error DB " + e);
        }
        logger.traceExit("No Car found with id: {}", aLong);
        return null;
    }

    @Override
    public ObservableList<Masina> findAll() {
        logger.info("Finding all Cars");
        Connection connection = jdbcUtils.getConnection();
        ObservableList<Masina> masini = javafx.collections.FXCollections.observableArrayList();
        try (PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM Masina")) {
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    Long id_utilizator = resultSet.getLong("id_utilizator");
                    String nr_inmatriculare = resultSet.getString("nr_inmatriculare");
                    String marca = resultSet.getString("marca");
                    String model = resultSet.getString("model");
                    Masina masina = new Masina(id_utilizator, nr_inmatriculare, marca, model);
                    masina.setId_masina(resultSet.getLong("id_masina"));
                    masini.add(masina);
                    logger.traceExit(masina);
                }
            }
        } catch (SQLException e) {
            logger.error(e);
            System.err.println("Error DB " + e);
        }
        return masini;
    }

    @Override
    public int size() {
        return 0;
    }

    @Override
    public ObservableList<Masina> getAllForUser(Utilizator user) {
        logger.info("Finding all Cars for User: {}", user);
        Connection connection = jdbcUtils.getConnection();
        ObservableList<Masina> masini = javafx.collections.FXCollections.observableArrayList();
        try (PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM Masina WHERE id_utilizator = ?")) {
            preparedStatement.setLong(1, user.getId_utilizator());
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    Long id_masina = resultSet.getLong("id_masina");
                    String nr_inmatriculare = resultSet.getString("nr_inmatriculare");
                    String marca = resultSet.getString("marca");
                    String model = resultSet.getString("model");
                    Masina masina = new Masina(user.getId_utilizator(), nr_inmatriculare, marca, model);
                    masina.setId_masina(id_masina);
                    masini.add(masina);
                }
            }
        } catch (SQLException e) {
            logger.error(e);
            System.err.println("Error DB " + e);
        }
        return masini;
    }
}
