package problema8.persistence.jdbc;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import problema8.model.Child;
import problema8.persistence.irepository.IRepoChild;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Properties;

public class RepoChild implements IRepoChild {
    private JdbcUtils jdbcUtils;

    private static final Logger logger = LogManager.getLogger();

    public RepoChild(Properties properties) {
        logger.info("Initializing ChildDBRepository with properties: {} ", properties);
        jdbcUtils = new JdbcUtils(properties);
    }

    @Override
    public Child findOne(Long aLong) {
        logger.info("Finding Child by id: {}", aLong);
        Connection connection = jdbcUtils.getConnection();
        try (PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM child WHERE child_id= ?")) {
            preparedStatement.setLong(1, aLong);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    String name = resultSet.getString("name");
                    String CNP = resultSet.getString("CNP");
                    Child child = new Child(aLong, name, CNP);
                    logger.traceExit(child);
                    return child;
                }
            }
        } catch (SQLException e) {
            logger.error(e);
            System.err.println("Error DB " + e);
        }
        logger.traceExit("No Child found with id: {}", aLong);
        return null;
    }

    @Override
    public Iterable<Child> findAll() {
        logger.traceEntry("finding all Children");
        Connection connection = jdbcUtils.getConnection();
        ArrayList<Child> children = new ArrayList<>();
        try (PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM child");
             ResultSet resultSet = preparedStatement.executeQuery();
        ) {
            while (resultSet.next()) {
                Long id = resultSet.getLong("child_id");
                String name = resultSet.getString("name");
                String CNP = resultSet.getString("CNP");
                Child child = new Child(id, name, CNP);
                children.add(child);
            }
        } catch (SQLException e) {
            logger.error(e);
            System.err.println("Error DB " + e);
        }
        logger.traceExit(children);
        return children;
    }

    @Override
    public void save(Child entity) {
        logger.info("Saving Child: {}", entity);
        Connection connection = jdbcUtils.getConnection();
        try (PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO child (name, CNP) VALUES (?, ?)");
        ) {
            preparedStatement.setString(1, entity.getName());
            preparedStatement.setString(2, entity.getCNP());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            logger.error(e);
            System.err.println("Error DB " + e);
        }
        logger.traceExit("Child saved successfully");
    }

    @Override
    public void delete(Long aLong) {
        logger.info("Deleting Child with id: {}", aLong);
        Connection connection = jdbcUtils.getConnection();
        try (PreparedStatement preparedStatement = connection.prepareStatement("DELETE FROM child WHERE child_id = ?");
        ) {
            preparedStatement.setLong(1, aLong);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            logger.error(e);
            System.err.println("Error DB " + e);
        }
        logger.traceExit("Child deleted successfully");
    }

    @Override
    public void update(Child entity) {
        logger.info("Updating Child: {}", entity);
        Connection connection = jdbcUtils.getConnection();
        try (PreparedStatement preparedStatement = connection.prepareStatement("UPDATE child SET name = ?, CNP = ? WHERE child_id = ?");
        ) {
            preparedStatement.setString(1, entity.getName());
            preparedStatement.setString(2, entity.getCNP());
            preparedStatement.setLong(3, entity.getId());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            logger.error(e);
            System.err.println("Error DB " + e);
        }
        logger.traceExit("Child updated successfully");
    }

    public Child findOneCNP(String cnp) {
        logger.info("Finding Child by CNP: {}", cnp);
        Connection connection = jdbcUtils.getConnection();
        try (PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM child WHERE CNP= ?")) {
            preparedStatement.setString(1, cnp);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    Long id = resultSet.getLong("child_id");
                    String name = resultSet.getString("name");
                    Child child = new Child(id, name, cnp);
                    logger.traceExit(child);
                    return child;
                }
            }
        } catch (SQLException e) {
            logger.error(e);
            System.err.println("Error DB " + e);
        }
        logger.traceExit("No Child found with CNP: {}", cnp);
        return null;
    }

    public Iterable<Child> findByEvent(Long id) {
        logger.info("Finding Children by Event id: {}", id);
        Connection connection = jdbcUtils.getConnection();
        ArrayList<Child> children = new ArrayList<>();
        try (PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM child WHERE child_id IN (SELECT child_id FROM enrollment WHERE event_id = ?)")) {
            preparedStatement.setLong(1, id);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    Long idChild = resultSet.getLong("child_id");
                    String name = resultSet.getString("name");
                    String CNP = resultSet.getString("CNP");
                    Child child = new Child(idChild, name, CNP);
                    children.add(child);
                }
            }
        } catch (SQLException e) {
            logger.error(e);
            System.err.println("Error DB " + e);
        }
        logger.traceExit(children);
        return children;
    }
}
