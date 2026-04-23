package org.example.zboruri.Repository;

import org.example.zboruri.Domain.Flight;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;

public class RepoFlight implements Repository<Flight> {
    private String url;
    private String user;
    private String password;

    public RepoFlight(String url, String user, String password) {
        this.url = url;
        this.user = user;
        this.password = password;
    }

    @Override
    public Iterable<Flight> getAll() {
        Iterable<Flight> flights = new ArrayList<>();
        try (Connection connection = DriverManager.getConnection(url, user, password);
             PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM flight");
             ResultSet resultSet = preparedStatement.executeQuery()) {
            while (resultSet.next()) {
                Long id = resultSet.getLong("flightid");
                String from = resultSet.getString("fromcity");
                String to = resultSet.getString("tocity");
                LocalDateTime departureTime = resultSet.getTimestamp("departuretime").toLocalDateTime();
                LocalDateTime landingTime = resultSet.getTimestamp("landingtime").toLocalDateTime();
                Integer seats = resultSet.getInt("seats");
                Flight flight = new Flight(id, from, to, departureTime, landingTime, seats);
                ((ArrayList<Flight>) flights).add(flight);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return flights;
    }

    public Iterable<Flight> getAllFlightsFilter(String from, String to, LocalDateTime date) {
        Iterable<Flight> flights = new ArrayList<>();
        try (Connection connection = DriverManager.getConnection(url, user, password);
             PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM flight WHERE fromcity = ? AND tocity = ? AND DATE(departuretime) = ?");
        ) {
            preparedStatement.setString(1, from);
            preparedStatement.setString(2, to);
            preparedStatement.setDate(3, java.sql.Date.valueOf(date.toLocalDate()));
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                Long id = resultSet.getLong("flightid");
                String fromCity = resultSet.getString("fromcity");
                String toCity = resultSet.getString("tocity");
                LocalDateTime departureTime = resultSet.getTimestamp("departuretime").toLocalDateTime();
                LocalDateTime landingTime = resultSet.getTimestamp("landingtime").toLocalDateTime();
                Integer seats = resultSet.getInt("seats");
                Flight flight = new Flight(id, fromCity, toCity, departureTime, landingTime, seats);
                ((ArrayList<Flight>) flights).add(flight);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return flights;
    }

    public Iterable<String> getAllFromCities() {
        Iterable<String> cities = new ArrayList<>();
        try (Connection connection = DriverManager.getConnection(url, user, password);
             PreparedStatement preparedStatement = connection.prepareStatement("SELECT DISTINCT fromcity FROM flight");
             ResultSet resultSet = preparedStatement.executeQuery()) {
            while (resultSet.next()) {
                String city = resultSet.getString("fromcity");
                ((ArrayList<String>) cities).add(city);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return cities;
    }

    public Iterable<String> getAllToCities() {
        Iterable<String> cities = new ArrayList<>();
        try (Connection connection = DriverManager.getConnection(url, user, password);
             PreparedStatement preparedStatement = connection.prepareStatement("SELECT DISTINCT tocity FROM flight");
             ResultSet resultSet = preparedStatement.executeQuery()) {
            while (resultSet.next()) {
                String city = resultSet.getString("tocity");
                ((ArrayList<String>) cities).add(city);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return cities;
    }
}
