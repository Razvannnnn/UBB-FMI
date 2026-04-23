package org.example.reteasocializare.Controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import org.example.reteasocializare.Domain.Utilizator;
import org.example.reteasocializare.Service.Network;
import org.example.reteasocializare.Service.UtilizatorService;
import org.example.reteasocializare.Utils.Events.UtilizatorEventType;
import org.example.reteasocializare.Utils.Observer.Observable;
import org.example.reteasocializare.Utils.Observer.Observer;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

public class UtilizatorController implements Observer<UtilizatorEventType> {

    UtilizatorService service;
    ObservableList<Utilizator> model = FXCollections.observableArrayList();

    @FXML
    TableView<Utilizator> tableView;
    @FXML
    TableColumn<Utilizator,String> tableColumnFirstName;
    @FXML
    TableColumn<Utilizator,String> tableColumnLastName;

    public void setUtilizatorService(UtilizatorService service) {
        this.service = service;
        this.service.addObserver(this);
        initModel();
    }

    @FXML
    public void initialize() {
        tableColumnFirstName.setCellValueFactory(new PropertyValueFactory<Utilizator, String>("Prenume"));
        tableColumnLastName.setCellValueFactory(new PropertyValueFactory<Utilizator, String>("Nume"));
        tableView.setItems(model);
    }

    private void initModel() {
        Iterable<Utilizator> messages = service.getAll();
        List<Utilizator> utilizatori = StreamSupport.stream(messages.spliterator(), false)
                .collect(Collectors.toList());
        model.setAll(utilizatori);
    }
    @Override
    public void update(UtilizatorEventType utilizatorEventType) {
        initModel();
    }

    public void handleAddUtilizator() {

    }

    public void handleDeleteUtilizator() {

    }

    public void handleUpdateUtilizator() {

    }

}
