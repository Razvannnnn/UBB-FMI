package ro.mpp2025.problema8_fx.Repository;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ro.mpp2025.problema8_fx.Domain.Event;
import ro.mpp2025.problema8_fx.Utils.JdbcUtils;

import java.sql.*;
import java.util.ArrayList;
import java.util.Properties;

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
}
