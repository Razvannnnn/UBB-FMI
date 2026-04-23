package com.example.guiex1;

import com.example.guiex1.controller.UtilizatorController;
import com.example.guiex1.domain.Utilizator;
import com.example.guiex1.domain.UtilizatorValidator;
import com.example.guiex1.repository.Repository;
import com.example.guiex1.repository.dbrepo.UtilizatorDbRepository;
import com.example.guiex1.services.UtilizatorService;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import java.io.IOException;

public class HelloApplication extends Application {
    UtilizatorService service;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws IOException {
        System.out.println("Reading data from database");
        String username="postgres";
        String pasword="postgres";
        String url="jdbc:postgresql://localhost:5432/socialnetwork";
        Repository<Long, Utilizator> utilizatorRepository =
                new UtilizatorDbRepository(url,username, pasword,  new UtilizatorValidator());

        service =new UtilizatorService(utilizatorRepository);
        initView(primaryStage);
        primaryStage.setWidth(800);
        primaryStage.show();
    }

    private void initView(Stage primaryStage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("views/UtilizatorView.fxml"));

        AnchorPane userLayout = fxmlLoader.load();
        primaryStage.setScene(new Scene(userLayout));

        UtilizatorController userController = fxmlLoader.getController();
        userController.setUtilizatorService(service);
    }
}