package ro.mpp2025.repository.database;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ro.mpp2025.domain.Child;
import ro.mpp2025.repository.ChildRepository;
import ro.mpp2025.utils.JdbcUtils;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class ChildDBRepository implements ChildRepository {
    private JdbcUtils dbUtils;

    private static final Logger logger= LogManager.getLogger();

    public ChildDBRepository(Properties props) {
        logger.info("Initializing ChildDBRepository with properties: {} ", props);
        dbUtils = new JdbcUtils(props);
    }

    @Override
    public Child findOne(Integer id) {
        logger.info("Finding Child with id: {}", id);
        Connection conn = dbUtils.getConnection();
        try (PreparedStatement preStmt = conn.prepareStatement("select * from child where id = ?")) {
            preStmt.setInt(1, id);
            try (ResultSet result = preStmt.executeQuery()) {
                if (result.next()) {
                    String name = result.getString("name");
                    String cnp = result.getString("cnp");
                    Child child = new Child(name, cnp);
                    child.setId(id);
                    logger.traceExit(child);
                    return child;
                }
            }
        } catch (SQLException e) {
            logger.error(e);
            System.err.println("Error DB " + e);
        }
        logger.traceExit("No child found with id: {}", id);
        return null;
    }

    @Override
    public Iterable<Child> findAll() {
        logger.traceEntry("finding all Children");
        Connection conn = dbUtils.getConnection();
        List<Child> children = new ArrayList<>();
        try (PreparedStatement preStmt = conn.prepareStatement("select * from child")) {
            try (ResultSet result = preStmt.executeQuery()) {
                while (result.next()) {
                    Integer id = result.getInt("id");
                    String name = result.getString("name");
                    String cnp = result.getString("cnp");
                    Child child = new Child(name, cnp);
                    child.setId(id);
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

    @Override
    public Child save(Child child) {
        logger.traceEntry("saving child ", child);
        String query = "insert into children (name, cnp) VALUES (?, ?)";
        Connection con = dbUtils.getConnection();
        try (PreparedStatement preStmt = con.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
            preStmt.setString(1, child.getName());
            preStmt.setString(2, child.getCNP());
            int result = preStmt.executeUpdate();
            if (result > 0) {
                ResultSet generatedKeys = preStmt.getGeneratedKeys();
                if (generatedKeys.next()) {
                    child.setId(generatedKeys.getInt(1));
                    logger.traceExit();
                    return child;
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
    public Child delete(Integer integer) {
        return null;
    }

    @Override
    public Child update(Child entity) {
        return null;
    }
}
