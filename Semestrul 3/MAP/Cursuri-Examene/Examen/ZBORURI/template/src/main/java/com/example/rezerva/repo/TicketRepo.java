package com.example.rezerva.repo;

import com.example.rezerva.domain.Flight;
import com.example.rezerva.domain.Ticket;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class TicketRepo implements Repository<Ticket>{
    private String url;
    private String username;
    private String passwd;

    public TicketRepo(String url, String username, String passwd) {
        this.url = url;
        this.username = username;
        this.passwd = passwd;
    }

    @Override
    public List<Ticket> getAll() {
        List<Ticket> all = new ArrayList<>();
        try (Connection connection = DriverManager.getConnection(url, username, passwd);
             PreparedStatement statement = connection.prepareStatement("SELECT * from \"ticket\"");
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                Long idFlight=resultSet.getLong("idflight");
                String usrname = resultSet.getString("username");
                LocalDateTime data=resultSet.getTimestamp("data").toLocalDateTime();

                Ticket tckt=new Ticket(usrname,idFlight,data);
                all.add(tckt);
            }
            return all;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return all;
    }

    public void adauga(Ticket ticket) {
        String sql = "insert into \"ticket\" (username,idflight,data) values (?,?,?)";

        try (Connection connection = DriverManager.getConnection(url, username, passwd);
             PreparedStatement ps = connection.prepareStatement(sql)) {

            ps.setString(1, ticket.getUsername());
            ps.setLong(2, ticket.getIdFlight());
            ps.setTimestamp(3,Timestamp.valueOf(ticket.getData()));

            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
