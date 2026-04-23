package iss.parkingapp.repository;

import iss.parkingapp.domain.LocParcare;
import iss.parkingapp.domain.StatusLoc;
import iss.parkingapp.domain.Utilizator;
import iss.parkingapp.utils.JdbcUtils;
import javafx.collections.ObservableList;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Properties;

public class RepoLocParcare implements IRepoLocParcare {
    private JdbcUtils jdbcUtils;
    private static final Logger logger = LogManager.getLogger();

    public RepoLocParcare(Properties properties) {
        logger.info("Initializing LocParcareDBRepository with properties: {} ", properties);
        jdbcUtils = new JdbcUtils(properties);
    }

    @Override
    public void add(LocParcare entity) {

    }

    @Override
    public void delete(Long aLong) {

    }

    @Override
    public void update(LocParcare entity) {

    }

    @Override
    public LocParcare findOne(Long aLong) {
        logger.info("Finding LocParcare by id: {}", aLong);
        Connection connection = jdbcUtils.getConnection();
        try (PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM LocParcare WHERE id_loc_parcare = ?")) {
            preparedStatement.setLong(1, aLong);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    StatusLoc status = StatusLoc.valueOf(resultSet.getString("status"));
                    String pozitie = resultSet.getString("pozitie");
                    Integer taxa = resultSet.getInt("taxa");
                    LocParcare locParcare = new LocParcare(status, pozitie, taxa);
                    locParcare.setId_loc_parcare(aLong);
                    logger.traceExit(locParcare);
                    return locParcare;
                }
            }
        } catch (SQLException e) {
            logger.error(e);
            System.err.println("Error DB " + e);
        }
        logger.traceExit("No Parking space found with id: {}", aLong);
        return null;
    }

    @Override
    public ObservableList<LocParcare> findAll() {
        logger.info("Finding all Parking spaces");
        Connection connection = jdbcUtils.getConnection();
        ObservableList<LocParcare> locuriParcare = javafx.collections.FXCollections.observableArrayList();
        try (PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM LocParcare")) {
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    Long id = resultSet.getLong("id_loc_parcare");
                    StatusLoc status = StatusLoc.valueOf(resultSet.getString("status"));
                    String pozitie = resultSet.getString("pozitie");
                    Integer taxa = resultSet.getInt("taxa");
                    LocParcare locParcare = new LocParcare(status, pozitie, taxa);
                    locParcare.setId_loc_parcare(id);
                    locuriParcare.add(locParcare);
                    logger.traceExit(locParcare);
                }
            }
        } catch (SQLException e) {
            logger.error(e);
            System.err.println("Error DB " + e);
        }
        return locuriParcare;
    }

    @Override
    public int size() {
        return 0;
    }
}
