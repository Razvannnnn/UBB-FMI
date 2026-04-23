package problema8.persistence.jdbc;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import problema8.model.AgeGroup;
import problema8.persistence.irepository.IRepoAgeGroup;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Properties;

public class RepoAgeGroup implements IRepoAgeGroup {

    private JdbcUtils jdbcUtils;
    private static final Logger logger = LogManager.getLogger();

    public RepoAgeGroup(Properties properties) {
        logger.info("Initializing AgeGroupDBRepository with properties: {} ", properties);
        jdbcUtils = new JdbcUtils(properties);
    }

    @Override
    public AgeGroup findOne(Long aLong) {
        logger.info("Finding AgeGroup by id: {}", aLong);
        Connection connection = jdbcUtils.getConnection();
        try (PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM age_group WHERE age_group_id = ?")) {
            preparedStatement.setLong(1, aLong);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    String name = resultSet.getString("name");
                    Integer minAge = resultSet.getInt("min_age");
                    Integer maxAge = resultSet.getInt("max_age");
                    AgeGroup ageGroup = new AgeGroup(aLong, name, minAge, maxAge);
                    logger.traceExit(ageGroup);
                    return ageGroup;
                }
            }
        } catch (SQLException e) {
            logger.error(e);
            System.err.println("Error DB " + e);
        }
        logger.traceExit("No AgeGroup found with id: {}", aLong);
        return null;
    }

    @Override
    public Iterable<AgeGroup> findAll() {
        logger.traceEntry("finding all AgeGroups");
        Connection connection = jdbcUtils.getConnection();
        ArrayList<AgeGroup> ageGroups = new ArrayList<>();
        try (PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM age_group");
             ResultSet resultSet = preparedStatement.executeQuery();
        ) {
            while (resultSet.next()) {
                Long id = resultSet.getLong("age_group_id");
                String name = resultSet.getString("name");
                Integer minAge = resultSet.getInt("min_age");
                Integer maxAge = resultSet.getInt("max_age");
                AgeGroup ageGroup = new AgeGroup(id, name, minAge, maxAge);
                ageGroups.add(ageGroup);
            }
        } catch (SQLException e) {
            logger.error(e);
            System.err.println("Error DB " + e);
        }
        logger.traceExit(ageGroups);
        return ageGroups;
    }

    @Override
    public void save(AgeGroup entity) {
        logger.info("Saving AgeGroup: {}", entity);
        Connection connection = jdbcUtils.getConnection();
        try (PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO age_group(name, min_age, max_age) VALUES (?, ?, ?)");
        ) {
            preparedStatement.setString(1, entity.getName());
            preparedStatement.setInt(2, entity.getMinAge());
            preparedStatement.setInt(3, entity.getMaxAge());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            logger.error(e);
            System.err.println("Error DB " + e);
        }
    }

    @Override
    public void delete(Long aLong) {
        logger.info("Deleting AgeGroup with id: {}", aLong);
        Connection connection = jdbcUtils.getConnection();
        try (PreparedStatement preparedStatement = connection.prepareStatement("DELETE FROM age_group WHERE age_group_id = ?");
        ) {
            preparedStatement.setLong(1, aLong);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            logger.error(e);
            System.err.println("Error DB " + e);
        }
    }

    @Override
    public void update(AgeGroup entity) {
        logger.info("Updating AgeGroup: {}", entity);
        Connection connection = jdbcUtils.getConnection();
        try (PreparedStatement preparedStatement = connection.prepareStatement("UPDATE age_group SET name = ?, min_age = ?, max_age = ? WHERE age_group_id = ?");
        ) {
            preparedStatement.setString(1, entity.getName());
            preparedStatement.setInt(2, entity.getMinAge());
            preparedStatement.setInt(3, entity.getMaxAge());
            preparedStatement.setLong(4, entity.getId());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            logger.error(e);
            System.err.println("Error DB " + e);
        }
    }
}
