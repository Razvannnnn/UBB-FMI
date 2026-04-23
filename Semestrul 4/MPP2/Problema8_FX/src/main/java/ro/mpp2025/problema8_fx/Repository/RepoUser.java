package ro.mpp2025.problema8_fx.Repository;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ro.mpp2025.problema8_fx.Domain.User;
import ro.mpp2025.problema8_fx.Utils.JdbcUtils;

import java.sql.*;
import java.util.ArrayList;
import java.util.Properties;

public class RepoUser implements IRepoUser {
    private JdbcUtils jdbcUtils;

    private static final Logger logger = LogManager.getLogger();

    public RepoUser(Properties properties) {
        logger.info("Initializing UserDBRepository with properties: {} ", properties);
        jdbcUtils = new JdbcUtils(properties);
    }

    @Override
    public User findOne(Long aLong) {
        logger.info("Finding User by id: {}", aLong);
        Connection connection = jdbcUtils.getConnection();
        try (PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM user WHERE user_id = ?")) {
            preparedStatement.setLong(1, aLong);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    String username = resultSet.getString("username");
                    String password = resultSet.getString("password");
                    User user = new User(aLong, username, password);
                    logger.traceExit(user);
                    return user;
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
    public Iterable<User> findAll() {
        logger.traceEntry("finding all Users");
        Connection connection = jdbcUtils.getConnection();
        ArrayList<User> users = new ArrayList<>();
        try (PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM user");
             ResultSet resultSet = preparedStatement.executeQuery();
        ) {
            while (resultSet.next()) {
                Long id = resultSet.getLong("user_id");
                String username = resultSet.getString("username");
                String password = resultSet.getString("password");
                User user = new User(id, username, password);
                users.add(user);
            }
        } catch (SQLException e) {
            logger.error(e);
            System.err.println("Error DB " + e);
        }
        logger.traceExit(users);
        return users;
    }

    @Override
    public void save(User entity) {
        logger.info("Saving User: {}", entity);
        Connection connection = jdbcUtils.getConnection();
        try (PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO user (username, password) VALUES (?, ?)")) {
            preparedStatement.setString(1, entity.getUsername());
            preparedStatement.setString(2, entity.getPassword());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            logger.error(e);
            System.err.println("Error DB " + e);
        }
    }

    @Override
    public void delete(Long aLong) {
        logger.info("Deleting User with id: {}", aLong);
        Connection connection = jdbcUtils.getConnection();
        try (PreparedStatement preparedStatement = connection.prepareStatement("DELETE FROM user WHERE user_id = ?")) {
            preparedStatement.setLong(1, aLong);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            logger.error(e);
            System.err.println("Error DB " + e);
        }
    }

    @Override
    public void update(User entity) {
        logger.info("Updating User: {}", entity);
        Connection connection = jdbcUtils.getConnection();
        try (PreparedStatement preparedStatement = connection.prepareStatement("UPDATE user SET username = ?, password = ? WHERE user_id = ?")) {
            preparedStatement.setString(1, entity.getUsername());
            preparedStatement.setString(2, entity.getPassword());
            preparedStatement.setLong(3, entity.getId());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            logger.error(e);
            System.err.println("Error DB " + e);
        }
    }

    public User login(String username, String password) {
        logger.info("Logging in User with username: {}", username);
        Connection connection = jdbcUtils.getConnection();
        try (PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM user WHERE username = ? AND password = ?")) {
            preparedStatement.setString(1, username);
            preparedStatement.setString(2, password);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    logger.traceExit("User logged in successfully");
                    return new User(resultSet.getLong("user_id"), username, password);
                }
            }
        } catch (SQLException e) {
            logger.error(e);
            System.err.println("Error DB " + e);
        }
        return null;
    }
}
