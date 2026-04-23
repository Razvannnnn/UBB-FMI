package ro.mpp2025.repository.database;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ro.mpp2025.domain.User;
import ro.mpp2025.repository.UserRepository;
import ro.mpp2025.utils.JdbcUtils;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class UserDBRepository implements UserRepository {
    private JdbcUtils dbUtils;

    private static final Logger logger= LogManager.getLogger();

    public UserDBRepository(Properties props) {
        logger.info("Initializing UserDBRepository with properties: {} ", props);
        dbUtils = new JdbcUtils(props);
    }

    @Override
    public User findOne(Integer id) {
        logger.info("Finding User with id: {}", id);
        Connection conn = dbUtils.getConnection();
        try (PreparedStatement preStmt = conn.prepareStatement("select * from user where id = ?")) {
            preStmt.setInt(1, id);
            try (ResultSet result = preStmt.executeQuery()) {
                if (result.next()) {
                    String username = result.getString("username");
                    String password = result.getString("password");
                    User user = new User(username, password);
                    user.setId(id);
                    logger.traceExit(user);
                    return user;
                }
            }
        } catch (SQLException e) {
            logger.error(e);
            System.err.println("Error DB " + e);
        }
        logger.traceExit("No user found with id: {}", id);
        return null;
    }

    @Override
    public Iterable<User> findAll() {
        logger.traceEntry("finding all Users");
        Connection conn = dbUtils.getConnection();
        List<User> users = new ArrayList<>();
        try (PreparedStatement preStmt = conn.prepareStatement("select * from user")) {
            try (ResultSet result = preStmt.executeQuery()) {
                while (result.next()) {
                    Integer id = result.getInt("id");
                    String username = result.getString("username");
                    String password = result.getString("password");
                    User user = new User(username, password);
                    user.setId(id);
                    users.add(user);
                }
            }
        } catch (SQLException e) {
            logger.error(e);
            System.err.println("Error DB " + e);
        }
        logger.traceExit(users);
        return users;
    }

    @Override
    public User save(User user) {
        logger.traceEntry("saving User ", user);
        String query = "insert into user (username, password) VALUES (?, ?)";
        Connection con = dbUtils.getConnection();
        try (PreparedStatement preStmt = con.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
            preStmt.setString(1, user.getUsername());
            preStmt.setString(2, user.getPassword());
            int result = preStmt.executeUpdate();
            if (result > 0) {
                ResultSet generatedKeys = preStmt.getGeneratedKeys();
                if (generatedKeys.next()) {
                    user.setId(generatedKeys.getInt(1));
                    logger.traceExit();
                    return user;
                }
            }
        } catch (SQLException e) {
            logger.error(e);
            System.err.println("Error DB " + e);
        }
        logger.traceExit();
        return null;
    }

    @Override
    public User delete(Integer integer) {
        return null;
    }

    @Override
    public User update(User entity) {
        return null;
    }
}
