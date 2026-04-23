package ro.mpp2025.repository.database;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ro.mpp2025.domain.AgeGroup;
import ro.mpp2025.repository.AgeGroupRepository;
import ro.mpp2025.utils.JdbcUtils;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class AgeGroupDBRepository implements AgeGroupRepository {
    private JdbcUtils dbUtils;

    private static final Logger logger = LogManager.getLogger();

    public AgeGroupDBRepository(Properties props) {
        logger.info("Initializing AgeGroupDBRepository with properties: {} ", props);
        dbUtils = new JdbcUtils(props);
    }

    @Override
    public AgeGroup findOne(Integer id) {
        logger.info("Finding AgeGroup by id: {}", id);
        Connection conn = dbUtils.getConnection();
        try (PreparedStatement preStmt = conn.prepareStatement("select * from age_group where id = ?")) {
            preStmt.setInt(1, id);
            try (ResultSet result = preStmt.executeQuery()) {
                if (result.next()) {
                    String name = result.getString("name");
                    int minAge = result.getInt("min_age");
                    int maxAge = result.getInt("max_age");
                    AgeGroup ageGroup = new AgeGroup(name, minAge, maxAge);
                    ageGroup.setId(id);
                    logger.traceExit(ageGroup);
                    return ageGroup;
                }
            }
        } catch (SQLException e) {
            logger.error(e);
            System.err.println("Error DB " + e);
        }
        logger.traceExit("No AgeGroup found with id: {}", id);
        return null;
    }

    @Override
    public Iterable<AgeGroup> findAll() {
        logger.traceEntry("finding all AgeGroups");
        Connection conn = dbUtils.getConnection();
        List<AgeGroup> ageGroups = new ArrayList<>();
        try (PreparedStatement preStmt = conn.prepareStatement("select * from age_group")) {
            try (ResultSet result = preStmt.executeQuery()) {
                while (result.next()) {
                    Integer id = result.getInt("id");
                    String name = result.getString("name");
                    int minAge = result.getInt("min_age");
                    int maxAge = result.getInt("max_age");
                    AgeGroup ageGroup = new AgeGroup(name, minAge, maxAge);
                    ageGroup.setId(id);
                    ageGroups.add(ageGroup);
                }
            }
        } catch (SQLException e) {
            logger.error(e);
            System.err.println("Error DB " + e);
        }
        logger.traceExit(ageGroups);
        return ageGroups;
    }

    @Override
    public AgeGroup save(AgeGroup entity) {
        return null;
    }

    @Override
    public AgeGroup delete(Integer id) {
        return null;
    }

    @Override
    public AgeGroup update(AgeGroup entity) {
        return null;
    }
}
