package com.example.rezerva;

import com.example.rezerva.controller.StaffController;
import com.example.rezerva.controller.TablePage;
import com.example.rezerva.domain.Table;
import com.example.rezerva.repo.MenuItemRepo;
import com.example.rezerva.repo.OrderItemRepo;
import com.example.rezerva.repo.OrderRepo;
import com.example.rezerva.repo.TableRepo;
import com.example.rezerva.service.Service;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;

public class HelloApplication extends Application {
    Service service;
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws IOException {
        this.service = new Service(new TableRepo("jdbc:postgresql://localhost:5432/RESTAURANT","postgres","ubb22"),
                new MenuItemRepo("jdbc:postgresql://localhost:5432/RESTAURANT","postgres","ubb22"),
                new OrderRepo("jdbc:postgresql://localhost:5432/RESTAURANT","postgres","ubb22"),
                new OrderItemRepo("jdbc:postgresql://localhost:5432/RESTAURANT","postgres","ubb22"));

        primaryStage.setTitle("START PAGE");
        startView(primaryStage);
        primaryStage.show();

        for(Table t: service.getTables()){
            //getTable(t.getId());
            TablePage tablePage = new TablePage(service,t);
            tablePage.show();
        }
    }

    private void startView(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("/views/staffView.fxml"));
        AnchorPane Layout = fxmlLoader.load();
        stage.setScene(new Scene(Layout));
        stage.setTitle("STAFF");

        StaffController mainController = fxmlLoader.getController();
        //mainController.setService(this.service);
    }

}