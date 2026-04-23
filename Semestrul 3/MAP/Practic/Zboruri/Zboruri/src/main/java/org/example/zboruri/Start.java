package org.example.zboruri;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.example.zboruri.Repository.RepoClient;
import org.example.zboruri.Repository.RepoFlight;
import org.example.zboruri.Repository.RepoTicket;
import org.example.zboruri.Service.Service;

import java.io.IOException;

public class Start extends Application {

    RepoClient repoClient;
    RepoFlight repoFlight;
    RepoTicket repoTicket;
    Service service;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) throws Exception {
        repoClient = new RepoClient("jdbc:postgresql://localhost:5432/zboruri",
                "postgres", "parola");
        repoFlight = new RepoFlight("jdbc:postgresql://localhost:5432/zboruri",
                "postgres", "parola");
        repoTicket = new RepoTicket("jdbc:postgresql://localhost:5432/zboruri",
                "postgres", "parola");
        service = new Service(repoClient, repoFlight, repoTicket);
        initViewLogin(stage);
        stage.setTitle("Login");
        stage.show();
    }

    private void initViewLogin(Stage stage) throws IOException {
        FXMLLoader loader = new FXMLLoader(Start.class.getResource("login.fxml"));
        Scene scene = new Scene(loader.load());
        stage.setScene(scene);
        LoginController controller = loader.getController();
        controller.setService(service);
    }
}
