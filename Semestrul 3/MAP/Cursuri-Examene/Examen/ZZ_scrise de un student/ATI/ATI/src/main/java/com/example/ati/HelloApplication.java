package com.example.ati;

import com.example.ati.Controller.PacientiController;
import com.example.ati.Controller.PaturiController;
import com.example.ati.Repository.PacientiRepository;
import com.example.ati.Repository.PaturiRepository;
import com.example.ati.Serivice.Service;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.io.IOException;

public class HelloApplication extends Application {
    PacientiRepository repo_pacienti=new PacientiRepository("jdbc:postgresql://localhost:5432/ati","postgres","aezakmi");
    PaturiRepository repo_paturi=new PaturiRepository("jdbc:postgresql://localhost:5432/ati","postgres","aezakmi");
    Service service=new Service(repo_paturi,repo_pacienti);



    @Override
    public void start(Stage stage) throws IOException
    {
        initView(stage);
        stage.setWidth(800);
        stage.setHeight(500);
        stage.show();
    }


    private void initView(Stage primaryStage) throws IOException
    {
        FXMLLoader fxmllLoader1=new FXMLLoader(HelloApplication.class.getResource("views/paturi.fxml"));
        Scene scene1=new Scene(fxmllLoader1.load(),800,500);
        Stage stage1=new Stage();
        stage1.setScene(scene1);
        stage1.show();

        FXMLLoader fxmllLoader2=new FXMLLoader(HelloApplication.class.getResource("views/pacienti.fxml"));
        AnchorPane userLayout2=(AnchorPane) fxmllLoader2.load();
        primaryStage.setScene(new Scene(userLayout2,800,500));



        PaturiController loginController1= fxmllLoader1.getController();
        loginController1.setPaturiService(service);
        PacientiController loginControllerPacienti= fxmllLoader2.getController();
        loginControllerPacienti.setPacientiService(service,loginController1);
    }
}