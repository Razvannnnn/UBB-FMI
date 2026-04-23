package com.example.ati.Controller;

import com.example.ati.Entities.Pacient;
import com.example.ati.Events.PatChangeEvent;
import com.example.ati.Observer.Observer;
import com.example.ati.Serivice.Service;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;

import java.util.List;

public class PacientiController implements Observer<PatChangeEvent> {

    @FXML
    private TableView<Pacient> tablePacientView;
    @FXML
    private TableColumn<Pacient,String> tableColumnCnp;
    @FXML
    private TableColumn<Pacient,String> tableColumnDiagnostic;
    @FXML
    private TableColumn<Pacient,Integer> tableColumnGravitate;
    @FXML
    private Button placeTic;
    @FXML
    private Button placeTim;
    @FXML
    private Button placeTip;
    private Service service;
    private PaturiController paturiController;
    private ObservableList<Pacient> modelPacient= FXCollections.observableArrayList();


    public void setPacientiService(Service service,PaturiController paturiController)
    {
        this.service=service;
        this.paturiController=paturiController;
        service.addObserver(this);
        initModel();
    }


    @FXML
    public void initialize()
    {
        tableColumnCnp.setCellValueFactory(new PropertyValueFactory<Pacient,String>("cnp"));
        tableColumnDiagnostic.setCellValueFactory(new PropertyValueFactory<Pacient,String>("diagnostic"));
        tableColumnGravitate.setCellValueFactory(new PropertyValueFactory<Pacient,Integer>("gravitate"));
        tablePacientView.setItems(modelPacient);
    }


    @FXML
    public void initModel()
    {
        placeTic.setDisable(true);
        placeTim.setDisable(true);
        placeTip.setDisable(true);
        List<Pacient> pacienti=service.getIdPacientiInAsteptare();
        modelPacient.setAll(pacienti);
    }

    @Override
    public void update(PatChangeEvent PatChangeEvent)
    {
        initialize();
        initModel();
    }


    public void handleActivateButtons(MouseEvent actionEvent)
    {
        if(tablePacientView.getSelectionModel().getSelectedItem()!=null)
        {
            placeTic.setDisable(false);
            placeTim.setDisable(false);
            placeTip.setDisable(false);
        }
    }


    public static void showErrorMessage()
    {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Atenție!");
        alert.setContentText("Tipul de pat selectat nu este disponibil");
        alert.show();
    }


    public void handleAtribuirePatTic(ActionEvent actionEvent)
    {
        Pacient p=(Pacient)tablePacientView.getSelectionModel().getSelectedItem();
        if(service.checkBedAvailability("1"))
        {
            service.atribuiePatPacient(p.getCnp(),"1");
            update(null);
            paturiController.initModel();
        }
        else
        {
            showErrorMessage();
        }
    }


    public void handleAtribuirePatTim(ActionEvent actionEvent)
    {
        Pacient p=(Pacient)tablePacientView.getSelectionModel().getSelectedItem();
        if(service.checkBedAvailability("2"))
        {
            service.atribuiePatPacient(p.getCnp(),"2");
            update(null);
            paturiController.initModel();
        }
        else
        {
            showErrorMessage();
        }
    }


    public void handleAtribuirePatTip(ActionEvent actionEvent)
    {
        Pacient p=(Pacient)tablePacientView.getSelectionModel().getSelectedItem();
        if(service.checkBedAvailability("3"))
        {
            service.atribuiePatPacient(p.getCnp(),"3");
            update(null);
            paturiController.initModel();
        }
        else
        {
            showErrorMessage();
        }
    }
}