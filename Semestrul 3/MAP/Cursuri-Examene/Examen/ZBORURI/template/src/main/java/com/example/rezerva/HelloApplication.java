package com.example.rezerva;

import com.example.rezerva.controller.LoginController;
import com.example.rezerva.repo.ClientRepo;
import com.example.rezerva.repo.FlightRepo;
import com.example.rezerva.repo.TicketRepo;
import com.example.rezerva.service.Service;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.List;

public class HelloApplication extends Application {
    Service service;

    @Override
    public void start(Stage primaryStage) throws IOException {
        this.service = new Service(new ClientRepo("jdbc:postgresql://localhost:5432/ZBOR","postgres","ubb22"),
                new FlightRepo("jdbc:postgresql://localhost:5432/ZBOR","postgres","ubb22"),
                new TicketRepo("jdbc:postgresql://localhost:5432/ZBOR","postgres","ubb22"));

        primaryStage.setTitle("START PAGE");
        startView(primaryStage);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }

    private void startView(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("/views/loginView.fxml"));
        AnchorPane Layout = fxmlLoader.load();
        stage.setScene(new Scene(Layout));

        LoginController startController = fxmlLoader.getController();
        startController.setService(service);
    }
}