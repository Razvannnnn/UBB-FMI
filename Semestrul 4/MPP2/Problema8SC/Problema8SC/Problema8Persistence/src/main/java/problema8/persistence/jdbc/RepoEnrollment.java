package problema8.persistence.jdbc;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import problema8.model.Child;
import problema8.model.Enrollment;
import problema8.model.Event;
import problema8.persistence.irepository.IRepoEnrollment;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class RepoEnrollment implements IRepoEnrollment {
    private JdbcUtils dbUtils;

    private static final Logger logger = LogManager.getLogger();

    public RepoEnrollment(Properties props) {
        logger.info("Initializing RepoEnrollment with properties: {}", props);
        dbUtils = new JdbcUtils(props);
    }

    @Override
    public Enrollment findOne(Long aLong) {
        logger.info("Finding Enrollment with id: {}", aLong);
        Connection conn = dbUtils.getConnection();
        String query = "SELECT e.id, e.child_id, e.event_id, c.name AS child_name, c.cnp AS child_cnp, " +
                "ev.name AS event_name, ev.distance AS event_distance, ev.age_group_id AS event_age_group_id " +
                "FROM enrollment e " +
                "JOIN child c ON e.child_id = c.id " +
                "JOIN event ev ON e.event_id = ev.id " +
                "WHERE e.id = ?";
        try (PreparedStatement preStmt = conn.prepareStatement(query)) {
            preStmt.setLong(1, aLong);
            try (ResultSet result = preStmt.executeQuery()) {
                if (result.next()) {
                    Long childId = result.getLong("child_id");
                    String childName = result.getString("child_name");
                    String childCnp = result.getString("child_cnp");
                    Child child = new Child(childId, childName, childCnp);

                    Long eventId = result.getLong("event_id");
                    String eventName = result.getString("event_name");
                    int eventDistance = result.getInt("event_distance");
                    Long ageGroupId = result.getLong("event_age_group_id");
                    Event event = new Event(eventId, eventName, eventDistance, ageGroupId);
                    event.setId(eventId);

                    Enrollment enrollment = new Enrollment(aLong, child, event);
                    logger.traceExit(enrollment);
                    return enrollment;
                }
            }
        } catch (SQLException e) {
            logger.error(e);
            System.err.println("Error DB " + e);
        }
        logger.traceExit("No enrollment found with id: {}", aLong);
        return null;
    }

    @Override
    public Iterable<Enrollment> findAll() {
        logger.traceEntry("finding all Enrollments");
        Connection conn = dbUtils.getConnection();
        List<Enrollment> enrollments = new ArrayList<>();
        String query = "SELECT e.enrollment_id, e.child_id, e.event_id, c.name AS child_name, c.cnp AS child_cnp, " +
                "ev.name AS event_name, ev.distance AS event_distance, ev.age_group_id AS event_age_group_id " +
                "FROM enrollment e " +
                "JOIN child c ON e.child_id = c.child_id " +
                "JOIN event ev ON e.event_id = ev.event_id";
        try (PreparedStatement preStmt = conn.prepareStatement(query)) {
            try (ResultSet result = preStmt.executeQuery()) {
                while (result.next()) {
                    Long id = result.getLong("enrollment_id");

                    Long childId = result.getLong("child_id");
                    String childName = result.getString("child_name");
                    String childCnp = result.getString("child_cnp");
                    Child child = new Child(childId, childName, childCnp);

                    Long eventId = result.getLong("event_id");
                    String eventName = result.getString("event_name");
                    int eventDistance = result.getInt("event_distance");
                    Long ageGroupId = result.getLong("event_age_group_id");
                    Event event = new Event(eventId, eventName, eventDistance, ageGroupId);

                    Enrollment enrollment = new Enrollment(id, child, event);
                    enrollments.add(enrollment);
                }
            }
        } catch (SQLException e) {
            logger.error(e);
            System.err.println("Error DB " + e);
        }
        logger.traceExit(enrollments);
        return enrollments;
    }

    @Override
    public void save(Enrollment entity) {
        logger.info("Saving Enrollment: {}", entity);
        Connection conn = dbUtils.getConnection();
        String query = "INSERT INTO enrollment (child_id, event_id) VALUES (?, ?)";
        try (PreparedStatement preStmt = conn.prepareStatement(query)) {
            preStmt.setLong(1, entity.getChild().getId());
            preStmt.setLong(2, entity.getEvent().getId());
            preStmt.executeUpdate();
        } catch (SQLException e) {
            logger.error(e);
            System.err.println("Error DB " + e);
        }
    }

    @Override
    public void delete(Long aLong) {
        logger.info("Deleting Enrollment with id: {}", aLong);
        Connection conn = dbUtils.getConnection();
        String query = "DELETE FROM enrollment WHERE enrollment_id = ?";
        try (PreparedStatement preStmt = conn.prepareStatement(query)) {
            preStmt.setLong(1, aLong);
            preStmt.executeUpdate();
        } catch (SQLException e) {
            logger.error(e);
            System.err.println("Error DB " + e);
        }
    }

    @Override
    public void update(Enrollment entity) {
        logger.info("Updating Enrollment: {}", entity);
        Connection conn = dbUtils.getConnection();
        String query = "UPDATE enrollment SET child_id = ?, event_id = ? WHERE enrollment_id = ?";
        try (PreparedStatement preStmt = conn.prepareStatement(query)) {
            preStmt.setLong(1, entity.getChild().getId());
            preStmt.setLong(2, entity.getEvent().getId());
            preStmt.setLong(3, entity.getId());
            preStmt.executeUpdate();
        } catch (SQLException e) {
            logger.error(e);
            System.err.println("Error DB " + e);
            }

    }

    public int getNumberOfEvents(Long id) {
        logger.info("Getting number of events for child with id: {}", id);
        Connection conn = dbUtils.getConnection();
        String query = "SELECT COUNT(*) AS event_count FROM enrollment WHERE child_id = ?";
        try (PreparedStatement preStmt = conn.prepareStatement(query)) {
            preStmt.setLong(1, id);
            try (ResultSet result = preStmt.executeQuery()) {
                if (result.next()) {
                    return result.getInt("event_count");
                }
            }
        } catch (SQLException e) {
            logger.error(e);
            System.err.println("Error DB " + e);
        }
        return 0;
    }
}
