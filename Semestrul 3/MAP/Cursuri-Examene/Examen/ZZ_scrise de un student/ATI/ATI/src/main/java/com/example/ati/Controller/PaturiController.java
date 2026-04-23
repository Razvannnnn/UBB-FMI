package com.example.ati.Controller;

import com.example.ati.Entities.Pat;
import com.example.ati.Events.PatChangeEvent;
import com.example.ati.Observer.Observer;
import com.example.ati.Serivice.Service;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.util.List;

public class PaturiController implements Observer<PatChangeEvent> {
    @FXML
    private Label labelPaturiOcupate;
    @FXML
    private Label labelPaturiTicLibere;
    @FXML
    private Label labelPaturiTimLibere;
    @FXML
    private Label labelPaturiTipLibere;
    @FXML
    private TextField cnpField;
    @FXML
    private TableView<Pat> tablePatView;
    @FXML
    private TableColumn<Pat,String> tableColumnCnp;
    @FXML
    private TableColumn<Pat,String> tableColumnIdPat;

    private Service service;
    private ObservableList<Pat> modelPat=FXCollections.observableArrayList();


    public void setPaturiService(Service service)
    {
        this.service=service;
        service.addObserver(this);
        initModel();
    }


    @FXML
    public void initialize()
    {
        tableColumnIdPat.setCellValueFactory(new PropertyValueFactory<Pat,String>("id"));
        tableColumnCnp.setCellValueFactory(new PropertyValueFactory<Pat,String>("pacient"));
        tablePatView.setItems(modelPat);
    }


    @FXML
    public void initModel()
    {
        List<Pat> paturi=service.getPaturiOcupate();
        modelPat.setAll(paturi);
        int paturiOcupateTotal,paturiLibereTim,paturiLibereTic,paturiLibereTip;
        paturiLibereTic=14-service.getBusyTicNumber();
        paturiLibereTim=8-service.getBusyTimNumber();
        paturiLibereTip=8-service.getBusyTipNumber();
        paturiOcupateTotal=30-(paturiLibereTic+paturiLibereTip+paturiLibereTim);
        labelPaturiOcupate.setText("Paturi ocupate:"+ paturiOcupateTotal);
        labelPaturiTicLibere.setText("TIC "+paturiLibereTic+" paturi libere");
        labelPaturiTimLibere.setText("TIM "+paturiLibereTim+" paturi libere");
        labelPaturiTipLibere.setText("TIP "+paturiLibereTip+" paturi libere");
    }

    @Override
    public void update(PatChangeEvent patChangeEvent)
    {
        initModel();
        initialize();
    }

    public static void showErrorMessage()
    {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Atentie!");
        alert.setContentText("Pacientul cu CNP-ul introdus nu exista!");
        alert.show();
    }

    public void handleEliberarePatButton(ActionEvent actionEvent)
    {
        String cnp=cnpField.getText();
        if(service.eliberarePat(cnp))
            update(null);
        else
            showErrorMessage();
    }
}
