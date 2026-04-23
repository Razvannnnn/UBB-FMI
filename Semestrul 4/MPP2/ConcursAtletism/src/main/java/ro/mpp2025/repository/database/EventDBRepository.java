package ro.mpp2025.repository.database;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ro.mpp2025.domain.Event;
import ro.mpp2025.repository.EventRepository;
import ro.mpp2025.utils.JdbcUtils;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class EventDBRepository implements EventRepository {
    private JdbcUtils dbUtils;

    private static final Logger logger = LogManager.getLogger();

    public EventDBRepository(Properties props) {
        logger.info("Initializing EventDBRepository with properties: {} ", props);
        dbUtils = new JdbcUtils(props);
    }

    @Override
    public Event findOne(Integer id) {
        logger.info("Finding one event wiht id: {}", id);
        Connection conn = dbUtils.getConnection();
        try (PreparedStatement preStmt = conn.prepareStatement("select * from event where id = ?")) {
            preStmt.setInt(1, id);
            try (ResultSet result = preStmt.executeQuery()) {
                if (result.next()) {
                    String name = result.getString("name");
                    int distance = result.getInt("distance");
                    Event event = new Event(name, distance);
                    event.setId(id);
                    logger.traceExit(event);
                    return event;
                }
            }
        } catch (SQLException e) {
            logger.error(e);
            System.err.println("Error DB " + e);
        }
        logger.traceExit("No event found with id: {}", id);
        return null;
    }

    @Override
    public Iterable<Event> findAll() {
        logger.traceEntry("finding all Events");
        Connection conn = dbUtils.getConnection();
        List<Event> events = new ArrayList<>();
        try (PreparedStatement preStmt = conn.prepareStatement("select * from event")) {
            try (ResultSet result = preStmt.executeQuery()) {
                while (result.next()) {
                    Integer id = result.getInt("id");
                    String name = result.getString("name");
                    int distance = result.getInt("distance");
                    Event event = new Event(name, distance);
                    event.setId(id);
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

    @Override
    public Event save(Event entity) {
        return null;
    }

    @Override
    public Event delete(Integer id) {
        return null;
    }

    @Override
    public Event update(Event entity) {
        return null;
    }
}
