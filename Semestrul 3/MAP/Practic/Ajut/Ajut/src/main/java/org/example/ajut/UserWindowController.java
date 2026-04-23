package org.example.ajut;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import org.example.ajut.Domain.Nevoie;
import org.example.ajut.Domain.Persoana;
import org.example.ajut.Service.Service;
import org.example.ajut.Utils.Observer;

import java.time.LocalDateTime;

public class UserWindowController implements Observer {

    private Service service;
    private Persoana persoana;

    @FXML
    TableView<Nevoie> tableNevoi;
    @FXML
    TableView<Nevoie> tableNevoi2;
    @FXML
    TextField titluField;
    @FXML
    TextField descriereField;
    @FXML
    DatePicker deadlineDatePicker;
    @FXML
    Button ajutorButton;
    @FXML
    Button ajutButton;

    @FXML
    TableColumn<Nevoie, String> columnTitlu;
    @FXML
    TableColumn<Nevoie, String> columnDescriere;
    @FXML
    TableColumn<Nevoie, LocalDateTime> columnDeadline;

    @FXML
    TableColumn<Nevoie, String> titluColumn2;
    @FXML
    TableColumn<Nevoie, String> descriereColumn2;
    @FXML
    TableColumn<Nevoie, LocalDateTime> deadlineColumn2;


    public void handleAjutor() {
        String titlu = titluField.getText();
        String descriere = descriereField.getText();
        LocalDateTime deadline = deadlineDatePicker.getValue().atStartOfDay();
        Nevoie nevoie = new Nevoie(titlu, descriere, deadline, persoana.getId(), null, "Caut Erou!");
        service.addNevoie(nevoie);
    }

    public void handleAjutButton() {
        Nevoie nevoie = tableNevoi.getSelectionModel().getSelectedItem();
        if (nevoie != null) {
            nevoie.setOmSalvator(persoana.getId());
            nevoie.setStatus("Ajutat");
            service.updateNevoie(nevoie, persoana);
        }
    }

    public void setService(Service service, Persoana persoana) {
        this.service = service;
        this.persoana = persoana;
        service.addObserver(this);
        initModel();
    }

    @FXML
    private void initialize() {
        //...
    }

    private void initModel() {
        tableNevoi.getItems().clear();
        columnTitlu.setCellValueFactory(new PropertyValueFactory<Nevoie, String>("titlu"));
        columnDescriere.setCellValueFactory(new PropertyValueFactory<Nevoie, String>("descriere"));
        columnDeadline.setCellValueFactory(new PropertyValueFactory<Nevoie, LocalDateTime>("deadline"));
        Iterable<Nevoie> nevoi = service.getNevoiDinOras(persoana);
        for (Nevoie nevoie : nevoi) {
            tableNevoi.getItems().add(nevoie);
        }

        tableNevoi2.getItems().clear();
        titluColumn2.setCellValueFactory(new PropertyValueFactory<Nevoie, String>("titlu"));
        descriereColumn2.setCellValueFactory(new PropertyValueFactory<Nevoie, String>("descriere"));
        deadlineColumn2.setCellValueFactory(new PropertyValueFactory<Nevoie, LocalDateTime>("deadline"));
        Iterable<Nevoie> nevoi2 = service.getNevoiPeCareLeRezolv(persoana);
        for (Nevoie nevoie : nevoi2) {
            tableNevoi2.getItems().add(nevoie);
        }
    }

    @Override
    public void update() {
        initModel();
    }
}
