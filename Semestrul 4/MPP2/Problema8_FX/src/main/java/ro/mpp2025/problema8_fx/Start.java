package ro.mpp2025.problema8_fx;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import ro.mpp2025.problema8_fx.Controllers.LoginController;
import ro.mpp2025.problema8_fx.Repository.*;
import ro.mpp2025.problema8_fx.Service.Service;

import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;

public class Start extends Application {

    IRepoEvent repoEvent;
    IRepoChild repoChild;
    IRepoEnrollment repoEnrollment;
    IRepoUser repoUser;
    IRepoAgeGroup repoAgeGroup;
    Service service;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws IOException {
        Properties properties = new Properties();
        try {
            properties.load(new FileReader("db.config"));
        } catch (Exception e) {
            System.out.println("Error: " + e);
        }

        repoAgeGroup = new RepoAgeGroup(properties);
        repoEvent = new RepoEvent(properties);
        repoChild = new RepoChild(properties);
        repoEnrollment = new RepoEnrollment(properties);
        repoUser = new RepoUser(properties);
        service = new Service(repoAgeGroup, repoUser, repoChild, repoEvent, repoEnrollment);

        initView(primaryStage);
        primaryStage.setTitle("Login");
        primaryStage.show();
    }

    public void initView(Stage primaryStage) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("login.fxml"));
        Scene scene = new Scene(loader.load());
        primaryStage.setScene(scene);
        LoginController controller = loader.getController();
        controller.setService(service);
    }
}
