package ro.mpp2025.repository.database;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ro.mpp2025.domain.Child;
import ro.mpp2025.domain.Enrollment;
import ro.mpp2025.domain.Event;
import ro.mpp2025.repository.EnrollmentRepository;
import ro.mpp2025.utils.JdbcUtils;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class EnrollmentDBRepository implements EnrollmentRepository {
    private JdbcUtils dbUtils;
    private static final Logger logger = LogManager.getLogger();

    public EnrollmentDBRepository(Properties props) {
        logger.info("Initializing EnrollmentDBRepository with properties: {}", props);
        dbUtils = new JdbcUtils(props);
    }

    @Override
    public Enrollment findOne(Integer id) {
        logger.info("Finding Enrollment with id: {}", id);
        Connection conn = dbUtils.getConnection();
        String query = "SELECT e.id, e.child_id, e.event_id, c.name AS child_name, c.cnp AS child_cnp, " +
                "ev.name AS event_name, ev.distance AS event_distance " +
                "FROM enrollment e " +
                "JOIN child c ON e.child_id = c.id " +
                "JOIN event ev ON e.event_id = ev.id " +
                "WHERE e.id = ?";
        try (PreparedStatement preStmt = conn.prepareStatement(query)) {
            preStmt.setInt(1, id);
            try (ResultSet result = preStmt.executeQuery()) {
                if (result.next()) {
                    // Construct Child object
                    Integer childId = result.getInt("child_id");
                    String childName = result.getString("child_name");
                    String childCnp = result.getString("child_cnp");
                    Child child = new Child(childName, childCnp);
                    child.setId(childId);

                    // Construct Event object
                    Integer eventId = result.getInt("event_id");
                    String eventName = result.getString("event_name");
                    int eventDistance = result.getInt("event_distance");
                    Event event = new Event(eventName, eventDistance);
                    event.setId(eventId);

                    // Construct Enrollment object
                    Enrollment enrollment = new Enrollment(child, event);
                    enrollment.setId(id);
                    logger.traceExit(enrollment);
                    return enrollment;
                }
            }
        } catch (SQLException e) {
            logger.error(e);
            System.err.println("Error DB " + e);
        }
        logger.traceExit("No enrollment found with id: {}", id);
        return null;
    }


    @Override
    public Iterable<Enrollment> findAll() {
        logger.traceEntry("finding all Enrollments");
        Connection conn = dbUtils.getConnection();
        List<Enrollment> enrollments = new ArrayList<>();
        String query = "SELECT e.id, e.child_id, e.event_id, c.name AS child_name, c.cnp AS child_cnp, " +
                "ev.name AS event_name, ev.distance AS event_distance " +
                "FROM enrollment e " +
                "JOIN child c ON e.child_id = c.id " +
                "JOIN event ev ON e.event_id = ev.id";
        try (PreparedStatement preStmt = conn.prepareStatement(query)) {
            try (ResultSet result = preStmt.executeQuery()) {
                while (result.next()) {
                    Integer id = result.getInt("id");

                    // Construct Child object
                    Integer childId = result.getInt("child_id");
                    String childName = result.getString("child_name");
                    String childCnp = result.getString("child_cnp");
                    Child child = new Child(childName, childCnp);
                    child.setId(childId);

                    // Construct Event object
                    Integer eventId = result.getInt("event_id");
                    String eventName = result.getString("event_name");
                    int eventDistance = result.getInt("event_distance");
                    Event event = new Event(eventName, eventDistance);
                    event.setId(eventId);

                    // Construct Enrollment object
                    Enrollment enrollment = new Enrollment(child, event);
                    enrollment.setId(id);
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
    public Enrollment save(Enrollment enrollment) {
        logger.traceEntry("saving Enrollment ", enrollment);
        String query = "INSERT INTO enrollment (child_id, event_id) VALUES (?, ?)";
        Connection con = dbUtils.getConnection();
        try (PreparedStatement preStmt = con.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
            preStmt.setInt(1, enrollment.getChild().getId());
            preStmt.setInt(2, enrollment.getEvent().getId());
            int result = preStmt.executeUpdate();
            if (result > 0) {
                ResultSet generatedKeys = preStmt.getGeneratedKeys();
                if (generatedKeys.next()) {
                    enrollment.setId(generatedKeys.getInt(1));
                    logger.traceExit();
                    return enrollment;
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
    public Enrollment delete(Integer id) {
        logger.info("Deleting Enrollment with id: {}", id);
        Connection conn = dbUtils.getConnection();
        Enrollment enrollment = findOne(id);
        if (enrollment == null) {
            logger.traceExit("No enrollment found with id: {}", id);
            return null;
        }
        try (PreparedStatement preStmt = conn.prepareStatement("DELETE FROM enrollment WHERE id = ?")) {
            preStmt.setInt(1, id);
            int result = preStmt.executeUpdate();
            if (result > 0) {
                logger.traceExit(enrollment);
                return enrollment;
            }
        } catch (SQLException e) {
            logger.error(e);
            System.err.println("Error DB " + e);
        }
        logger.traceExit();
        return null;
    }


    @Override
    public Enrollment update(Enrollment entity) {
        return null;
    }
}
