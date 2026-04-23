package com.example.rezerva.controller;

import com.example.rezerva.domain.*;
import com.example.rezerva.service.Service;

import com.example.rezerva.utils.TableViewCustom;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

public class TablePage extends VBox {
    private Service srv;

    private Table table;
    private List<TableViewCustom> tables=new ArrayList<>();

    public TablePage(Service srv, Table table){
        this.srv=srv;
        this.table=table;

        buildEverything();
    }

    private void buildEverything() {
        buildTables();
    }

    private void buildTables() {
        for(String category:srv.getAllCategory()){
            List<String> headere = new ArrayList<>();

            //titlul coloanei si functia pe coloana
            headere.add("item");
            Function<MenuItem,String> f1 = new Function<MenuItem, String>() {
                @Override
                public String apply(MenuItem menuItem) {
                    return menuItem.getItem();
                }
            };

            headere.add("pret");
            Function<MenuItem,String> f2 = new Function<MenuItem, String>() {
                @Override
                public String apply(MenuItem menuItem) {
                    return menuItem.getPrice().toString()+" "+menuItem.getCurrency();
                }
            };


            //adaug titlul tabelei
            Label label = new Label(category);
            getChildren().add(label);

            //creez efectiv tabelul
            TableViewCustom<MenuItem> tabel = new TableViewCustom(srv.getMenuItemForCategory(category),headere,f1,f2);
            getChildren().add(tabel);
//            Button button = new Button("ext");
//            getChildren().add(button);

            tables.add(tabel);
        }


        //creare buton si onAction pe el (selectare multimpla)
        Button placeOrder = new Button("Place Order");
        getChildren().add(placeOrder);
        placeOrder.setOnAction(event -> {
            List<MenuItem> selectedItems = new ArrayList<>();
            for(TableViewCustom<MenuItem> tabel:tables){
                for(MenuItem menuItem:tabel.getSelectedElements()){
                    selectedItems.add(menuItem);
                }
            }
            //System.out.println(selectedItems);
            handleOrder(table.getId(),selectedItems);

        });
    }

    public void show(){
        Scene scene = new Scene(this, 300, 500);
        Stage stage = new Stage();

        //titlul ferestrei
        stage.setTitle("ID table: "+table.getId().toString());
        stage.setScene(scene);

        stage.show();
    }

    public void handleOrder(Integer idTable, List<MenuItem> items){
        Integer id;
        List<Order> all=srv.getOrders();
        id=all.size();

        Order order=new Order(id,idTable, LocalDateTime.now(), Status.PLACED);
        srv.adaugaOrder(order);

        for(MenuItem mi: items){
            OrderItem oi = new OrderItem(id,mi.getId());
            srv.adaugaOrderItem(oi);
        }

    }
}