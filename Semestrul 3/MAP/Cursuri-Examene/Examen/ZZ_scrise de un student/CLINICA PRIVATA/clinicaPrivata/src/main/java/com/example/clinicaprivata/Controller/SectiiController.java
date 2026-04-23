package com.example.clinicaprivata.Controller;

import com.example.clinicaprivata.Entities.Sectie;
import com.example.clinicaprivata.HelloApplication;
import com.example.clinicaprivata.Service.Service;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.List;

public class SectiiController {

    @FXML
    private TableView<Sectie> tableSectieView;
    @FXML
    private TableColumn<Sectie,Integer> tableColumnId;
    @FXML
    private TableColumn<Sectie,String> tableColumnNume;
    @FXML
    private TableColumn<Sectie,Integer> tableColumnIdSef;
    @FXML
    private TableColumn<Sectie,Integer> tableColumnPret;
    @FXML
    private TableColumn<Sectie,Integer> tableColumnDurata;
    private Service service;
    private ObservableList<Sectie> modelSectie= FXCollections.observableArrayList();


    public void setSectiiService(Service service)
    {
        this.service=service;
        //service.addObserver(this);
        initModel();
    }


    @FXML
    public void initialize()
    {
        tableColumnId.setCellValueFactory(new PropertyValueFactory<Sectie,Integer>("id"));
        tableColumnNume.setCellValueFactory(new PropertyValueFactory<Sectie,String>("nume"));
        tableColumnIdSef.setCellValueFactory(new PropertyValueFactory<Sectie,Integer>("idSefDeSectie"));
        tableColumnPret.setCellValueFactory(new PropertyValueFactory<Sectie,Integer>("pretPerConsultatie"));
        tableColumnDurata.setCellValueFactory(new PropertyValueFactory<Sectie,Integer>("durataMaximaConsultatie"));
        tableSectieView.setItems(modelSectie);
    }


    @FXML
    public void initModel()
    {
        List<Sectie> sectii=service.getSectii();
        modelSectie.setAll(sectii);
    }


    public void handleProgramareConsultatie(MouseEvent actionEvent) throws IOException
    {
        if(tableSectieView.getSelectionModel().getSelectedItem()!=null)
        {
            Long idSectie=tableSectieView.getSelectionModel().getSelectedItem().getId();
            FXMLLoader fxmllLoader=new FXMLLoader(HelloApplication.class.getResource("views/Consultatie.fxml"));
            Scene scene=new Scene(fxmllLoader.load(),800,500);
            Stage stage=new Stage();
            stage.setScene(scene);
            stage.show();
            ConsultatieController consultatieController= fxmllLoader.getController();
            consultatieController.setConsultatieService(service,idSectie);
        }
    }
}
