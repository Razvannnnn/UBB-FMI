package org.example.comenzirestaurant.Domain;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.List;

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


}
