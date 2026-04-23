package chat.persistence.repository.jdbc;

import chat.model.User;
import chat.persistence.UserRepository;
import org.springframework.stereotype.Component;

import java.sql.*;
import java.util.ArrayList;
import java.util.Properties;


public class UserRepositoryJdbc implements UserRepository {
    private JdbcUtils jdbcUtils;
    public UserRepositoryJdbc(Properties jdbcProps){
        jdbcUtils=new JdbcUtils(jdbcProps);
    }

    @Override
    public User findBy(String username) {
        return null;
    }

    @Override
    public User findBy(String username, String pass) {
        System.out.println("JDBC findBy 2 params");
        Connection con=jdbcUtils.getConnection();
        try {
            PreparedStatement preStmt=con.prepareStatement("select name from users where username=? and password=?");
            preStmt.setString(1,username);
            preStmt.setString(2,pass);
            ResultSet result=preStmt.executeQuery();
            boolean resOk=result.next();
            System.out.println("findBy user, pass "+resOk);
            if (resOk) {
                User user=new User(username);
                user.setName(result.getString("name"));
                return user;
            }
        } catch (SQLException e) {
            System.out.println("Error DB "+e);
        }
        return null;
    }

    @Override
    public Iterable<User> getFriendsOf(User user) {
        System.out.println("JDBC get friends of");
        Connection con=jdbcUtils.getConnection();
        ArrayList<User> result=new ArrayList<User>();
        try {
            PreparedStatement preStmt=con.prepareStatement("select friends.userA, friends.userB  from friends where  friends.userA=? or friends.userB=?");
            preStmt.setString(1,user.getId());
            preStmt.setString(2,user.getId());
            ResultSet rs=preStmt.executeQuery();
            while(rs.next()){
                String friendA=rs.getString(1);
                String friendB=rs.getString(2);
                User u=new User(friendA.equals(user.getId())?friendB:friendA);
                result.add(u);
            }

        } catch (SQLException e) {
            System.out.println("Error DB "+e);
        }
        return result;
    }

    @Override
    public User[] getUsers() {
        return new User[0];
    }

    @Override
    public void save(User user) {

    }

    @Override
    public void delete(String s) {

    }

    @Override
    public User findOne(String s) {
        return null;
    }

    @Override
    public void update(String s, User user) {

    }

    @Override
    public Iterable<User> getAll() {
        return null;
    }
}
