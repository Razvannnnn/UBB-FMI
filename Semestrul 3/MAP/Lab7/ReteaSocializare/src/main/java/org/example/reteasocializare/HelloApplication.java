package org.example.reteasocializare;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import org.example.reteasocializare.Controller.UtilizatorController;
import org.example.reteasocializare.Domain.Validators.PrietenieValidator;
import org.example.reteasocializare.Domain.Validators.UtilizatorValidator;
import org.example.reteasocializare.Repository.*;
import org.example.reteasocializare.Domain.Utilizator;
import org.example.reteasocializare.Domain.Prietenie;
import org.example.reteasocializare.Service.Comunitati;
import org.example.reteasocializare.Service.Network;
import org.example.reteasocializare.Repository.DB.UtilizatorDBRepository;
import org.example.reteasocializare.Repository.DB.PrietenieDBRepository;
import org.example.reteasocializare.Service.UtilizatorService;

import java.io.IOException;
import java.security.Provider;

public class HelloApplication extends Application {

    UtilizatorService service;
    public static void main(String[] args) {
        launch();
    }

    @Override
    public void start(Stage primaryStage) throws IOException {
        System.out.println("Reading data from file");
        String username="postgres";
        String pasword="postgres";
        String url="jdbc:postgresql://localhost:5432/postgres";
        UtilizatorDBRepository utilizatorRepository =
                new UtilizatorDBRepository(url,username, pasword,  new UtilizatorValidator());
        PrietenieDBRepository prietenieRepository =
                new PrietenieDBRepository(url, username, pasword, new PrietenieValidator(utilizatorRepository));

        utilizatorRepository.findAll().forEach(x-> System.out.println(x));
        service = new UtilizatorService(utilizatorRepository);
        initView(primaryStage);
        primaryStage.setWidth(800);
        primaryStage.show();


    }

    private void initView(Stage primaryStage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("views/View.fxml"));

        AnchorPane userLayout = fxmlLoader.load();

        primaryStage.setScene(new Scene(userLayout));

        UtilizatorController userController = fxmlLoader.getController();
        userController.setUtilizatorService(service);

    }
}