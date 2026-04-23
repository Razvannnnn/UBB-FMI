package com.example.vacanta_var_2.controller;

import com.example.vacanta_var_2.domain.Client;
import com.example.vacanta_var_2.domain.Hotel;
import com.example.vacanta_var_2.domain.Location;
import com.example.vacanta_var_2.domain.Offers;
import com.example.vacanta_var_2.service.Service;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.time.LocalDate;

public class ClientController {
    @FXML
    public TableView<Offers> tableView;
    @FXML
    private TableColumn<Offers, String> hotelColoana;
    @FXML
    public TableColumn<Offers, String> locatieColoana;
    @FXML
    public TableColumn<Offers, LocalDate> startdateColoana;
    @FXML
    public TableColumn<Offers,LocalDate> enddateColoana;

    //---------------------------------
    @FXML
    public TableView<Hotel> hotelTable;
    @FXML
    public TableColumn<Hotel,String> numeHotelColoana;
    @FXML
    public DatePicker dataPicker;
    @FXML
    public TextField nrNoptiField;


    private Service service;
    private Long idClient;

    ObservableList<Offers> model= FXCollections.observableArrayList();
    ObservableList<Hotel> modelHotel= FXCollections.observableArrayList();

    public void setService(Service service,Long idClient) {
        this.service = service;
        this.idClient=idClient;
        initModel();
    }

    @FXML
    public void initialize() {
        tableView.setItems(model);

        startdateColoana.setCellValueFactory(new PropertyValueFactory<Offers, LocalDate>("start"));
        enddateColoana.setCellValueFactory(new PropertyValueFactory<Offers, LocalDate>("end"));

        hotelColoana.setCellValueFactory(c -> {
            Offers off=c.getValue();
            for(Hotel h: service.getHotels())
            {
                if(off.getHotel().equals(h.getId()))
                    return new ReadOnlyObjectWrapper<String>(h.getName());
            }
            return null;
        });

        locatieColoana.setCellValueFactory(c -> {
            Offers off=c.getValue();
            for(Hotel h: service.getHotels())
            {
                if(off.getHotel().equals(h.getId())){
                    Double idLocatie=h.getLid();
                    for(Location l: service.getLocations())
                        if(l.getId().equals(idLocatie))
                            return new ReadOnlyObjectWrapper<String>(l.getName());
                }
            }
            return null;
        });

        tableView.setItems(model);


        hotelTable.setItems(modelHotel);
        numeHotelColoana.setCellValueFactory(new PropertyValueFactory<Hotel, String>("name"));
    }

    private void initModel() {
        for(Offers off : this.service.getOffersAvailable(idClient)){
            model.add(off);
        }

        for(Hotel h : this.service.getHotels()){
            modelHotel.add(h);
        }
    }

    public void handleRezerva(ActionEvent event){
        if(hotelTable.getSelectionModel().getSelectedItem() == null)
            return;
        Hotel hotel = hotelTable.getSelectionModel().getSelectedItem();

        LocalDate start = dataPicker.getValue();
        Integer noNights = Integer.parseInt(nrNoptiField.getText());
        Double idHotel=hotel.getId();

        service.adaugaRezervare(idClient,idHotel,start,noNights);
    }
}
