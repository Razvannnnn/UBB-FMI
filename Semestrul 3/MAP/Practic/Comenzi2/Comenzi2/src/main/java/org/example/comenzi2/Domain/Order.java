package org.example.comenzi2.Domain;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

public class Order {
    private int id;
    private int table;
    private List<Integer> menuItems;
    private LocalDateTime date;
    private OrderStatus status;

    public Order(int id, int table, List<Integer> menuItems, LocalDateTime date, OrderStatus status) {
        this.id = id;
        this.table = table;
        this.menuItems = menuItems;
        this.date = date;
        this.status = status;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getTable() {
        return table;
    }

    public void setTable(int table) {
        this.table = table;
    }

    public List<Integer> getMenuItems() {
        return menuItems;
    }

    public void setMenuItems(List<Integer> menuItems) {
        this.menuItems = menuItems;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }

    public OrderStatus getStatus() {
        return status;
    }

    public void setStatus(OrderStatus status) {
        this.status = status;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Order order = (Order) o;
        return id == order.id && table == order.table && Objects.equals(menuItems, order.menuItems) && Objects.equals(date, order.date) && status == order.status;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, table, menuItems, date, status);
    }

    @Override
    public String toString() {
        return "Order{" +
                "id=" + id +
                ", table=" + table +
                ", menuItems=" + menuItems +
                ", date=" + date +
                ", status=" + status +
                '}';
    }
}
