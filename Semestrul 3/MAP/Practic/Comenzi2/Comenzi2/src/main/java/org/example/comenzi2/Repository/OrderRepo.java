package org.example.comenzi2.Repository;

import org.example.comenzi2.Domain.MenuItem;
import org.example.comenzi2.Domain.Order;
import org.example.comenzi2.Domain.OrderStatus;

import java.sql.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class OrderRepo implements Repository<Order> {
    private String url;
    private String user;
    private String password;

    public OrderRepo(String url, String user, String password) {
        this.url = url;
        this.user = user;
        this.password = password;
    }

    @Override
    public Iterable<Order> getAll() {
        List<Order> orders = new ArrayList<>();

        try (Connection connection = DriverManager.getConnection(url, user, password);
             PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM orders");
             ResultSet resultSet = preparedStatement.executeQuery()) {
            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                int tableid = resultSet.getInt("tableid");
                LocalDateTime date = resultSet.getTimestamp("date").toLocalDateTime();
                String status = resultSet.getString("status");
                List<Integer> menuItems = (List<Integer>) getMenuItemsFromOrder(id);

                Order order = new Order(id, tableid, menuItems, date, OrderStatus.valueOf(status));

                orders.add(order);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return orders;
    }

    public Iterable<Integer> getMenuItemsFromOrder(int idOrder) {
        List<Integer> menuItems = new ArrayList<>();
        try (Connection connection = DriverManager.getConnection(url, user, password);
             PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM orderitems WHERE orderid = ?");
        ) {
            preparedStatement.setInt(1, idOrder);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                int idMenuItem = resultSet.getInt("menuitemid");
                menuItems.add(idMenuItem);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return menuItems;
    }

    public Iterable<Order> getPlacedOrders() {
        List<Order> orders = new ArrayList<>();

        try (Connection connection = DriverManager.getConnection(url, user, password);
             PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM orders WHERE status = 'PLACED'");
             ResultSet resultSet = preparedStatement.executeQuery()) {
            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                int tableid = resultSet.getInt("tableid");
                LocalDateTime date = resultSet.getTimestamp("date").toLocalDateTime();
                String status = resultSet.getString("status");
                List<Integer> menuItems = (List<Integer>) getMenuItemsFromOrder(id);

                Order order = new Order(id, tableid, menuItems, date, OrderStatus.valueOf(status));

                orders.add(order);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return orders;
    }

    public void addOrder(Iterable<MenuItem> menuItems, Integer tableId) {
        try (Connection connection = DriverManager.getConnection(url, user, password);
             PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO orders (id, tableid, date, status) VALUES (?, ?, ?, ?::orderstatus)");
        ) {
            int id = getMaxIdOrder() + 1;
            preparedStatement.setInt(1, id);
            preparedStatement.setInt(2, tableId);
            preparedStatement.setTimestamp(3, Timestamp.valueOf(LocalDate.now().atStartOfDay()));
            preparedStatement.setString(4, OrderStatus.PLACED.name());
            preparedStatement.executeUpdate();
            addOrderItems(menuItems, id);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public int getMaxIdOrder() {
        int id = 1;
        try (Connection connection = DriverManager.getConnection(url, user, password);
             PreparedStatement preparedStatement = connection.prepareStatement("SELECT MAX(id) FROM orders");
             ResultSet resultSet = preparedStatement.executeQuery()) {
            if (resultSet.next()) {
                id = resultSet.getInt(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return id;
    }

    public void addOrderItems(Iterable<MenuItem> menuItems, Integer orderId) {
        try (Connection connection = DriverManager.getConnection(url, user, password);
             PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO orderitems (orderid, menuitemid) VALUES (?, ?)");
        ) {
            for (MenuItem menuItem : menuItems) {
                preparedStatement.setInt(1, orderId);
                preparedStatement.setInt(2, menuItem.getId());
                preparedStatement.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
}
