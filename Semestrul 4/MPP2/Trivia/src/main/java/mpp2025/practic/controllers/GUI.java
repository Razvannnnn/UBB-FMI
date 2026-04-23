package mpp2025.practic.controllers;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import mpp2025.domain.User;
import mpp2025.practic.models.Client;

import java.io.IOException;

public class GUI extends Application {
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        Client client = new Client("127.0.0.1", 12353);
        Thread clientThread = new Thread(() -> {
            try {
                client.start();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        clientThread.setDaemon(true);
        clientThread.start();

        FXMLLoader loaderLogIn = new FXMLLoader(GUI.class.getResource("/mpp2025/practic/login.fxml"));
        AnchorPane loginView = loaderLogIn.load();
        LoginController loginController = loaderLogIn.getController();

        /////// PAS 2: main view
        FXMLLoader loaderMain = new FXMLLoader(GUI.class.getResource("/mpp2025/practic/main.fxml"));
        AnchorPane mainView = loaderMain.load();
        MainController playGameController = loaderMain.getController();

        // set up tabs
        TabPane tabPane = new TabPane();
        Tab loginTab = new Tab("Login", loginView);
        Tab mainTab = new Tab("Main", mainView); /// pas 2
        tabPane.getTabs().add(loginTab);

        loginController.setClient(client);
        playGameController.setClient(client); // PAS 2

        loginController.setOnLoginSuccess(() -> {
            tabPane.getTabs().clear();
            tabPane.getTabs().add(mainTab); // PAS 2
            User loggedInUser = loginController.getUser();
            playGameController.setUser(loggedInUser); // PAS 2
            playGameController.loadAfterLogin();
        });

        Scene scene = new Scene(tabPane);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Login");
        primaryStage.show();
    }
}
