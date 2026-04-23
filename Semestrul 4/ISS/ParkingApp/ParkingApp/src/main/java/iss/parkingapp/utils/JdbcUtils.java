package iss.parkingapp.utils;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class JdbcUtils {
    private Properties jdbcProps;
    private static final Logger logger = LogManager.getLogger();
    public JdbcUtils(Properties props){
        jdbcProps=props;
    }
    private Connection instance=null;

    private Connection getNewConnection() {
        Connection con = null;
        try {
            con = DriverManager.getConnection(jdbcProps.getProperty("jdbc.url"));
            System.out.println("Connection established");
        } catch (SQLException e) {
            logger.error(e);
            System.out.println("Error getting connection " + e);
        }
        return con;
    }

    public Connection getConnection() {
        System.out.println("Getting connection");
        try {
            if (instance == null || instance.isClosed())
                instance = getNewConnection();
        } catch (SQLException e) {
            logger.error(e);
            System.out.println("Error DB " + e);
        }
        return instance;
    }
}
