package com.example.rezerva.controller;

import com.example.rezerva.domain.City;
import com.example.rezerva.domain.TrainStation;
import com.example.rezerva.service.Service;
import com.example.rezerva.utils.observer.Observer;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class ClientController implements Observer {
    private Service service;
    @FXML
    private ComboBox departureCombo;
    @FXML
    private ComboBox destinationCombo;

    @FXML
    private Button cautaButton;
    @FXML
    private ListView listView;
    @FXML
    private Label textCasuta=new Label();

    private String idOm;
    private Double constanta=10.0;

    private Double calcul(Integer nrStatii){
        Double rez=constanta*nrStatii;
        return rez;
    }
    ObservableList<String> model= FXCollections.observableArrayList();

    public void setService(Service service) {
        this.service = service;
        initModel();
        service.addObserver(this);
        this.idOm=(String) UUID.randomUUID().toString();
    }

    @FXML
    public void initialize() {
        listView.setItems(model);
    }

    private void initModel() {
        setCombo();
        //handleSearch();
        updateCasuta();
    }

    private void updateCasuta(){
        if(departureCombo.getValue() != null && destinationCombo.getValue()!=null) {
            String from = departureCombo.getValue().toString();
            String to = destinationCombo.getValue().toString();
            Integer cnt=service.nrPersoane(from,to);
            textCasuta.setText("SUNT " + cnt.toString() + " PERSOANE CU ACEEASI CAUTARE");
        }
        else
            textCasuta.setText("MOMENTAN NU AVETI CAUTARI!");

    }

    public void setCombo(){
        Set<String> from = new HashSet<>();
        Set<String> to = new HashSet<>();
        for(City c : service.getCities()) {
            from.add(c.getNume());
            to.add(c.getNume());
        }
        departureCombo.getItems().addAll(from);
        destinationCombo.getItems().addAll(to);
    }

    public void handleSearch() {
        if(departureCombo.getValue() != null && destinationCombo.getValue()!=null) {
            //service.notifyObservers();
            String from = departureCombo.getValue().toString();
            String to = destinationCombo.getValue().toString();
            model.clear();
            for (TrainStation ts : service.getTrainStations()) {
                String dep=ts.getDeparture();
                String dest=ts.getDestination();
                if (service.getNameById(ts.getDeparture()).equals(from) && service.getNameById(ts.getDestination()).equals(to)) {
                        //model.add(ts);
                        model.add(service.getNameById(ts.getDeparture()) + " -- " + ts.getId() + " -> " + service.getNameById(ts.getDestination()) + " PRET: " +calcul(1).toString());
                        //System.out.println(service.getNameById(ts.getDeparture()) + " -- " + ts.getId() + " -> " + service.getNameById(ts.getDestination()));
                }
            }

            service.addCautare(idOm,from,to);
            service.notifyObservers();
        }
    }

    @Override
    public void update() {
        initModel();
        //updateCasuta();
    }
}
