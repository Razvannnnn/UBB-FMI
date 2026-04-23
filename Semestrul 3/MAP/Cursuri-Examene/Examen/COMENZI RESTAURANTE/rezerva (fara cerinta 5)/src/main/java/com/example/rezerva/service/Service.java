package com.example.rezerva.service;

import com.example.rezerva.domain.MenuItem;
import com.example.rezerva.domain.Order;
import com.example.rezerva.domain.OrderItem;
import com.example.rezerva.domain.Table;
import com.example.rezerva.repo.MenuItemRepo;
import com.example.rezerva.repo.OrderItemRepo;
import com.example.rezerva.repo.OrderRepo;
import com.example.rezerva.repo.TableRepo;
import com.example.rezerva.utils.observer.Observer;
import javafx.event.ActionEvent;
import javafx.scene.control.Menu;

import java.util.ArrayList;
import java.util.List;

public class Service {
    private TableRepo tableRepo;
    private MenuItemRepo menuItemRepo;
    private OrderRepo orderRepo;
    private OrderItemRepo orderItemRepo;
    //private List<Observer<EntityChangeEvent>> observers= new ArrayList<>();

    public Service(TableRepo tableRepo,MenuItemRepo menuItemRepo, OrderRepo orderRepo,OrderItemRepo oderItemRepo) {
        this.tableRepo = tableRepo;
        this.menuItemRepo=menuItemRepo;
        this.orderRepo=orderRepo;
        this.orderItemRepo=oderItemRepo;
    }

    public List<Table> getTables() {
        return tableRepo.getAll();
    }
    public List<MenuItem> getMenuItems() {
        return menuItemRepo.getAll();
    }
    public List<Order> getOrders() {
        return orderRepo.getAll();
    }
    public List<OrderItem> getOrderItames() {
        return orderItemRepo.getAll();
    }

    public List<String> getAllCategory()
    {
        List<String> all=new ArrayList<>();
        for(MenuItem mi: menuItemRepo.getAll()){
            if(!all.contains(mi.getCategory()))
                all.add(mi.getCategory());
        }
        return all;
    }

    public List<MenuItem> getMenuItemForCategory(String category){
        List<MenuItem> all=new ArrayList<>();
        for(MenuItem mi: menuItemRepo.getAll()){
            if(mi.getCategory().equals(category))
                all.add(mi);
        }
        return all;
    }

    public void adaugaOrder(Order order){
        orderRepo.adauga(order);
    }

    public void adaugaOrderItem(OrderItem orderitem){
        orderItemRepo.adauga(orderitem);
    }
}
