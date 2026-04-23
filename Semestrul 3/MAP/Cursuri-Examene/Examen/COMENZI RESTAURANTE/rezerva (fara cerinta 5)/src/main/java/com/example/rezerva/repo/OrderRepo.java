package com.example.rezerva.repo;

import com.example.rezerva.domain.Order;
import com.example.rezerva.domain.Status;
import com.example.rezerva.domain.Table;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class OrderRepo implements Repository<Order>{
    private String url;
    private String usernamee;
    private String passwd;

    public OrderRepo(String url, String usernamee, String passwd) {
        this.url = url;
        this.usernamee = usernamee;
        this.passwd = passwd;
    }

    @Override
    public List<Order> getAll() {
        List<Order> all = new ArrayList<>();
        try (Connection connection = DriverManager.getConnection(url, usernamee, passwd);
             PreparedStatement statement = connection.prepareStatement("SELECT * from orderr");
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                Integer id = resultSet.getInt("id");
                Integer idTable = resultSet.getInt("idtable");
                LocalDateTime data = resultSet.getTimestamp("data").toLocalDateTime();
                Status status=Status.valueOf(resultSet.getString("status"));

                Order order=new Order(id,idTable,data,status);
                all.add(order);
            }
            return all;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return all;
    }

    public void adauga(Order order) {

        String sql = "insert into \"orderr\" (id,idtable,data,status) values (?,?,?,?)";

        try (Connection connection = DriverManager.getConnection(url, usernamee, passwd);
             PreparedStatement ps = connection.prepareStatement(sql)) {

            ps.setInt(1, order.getId());;
            ps.setInt(2, order.getIdTable());
            Timestamp timestamp=Timestamp.valueOf(order.getData());
            ps.setTimestamp(3,timestamp);
            ps.setString(4, order.getStatus().toString());

            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
