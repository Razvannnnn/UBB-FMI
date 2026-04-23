package com.example.vacanta_var_2;

import com.example.vacanta_var_2.controller.ClientController;
import com.example.vacanta_var_2.controller.MainController;
import com.example.vacanta_var_2.domain.Client;
import com.example.vacanta_var_2.domain.Hotel;
import com.example.vacanta_var_2.domain.Location;
import com.example.vacanta_var_2.repo.*;
import com.example.vacanta_var_2.service.Service;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.List;
import java.util.Objects;

public class HelloApplication extends Application {
    Service service;
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws IOException {
        this.service = new Service(new LocationRepo("jdbc:postgresql://localhost:5432/EXAMEN","postgres","ubb22"),
                new HotelRepo("jdbc:postgresql://localhost:5432/EXAMEN","postgres","ubb22"),
                new OffRepo("jdbc:postgresql://localhost:5432/EXAMEN","postgres","ubb22"),
                new ClientRepo("jdbc:postgresql://localhost:5432/EXAMEN","postgres","ubb22"),
                new RezervareRepo("jdbc:postgresql://localhost:5432/EXAMEN","postgres","ubb22"));

        primaryStage.setTitle("START PAGE");
        startView(primaryStage);
        primaryStage.show();


        Parameters param = getParameters();
        List<String> list = param.getRaw();
        System.out.println(list.size());
        for(String clientId : list){
            Long id = Long.parseLong(clientId);
            System.out.println(id);

            //!!!!!
            getUsers(id);
        }
    }

    private void startView(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("/views/startView.fxml"));
        AnchorPane Layout = fxmlLoader.load();
        stage.setScene(new Scene(Layout));

        MainController mainController = fxmlLoader.getController();
        mainController.setService(this.service);
    }

    public void getUsers(Long clientId) {
        for (Client c : service.getClients()) {
            //System.out.println(c.getName());
            if (Objects.equals(clientId, c.getId())) {
                try {
                    Stage stageClient = new Stage();
                    FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("/views/clientView.fxml"));
                    Scene scene = new Scene(fxmlLoader.load());
                    stageClient.setTitle(c.getName());
                    stageClient.setScene(scene);
                    ClientController clientController = fxmlLoader.getController();
                    clientController.setService(service, clientId);
                    stageClient.show();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }
}