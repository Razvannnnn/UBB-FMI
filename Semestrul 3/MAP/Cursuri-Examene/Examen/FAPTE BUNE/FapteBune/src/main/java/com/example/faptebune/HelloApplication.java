package com.example.faptebune;


import com.example.faptebune.Controller.LogInController;
import com.example.faptebune.Repository.NevoieDataBaseRepository;
import com.example.faptebune.Repository.PersoanaDataBaseRepository;
import com.example.faptebune.Service.NevoieService;
import com.example.faptebune.Service.PersoanaService;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class HelloApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("logIn-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 600, 400);
        stage.setTitle("LogIn");
        stage.setScene(scene);

        PersoanaDataBaseRepository persoanaDataBaseRepository = new PersoanaDataBaseRepository();
        PersoanaService persoanaService = new PersoanaService(persoanaDataBaseRepository);

        NevoieDataBaseRepository nevoieDataBaseRepository = new NevoieDataBaseRepository();
        NevoieService nevoieService = new NevoieService(nevoieDataBaseRepository);

        LogInController logInController = fxmlLoader.getController();
        logInController.setPersoanaService(persoanaService);
        logInController.setNevoieService(nevoieService);
        logInController.setLogInStage(stage);
        logInController.setLogInController(logInController);
        logInController.setListaPersoane();

        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}