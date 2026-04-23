package iss.parkingapp;

import iss.parkingapp.repository.*;
import iss.parkingapp.service.Service;
import iss.parkingapp.utils.HibernateUtil;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;

public class Start extends Application {
    private IRepoMasina repoMasina;
    private IRepoUtilizator repoUtilizator;
    private IRepoLocParcare repoLocParcare;
    private IRepoRezervare repoRezervare;
    private Service service;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) throws Exception {
        Properties properties = new Properties();
        try {
            properties.load(new FileReader("db.config")); // Use relative path
        } catch (IOException e) {
            System.err.println("Error loading configuration: " + e.getMessage());
            throw e;
        }

        // Initialize Hibernate SessionFactory
        HibernateUtil.getSessionFactory();


//        repoMasina = new RepoMasina(properties);
//        repoUtilizator = new RepoUtilizator(properties);
//        repoLocParcare = new RepoLocParcare(properties);
//        repoRezervare = new RepoRezervare(properties);
//        service = new Service(repoUtilizator, repoLocParcare, repoRezervare, repoMasina);

        // Initialize repositories
        repoMasina = new HiberRepoMasina();
        repoUtilizator = new HiberRepoUtilizator();
        repoLocParcare = new HiberRepoLocParcare();
        repoRezervare = new HiberRepoRezervare();

        service = new Service(repoUtilizator, repoLocParcare, repoRezervare, repoMasina);

        initView(stage);
        stage.setTitle("Login");
        stage.show();
    }

    @Override
    public void stop() throws Exception {
        //HibernateUtil.closeSessionFactory();
        super.stop();
    }

    private void initView(Stage primaryStage) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("login.fxml"));
        Scene scene = new Scene(loader.load());
        primaryStage.setScene(scene);
        LoginController controller = loader.getController();
        controller.setService(service);
    }
}