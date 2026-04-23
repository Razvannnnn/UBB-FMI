package com.example.clinicaprivata.Controller;

import com.example.clinicaprivata.Entities.Consultatie;
import com.example.clinicaprivata.Entities.Medic;
import com.example.clinicaprivata.Events.ConsultatieChangeEvent;
import com.example.clinicaprivata.Observer.Observer;
import com.example.clinicaprivata.Service.Service;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.time.LocalDate;
import java.time.LocalTime;

public class MediciController implements Observer<ConsultatieChangeEvent> {

    @FXML
    private TableView<Consultatie> tableConsultatieView;
    @FXML
    private TableColumn<Consultatie,Integer> tableColumnIdMedic;
    @FXML
    private TableColumn<Consultatie,String> tableColumnCnpPacient;
    @FXML
    private TableColumn<Consultatie,String> tableColumnNumePacient;
    @FXML
    private TableColumn<Consultatie,LocalDate> tableColumnData;
    @FXML
    private TableColumn<Consultatie,LocalTime> tableColumnOra;
    private Service service;
    private Medic medic;
    private ObservableList<Consultatie> modelConsultatie= FXCollections.observableArrayList();


    public void setMediciService(Service service,Medic medic)
    {
        this.service=service;
        this.medic=medic;
        service.addObserver(this);
        initModel();
    }


    @FXML
    public void initialize()
    {
        tableColumnIdMedic.setCellValueFactory(new PropertyValueFactory<Consultatie,Integer>("idMedic"));
        tableColumnCnpPacient.setCellValueFactory(new PropertyValueFactory<Consultatie,String>("cnpPacient"));
        tableColumnNumePacient.setCellValueFactory(new PropertyValueFactory<Consultatie,String>("numePacient"));
        tableColumnData.setCellValueFactory(new PropertyValueFactory<Consultatie, LocalDate>("data"));
        tableColumnOra.setCellValueFactory(new PropertyValueFactory<Consultatie, LocalTime>("ora"));
        tableConsultatieView.setItems(modelConsultatie);
    }


    @FXML
    public void initModel()
    {
        modelConsultatie.setAll(service.sortareConsultatii(medic));
    }

    @Override
    public void update(ConsultatieChangeEvent consultatieChangeEvent)
    {
        initModel();
    }
}
