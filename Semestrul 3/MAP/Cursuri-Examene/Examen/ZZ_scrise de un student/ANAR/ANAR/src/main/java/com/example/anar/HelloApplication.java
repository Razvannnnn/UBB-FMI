package com.example.anar;

import com.example.anar.controller.LocalitatiController;
import com.example.anar.controller.MainController;
import com.example.anar.repository.LocalitateRepository;
import com.example.anar.repository.RauRepository;
import com.example.anar.service.Service;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class HelloApplication extends Application {

    private RauRepository rauRepository = new RauRepository();
    private LocalitateRepository localitateRepository = new LocalitateRepository();

    private Service service = new Service(rauRepository, localitateRepository);

    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("main.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 600, 400);
        MainController controller = fxmlLoader.getController();
        controller.setService(service);
        stage.setTitle("Rauri");
        stage.setScene(scene);
        stage.show();

        FXMLLoader localitatiLoader = new FXMLLoader(HelloApplication.class.getResource("localitati.fxml"));
        Stage localitatiStage = new Stage();
        Scene localitatiScene = new Scene(localitatiLoader.load(), 600, 400);
        LocalitatiController localitatiController = localitatiLoader.getController();
        localitatiController.setService(service);
        localitatiStage.setTitle("Localitati");
        localitatiStage.setScene(localitatiScene);
        localitatiStage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}