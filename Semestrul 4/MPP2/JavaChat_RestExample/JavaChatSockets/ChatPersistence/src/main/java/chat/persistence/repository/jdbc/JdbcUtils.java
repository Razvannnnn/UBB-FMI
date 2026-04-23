package chat.persistence.repository.jdbc;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

/**
 * Created by grigo on 3/17/17.
 */
public class JdbcUtils {
    private Properties props;
    public JdbcUtils(Properties props){
        this.props=props;
    }
    private static Connection instance=null;
    private Connection getNewConnection(){
        String driver=props.getProperty("chat.jdbc.driver");
        String url=props.getProperty("chat.jdbc.url");
        String user=props.getProperty("chat.jdbc.user");
        String pass=props.getProperty("chat.jdbc.pass");
        Connection con=null;
        try {
            Class.forName(driver);
            con= DriverManager.getConnection(url,user,pass);
        } catch (ClassNotFoundException e) {
            System.out.println("Error loading driver "+e);
        } catch (SQLException e) {
            System.out.println("Error getting connection "+e);
        }
        return con;
    }

    public Connection getConnection(){
        try {
            if (instance==null || instance.isClosed())
                instance=getNewConnection();

        } catch (SQLException e) {
            System.out.println("Error DB "+e);
        }
        return instance;
    }

}
