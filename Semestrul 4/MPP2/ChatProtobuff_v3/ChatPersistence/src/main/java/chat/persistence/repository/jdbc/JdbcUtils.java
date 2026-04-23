package chat.persistence.repository.jdbc;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;


public class JdbcUtils {
    private Properties props;
    public JdbcUtils(Properties props){
        this.props=props;
    }
    private static Connection instance=null;
    private Connection getNewConnection(){
        String url=props.getProperty("chat.jdbc.url");
        String user=props.getProperty("chat.jdbc.user");
        String pass=props.getProperty("chat.jdbc.pass");
        Connection con=null;
        try {
            if (user==null || pass==null){
                con=DriverManager.getConnection(url);
            }
            else
                con= DriverManager.getConnection(url,user,pass);
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
