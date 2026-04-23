package com.example.clinicaprivata;

import com.example.clinicaprivata.Controller.MediciController;
import com.example.clinicaprivata.Controller.SectiiController;
import com.example.clinicaprivata.Entities.Medic;
import com.example.clinicaprivata.Repository.RepositoryConsultatii;
import com.example.clinicaprivata.Repository.RepositoryMedici;
import com.example.clinicaprivata.Repository.RepositorySectii;
import com.example.clinicaprivata.Service.Service;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.List;

public class HelloApplication extends Application {
    RepositorySectii repo_sectii=new RepositorySectii("jdbc:postgresql://localhost:5432/clinica","postgres","aezakmi");
    RepositoryMedici repo_medici=new RepositoryMedici("jdbc:postgresql://localhost:5432/clinica","postgres","aezakmi");
    RepositoryConsultatii repo_consultatii=new RepositoryConsultatii("jdbc:postgresql://localhost:5432/clinica","postgres","aezakmi");
    Service service=new Service(repo_sectii,repo_medici,repo_consultatii);



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
        FXMLLoader fxmllLoader=new FXMLLoader(HelloApplication.class.getResource("views/Sectii.fxml"));
        Pane sectiiLayout=(AnchorPane) fxmllLoader.load();
        primaryStage.setScene(new Scene(sectiiLayout,800,500));
        SectiiController sectiiController= fxmllLoader.getController();
        sectiiController.setSectiiService(service);

        List<Medic> medici=service.getMedici();
        for(Medic m:medici)
        {
            FXMLLoader fxmllLoaderM=new FXMLLoader(HelloApplication.class.getResource("views/Medic.fxml"));
            Scene scene1=new Scene(fxmllLoaderM.load(),800,500);
            Stage stage1=new Stage();
            stage1.setScene(scene1);
            stage1.show();
            MediciController mediciController= fxmllLoaderM.getController();
            mediciController.setMediciService(service,m);
        }
    }
}