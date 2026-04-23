package org.example.comenzirestaurant.Repository;

import org.example.comenzirestaurant.Domain.MenuItem;
import org.example.comenzirestaurant.Domain.Order;
import org.example.comenzirestaurant.Domain.OrderStatus;

import java.sql.Connection;
import java.sql.DriverManager;
import java.time.LocalDateTime;
import java.util.*;

public class RepoOrder implements Repository<Order> {
    private String url;
    private String username;
    private String password;

    public RepoOrder(String url, String username, String password) {
        this.url = url;
        this.username = username;
        this.password = password;
    }

    @Override
    public List<Order> getAll() {
        List<Order> orders = new ArrayList<>();
        Map<Integer, List<Integer>> orderItems = new HashMap<>();
        try (Connection connection = DriverManager.getConnection(url, username, password);
             var statement = connection.prepareStatement("SELECT * from \"orderitems\"");
             var resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                int orderid = resultSet.getInt("orderid");
                int menuitemid = resultSet.getInt("menuitemid");

                orderItems.computeIfAbsent(orderid, k -> new ArrayList<>()).add(menuitemid);
            }
            return orders;
        } catch (Exception e) {
            e.printStackTrace();
        }

        try (Connection connection = DriverManager.getConnection(url, username, password);
             var statement = connection.prepareStatement("SELECT * from \"orders\"");
             var resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                int tableId = resultSet.getInt("table_id");
                LocalDateTime date = resultSet.getTimestamp("date").toLocalDateTime();
                OrderStatus status = OrderStatus.valueOf(resultSet.getString("status"));

                List<Integer> orderItemsIds = orderItems.get(id);
                Order order = new Order(id, tableId, orderItemsIds, date, status);
                orders.add(order);
            }
            return orders;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return orders;
    }
}
