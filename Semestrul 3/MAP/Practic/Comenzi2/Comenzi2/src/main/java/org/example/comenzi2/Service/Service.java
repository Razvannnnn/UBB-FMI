package org.example.comenzi2.Service;

import org.example.comenzi2.Domain.MenuItem;
import org.example.comenzi2.Domain.Order;
import org.example.comenzi2.Domain.Table;
import org.example.comenzi2.Repository.MenuItemRepo;
import org.example.comenzi2.Repository.OrderRepo;
import org.example.comenzi2.Repository.TableRepo;
import org.example.comenzi2.Utils.Observable;
import org.example.comenzi2.Utils.Observer;
import org.example.comenzi2.Utils.ObserverImplem;

import java.util.ArrayList;
import java.util.List;

public class Service implements Observable {
    private final MenuItemRepo menuItemRepo;
    private final TableRepo tableRepo;
    private final OrderRepo orderRepo;
    private final List<Observer> observers;

    public Service(MenuItemRepo menuItemRepo, TableRepo tableRepo, OrderRepo orderRepo) {
        this.menuItemRepo = menuItemRepo;
        this.tableRepo = tableRepo;
        this.orderRepo = orderRepo;
        this.observers = new ArrayList<>();
    }

    public Iterable<Table> getAllTables() {
        return tableRepo.getAll();
    }

    public Iterable<MenuItem> getAllMenuItems() {
        return menuItemRepo.getAll();
    }

    public Iterable<String> getCategorii() {
        return menuItemRepo.categorii();
    }

    public Iterable<MenuItem> menuItemDupaCategorie(String categorie) {
        return menuItemRepo.menuItemDupaCategorie(categorie);
    }

    public Iterable<Order> getPlacedOrders() {
        return orderRepo.getPlacedOrders();
    }

    public List<MenuItem> getMenuItemsById(List<Integer> ids) {
        return menuItemRepo.menuItemsDupaId(ids);
    }


    @Override
    public void addObserver(Observer observer) {
        observers.add(observer);
    }

    @Override
    public void removeObserver(Observer observer) {
        observers.remove(observer);
    }

    @Override
    public void notifyObservers() {
        for (Observer observer : observers) {
            observer.update();
        }
    }

    public void adaugaComanda(Iterable<MenuItem> menuItems, Integer tableId) {
        orderRepo.addOrder(menuItems, tableId);
        notifyObservers();
    }
}

