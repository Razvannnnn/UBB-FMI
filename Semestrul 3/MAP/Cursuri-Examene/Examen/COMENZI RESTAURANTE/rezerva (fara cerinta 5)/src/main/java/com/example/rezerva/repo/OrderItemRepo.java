package com.example.rezerva.repo;

import com.example.rezerva.domain.Order;
import com.example.rezerva.domain.OrderItem;
import com.example.rezerva.domain.Status;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class OrderItemRepo implements Repository<OrderItem> {
    private String url;
    private String usernamee;
    private String passwd;

    public OrderItemRepo(String url, String usernamee, String passwd) {
        this.url = url;
        this.usernamee = usernamee;
        this.passwd = passwd;
    }

    @Override
    public List<OrderItem> getAll() {
        List<OrderItem> all = new ArrayList<>();
        try (Connection connection = DriverManager.getConnection(url, usernamee, passwd);
             PreparedStatement statement = connection.prepareStatement("SELECT * from orderitem");
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                Integer idorder = resultSet.getInt("idorder");
                Integer iditem = resultSet.getInt("iditem");

                OrderItem oi=new OrderItem(idorder,iditem);
                all.add(oi);
            }
            return all;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return all;
    }

    public void adauga(OrderItem orderitem) {

        String sql = "insert into \"orderitem\" (idorder,iditem) values (?,?)";

        try (Connection connection = DriverManager.getConnection(url, usernamee, passwd);
             PreparedStatement ps = connection.prepareStatement(sql)) {

            ps.setInt(1, orderitem.getIdOrder());;
            ps.setInt(2, orderitem.getIdItem());

            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
