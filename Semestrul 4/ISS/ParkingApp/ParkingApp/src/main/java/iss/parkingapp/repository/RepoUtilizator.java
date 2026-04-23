package iss.parkingapp.repository;

import iss.parkingapp.domain.Utilizator;
import iss.parkingapp.utils.JdbcUtils;
import javafx.collections.ObservableList;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Properties;

public class RepoUtilizator implements IRepoUtilizator{
    private JdbcUtils jdbcUtils;
    private static final Logger logger = LogManager.getLogger();

    public RepoUtilizator(Properties properties) {
        logger.info("Initializing UtilizatorDBRepository with properties: {} ", properties);
        jdbcUtils = new JdbcUtils(properties);
    }

    @Override
    public void add(Utilizator entity) {
        logger.info("Adding User: {}", entity);
        Connection connection = jdbcUtils.getConnection();
        try (PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO Utilizator (nume, prenume, email, password, rol) VALUES (?, ?, ?, ?, ?)")) {
            preparedStatement.setString(1, entity.getNume());
            preparedStatement.setString(2, entity.getPrenume());
            preparedStatement.setString(3, entity.getEmail());
            preparedStatement.setString(4, entity.getPassword());
            preparedStatement.setString(5, "user");
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            logger.error(e);
            System.err.println("Error DB " + e);
        }
    }

    @Override
    public void delete(Long aLong) {

    }

    @Override
    public void update(Utilizator entity) {
        logger.info("Updating User: {}", entity);
        Connection connection = jdbcUtils.getConnection();
        try (PreparedStatement preparedStatement = connection.prepareStatement("UPDATE Utilizator SET nume = ?, prenume = ?, email = ?, password = ? WHERE id_utilizator = ?")) {
            preparedStatement.setString(1, entity.getNume());
            preparedStatement.setString(2, entity.getPrenume());
            preparedStatement.setString(3, entity.getEmail());
            preparedStatement.setString(4, entity.getPassword());
            preparedStatement.setLong(5, entity.getId_utilizator());
            int rowsAffected = preparedStatement.executeUpdate();
            if (rowsAffected > 0) {
                logger.info("User updated successfully: {}", entity);
            } else {
                logger.warn("No User found with id: {}", entity.getId_utilizator());
            }
        } catch (SQLException e) {
            logger.error(e);
            System.err.println("Error DB " + e);
        }
    }

    @Override
    public Utilizator findOne(Long aLong) {
        logger.info("Finding User by id: {}", aLong);
        Connection connection = jdbcUtils.getConnection();
        try (PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM Utilizator WHERE id_utilizator = ?")) {
            preparedStatement.setLong(1, aLong);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    String nume = resultSet.getString("nume");
                    String prenume = resultSet.getString("prenume");
                    String email = resultSet.getString("email");
                    String password = resultSet.getString("password");
                    String rol = resultSet.getString("rol");
                    Utilizator utilizator = new Utilizator(nume, prenume, email, password);
                    utilizator.setId_utilizator(aLong);
                    logger.traceExit(utilizator);
                    return utilizator;
                }
            }
        } catch (SQLException e) {
            logger.error(e);
            System.err.println("Error DB " + e);
        }
        logger.traceExit("No User found with id: {}", aLong);
        return null;
    }

    @Override
    public ObservableList<Utilizator> findAll() {
        logger.info("Finding all Users");
        Connection connection = jdbcUtils.getConnection();
        ObservableList<Utilizator> utilizatori = javafx.collections.FXCollections.observableArrayList();
        try (PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM Utilizator")) {
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    String nume = resultSet.getString("nume");
                    String prenume = resultSet.getString("prenume");
                    String email = resultSet.getString("email");
                    String password = resultSet.getString("password");
                    String rol = resultSet.getString("rol");
                    Utilizator utilizator = new Utilizator(nume, prenume, email, password);
                    utilizator.setId_utilizator(resultSet.getLong("id_utilizator"));
                    utilizatori.add(utilizator);
                    logger.traceExit(utilizator);
                }
            }
        } catch (SQLException e) {
            logger.error(e);
            System.err.println("Error DB " + e);
        }
        return utilizatori;
    }

    @Override
    public int size() {
        return 0;
    }


    @Override
    public Utilizator login(String email, String parola) {
        logger.info("Logging in user with email: {} and password: {}", email, parola);
        Connection connection = jdbcUtils.getConnection();
        try (PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM Utilizator WHERE email = ? AND password = ?")) {
            preparedStatement.setString(1, email);
            preparedStatement.setString(2, parola);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    String nume = resultSet.getString("nume");
                    String prenume = resultSet.getString("prenume");
                    String rol = resultSet.getString("rol");
                    Utilizator utilizator = new Utilizator(nume, prenume, email, parola);
                    utilizator.setId_utilizator(resultSet.getLong("id_utilizator"));
                    logger.traceExit(utilizator);
                    return utilizator;
                }
            }
        } catch (SQLException e) {
            logger.error(e);
            System.err.println("Error DB " + e);
        }
        return null;
    }

    @Override
    public Utilizator findByEmail(String email) {
        logger.info("Finding User by email: {}", email);
        Connection connection = jdbcUtils.getConnection();
        try (PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM Utilizator WHERE email = ?")) {
            preparedStatement.setString(1, email);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    String nume = resultSet.getString("nume");
                    String prenume = resultSet.getString("prenume");
                    String password = resultSet.getString("password");
                    String rol = resultSet.getString("rol");
                    Utilizator utilizator = new Utilizator(nume, prenume, email, password);
                    utilizator.setId_utilizator(resultSet.getLong("id_utilizator"));
                    logger.traceExit(utilizator);
                    return utilizator;
                }
            }
        } catch (SQLException e) {
            logger.error(e);
            System.err.println("Error DB " + e);
        }
        return null;
    }
}
