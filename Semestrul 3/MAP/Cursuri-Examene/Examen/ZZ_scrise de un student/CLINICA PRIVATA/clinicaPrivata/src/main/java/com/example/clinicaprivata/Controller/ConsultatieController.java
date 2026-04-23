package com.example.clinicaprivata.Controller;

import com.example.clinicaprivata.Entities.Consultatie;
import com.example.clinicaprivata.Entities.Medic;
import com.example.clinicaprivata.Entities.Sectie;
import com.example.clinicaprivata.Service.Service;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public class ConsultatieController {
    @FXML
    private TableView<Medic> tableMedicView;
    @FXML
    private TableColumn<Medic,Integer> tableColumnIdMedic;
    @FXML
    private TableColumn<Medic,String> tableColumnNume;
    @FXML
    private TableColumn<Medic,String> tableColumnRezident;
    @FXML
    private DatePicker datePicker;
    @FXML
    private TextField numeField;
    @FXML
    private TextField cnpField;
    @FXML
    private TextField oraField;
    @FXML
    private Button programeazaButton;




    private Long idSectie;
    private Service service;
    private ObservableList<Medic> modelMedic= FXCollections.observableArrayList();


    public void setConsultatieService(Service service,Long idSectie)
    {
        this.service=service;
        this.idSectie=idSectie;
        initModel();
    }


    @FXML
    public void initialize()
    {
        tableColumnIdMedic.setCellValueFactory(new PropertyValueFactory<Medic,Integer>("id"));
        tableColumnNume.setCellValueFactory(new PropertyValueFactory<Medic,String>("nume"));
        tableColumnRezident.setCellValueFactory(new PropertyValueFactory<Medic,String>("rezident"));
        tableMedicView.setItems(modelMedic);
    }


    @FXML
    public void initModel()
    {
        programeazaButton.setDisable(true);
        List<Medic> medici=service.getMediciSectie(idSectie);
        modelMedic.setAll(medici);
    }


    public void handleProgramarePacient(MouseEvent actionEvent) throws IOException
    {
        if(tableMedicView.getSelectionModel().getSelectedItem()!=null)
        {
            programeazaButton.setDisable(false);
        }
    }


    public void handleProgramareButton(ActionEvent actionEvent)
    {
        String nume=numeField.getText();
        String cnp=cnpField.getText();
        LocalTime ora=LocalTime.parse(oraField.getText());
        LocalDate data=datePicker.getValue();
        Long idMedic=tableMedicView.getSelectionModel().getSelectedItem().getId();
        service.adaugaConsultatie(idMedic,cnp,nume,data,ora);
    }
}