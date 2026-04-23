package org.example.zboruri.Repository;

import org.example.zboruri.Domain.Client;
import org.example.zboruri.Domain.Flight;
import org.example.zboruri.Domain.Ticket;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;

public class RepoTicket implements Repository<Ticket> {
    private String url;
    private String user;
    private String password;

    public RepoTicket(String url, String user, String password) {
        this.url = url;
        this.user = user;
        this.password = password;
    }

    @Override
    public Iterable<Ticket> getAll() {
        Iterable<Ticket> tickets = new ArrayList<>();
        try (Connection connection = DriverManager.getConnection(url, user, password);
             PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM ticket");
             ResultSet resultSet = preparedStatement.executeQuery()) {
            while (resultSet.next()) {
                Long id = resultSet.getLong("ticketid");
                String username = resultSet.getString("username");
                Long id_flight = resultSet.getLong("flightid");
                LocalDateTime purchaseTime = resultSet.getTimestamp("purchasetime").toLocalDateTime();
                Ticket ticket = new Ticket(id, username, id_flight, purchaseTime);
                ((ArrayList<Ticket>) tickets).add(ticket);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return tickets;
    }

    public void save(Flight flight, Client client) {
        try (Connection connection = DriverManager.getConnection(url, user, password);
             var statement = connection.prepareStatement("INSERT INTO ticket(username, flightid, purchasetime) VALUES (?, ?, ?)")) {
            statement.setString(1, client.getUsername());
            statement.setLong(2, flight.getId());
            statement.setTimestamp(3, java.sql.Timestamp.valueOf(LocalDateTime.now()));
            statement.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Integer numberOfTicketsForFlight(Long flightId) {
        Integer count = 0;
        try (Connection connection = DriverManager.getConnection(url, user, password);
             var statement = connection.prepareStatement("SELECT COUNT(*) FROM ticket WHERE flightid = ?")) {
            statement.setLong(1, flightId);
            ResultSet resultSet = statement.executeQuery();
            resultSet.next();
            count = resultSet.getInt(1);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return count;
    }
}
