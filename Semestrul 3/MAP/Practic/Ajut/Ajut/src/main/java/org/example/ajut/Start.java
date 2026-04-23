package org.example.ajut;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.example.ajut.Repository.NevoieRepo;
import org.example.ajut.Repository.PersoanaRepo;
import org.example.ajut.Service.Service;

import java.io.IOException;

public class Start extends Application {

    NevoieRepo nevoieRepo;
    PersoanaRepo persoanaRepo;
    Service service;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) throws Exception {
        nevoieRepo = new NevoieRepo("jdbc:postgresql://localhost:5432/ajut",
                "postgres", "parola");
        persoanaRepo = new PersoanaRepo("jdbc:postgresql://localhost:5432/ajut",
                "postgres", "parola");
        service = new Service(nevoieRepo, persoanaRepo);
        initView(stage);
        stage.setTitle("Ajutor");
        stage.show();
    }

    private void initView(Stage stage) throws IOException {
        FXMLLoader loader = new FXMLLoader(Start.class.getResource("login.fxml"));
        Scene scene = new Scene(loader.load());
        stage.setScene(scene);
        LoginController controller = loader.getController();
        controller.setService(service);
    }
}
