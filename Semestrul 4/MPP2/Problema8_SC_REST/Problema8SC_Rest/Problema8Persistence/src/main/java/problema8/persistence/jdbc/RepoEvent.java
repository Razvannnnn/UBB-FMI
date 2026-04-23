package problema8.persistence.jdbc;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;
import problema8.model.Event;
import problema8.persistence.RepositoryException;
import problema8.persistence.irepository.IRepoEvent;

import java.sql.*;
import java.util.ArrayList;
import java.util.Properties;

@Repository
public class RepoEvent implements IRepoEvent {
    private JdbcUtils jdbcUtils;

    private static final Logger logger = LogManager.getLogger();

    public RepoEvent(Properties properties) {
        logger.info("Initializing EventDBRepository with properties: {} ", properties);
        jdbcUtils = new JdbcUtils(properties);
    }

    @Override
    public Event findOne(Long aLong) {
        logger.info("Finding Event by id: {}", aLong);
        Connection connection = jdbcUtils.getConnection();
        try (PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM event WHERE event_id = ?")) {
            preparedStatement.setLong(1, aLong);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    String name = resultSet.getString("name");
                    Integer distance = resultSet.getInt("distance");
                    Long id = resultSet.getLong("age_group_id");
                    Event event = new Event(aLong, name, distance, id);
                    logger.traceExit(event);
                    return event;
                }
            }
        } catch (SQLException e) {
            logger.error(e);
            System.err.println("Error DB " + e);
        }
        logger.traceExit("No Event found with id: {}", aLong);
        return null;
    }

    @Override
    public Iterable<Event> findAll() {
        logger.traceEntry("finding all Events");
        Connection connection = jdbcUtils.getConnection();
        ArrayList<Event> events = new ArrayList<>();
        try (PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM event");
             ResultSet resultSet = preparedStatement.executeQuery();
        ) {
            while (resultSet.next()) {
                Long id = resultSet.getLong("event_id");
                String name = resultSet.getString("name");
                Integer distance = resultSet.getInt("distance");
                Long ag_id = resultSet.getLong("age_group_id");
                Event event = new Event(id, name, distance, ag_id);
                events.add(event);
            }
        } catch (SQLException e) {
            logger.error(e);
            System.err.println("Error DB " + e);
        }
        logger.traceExit("No Events found");
        return events;
    }

    @Override
    public void save(Event entity) {
        logger.info("Saving Event: {}", entity);
        Connection connection = jdbcUtils.getConnection();
        try (PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO event(name, distance, age_group_id) VALUES (?, ?)")) {
            preparedStatement.setString(1, entity.getName());
            preparedStatement.setInt(2, entity.getDistance());
            preparedStatement.setLong(3, entity.getAgeGroupId());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            logger.error(e);
            System.err.println("Error DB " + e);
        }
    }

    public Event saveReturnEvent(Event entity) {
        logger.info("Saving Event and returning it: {}", entity);
        Connection connection = jdbcUtils.getConnection();

        try {
            String insertSql = "INSERT INTO event(name, distance, age_group_id) VALUES (?, ?, ?)";
            try (PreparedStatement insertStmt = connection.prepareStatement(insertSql)) {
                insertStmt.setString(1, entity.getName());
                insertStmt.setInt(2, entity.getDistance());
                insertStmt.setLong(3, entity.getAgeGroupId());
                insertStmt.executeUpdate();
            }

            try (Statement idStmt = connection.createStatement();
                 ResultSet rs = idStmt.executeQuery("SELECT last_insert_rowid()")) {
                if (rs.next()) {
                    long id = rs.getLong(1);
                    entity.setId(id);
                    logger.info("Inserted Event ID: {}", id);
                } else {
                    throw new SQLException("Failed to retrieve last insert ID.");
                }
            }

        } catch (SQLException e) {
            logger.error("Error saving Event", e);
            throw new RuntimeException("Database error: " + e.getMessage(), e);
        }

        return entity;
    }


    @Override
    public void delete(Long aLong) {
        logger.info("Deleting Event with id: {}", aLong);
        Connection connection = jdbcUtils.getConnection();
        try (PreparedStatement preparedStatement = connection.prepareStatement("DELETE FROM event WHERE event_id = ?")) {
            preparedStatement.setLong(1, aLong);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            logger.error(e);
            System.err.println("Error DB " + e);
        }
    }

    @Override
    public void update(Event entity) {
        logger.info("Updating Event: {}", entity);
        Connection connection = jdbcUtils.getConnection();
        try (PreparedStatement preparedStatement = connection.prepareStatement("UPDATE event SET name = ?, distance = ?, age_group_id = ? WHERE event_id = ?")) {
            preparedStatement.setString(1, entity.getName());
            preparedStatement.setInt(2, entity.getDistance());
            preparedStatement.setLong(3, entity.getAgeGroupId());
            preparedStatement.setLong(4, entity.getId());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            logger.error(e);
            System.err.println("Error DB " + e);
        }
    }

    public Iterable<Event> findByAgeGroup(Long ageGroupId) {
        logger.info("Finding Events by AgeGroup id: {}", ageGroupId);
        Connection connection = jdbcUtils.getConnection();
        ArrayList<Event> events = new ArrayList<>();
        try (PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM event WHERE age_group_id = ?")) {
            preparedStatement.setLong(1, ageGroupId);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    Long id = resultSet.getLong("event_id");
                    String name = resultSet.getString("name");
                    Integer distance = resultSet.getInt("distance");
                    Event event = new Event(id, name, distance, ageGroupId);
                    events.add(event);
                }
            }
        } catch (SQLException e) {
            logger.error(e);
            System.err.println("Error DB " + e);
        }
        logger.traceExit(events);
        return events;
    }

    public void deleteByName(String name) {
        logger.info("Deleting Event by name: {}", name);
        Connection connection = jdbcUtils.getConnection();
        try (PreparedStatement preparedStatement = connection.prepareStatement("DELETE FROM event WHERE name = ?")) {
            preparedStatement.setString(1, name);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            logger.error(e);
            System.err.println("Error DB " + e);
        }
    }
}
