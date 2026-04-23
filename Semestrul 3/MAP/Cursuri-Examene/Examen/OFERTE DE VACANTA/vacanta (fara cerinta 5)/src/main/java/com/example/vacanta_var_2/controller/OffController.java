package com.example.vacanta_var_2.controller;

import com.example.vacanta_var_2.domain.Hotel;
import com.example.vacanta_var_2.domain.Location;
import com.example.vacanta_var_2.domain.Offers;
import com.example.vacanta_var_2.service.Service;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.DatePicker;
import javafx.scene.control.ListView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.time.LocalDate;

public class OffController {
//    @FXML
//    public ListView<Hotel> offList;
    @FXML
    public DatePicker startDate;
    @FXML
    public DatePicker endDate;
    @FXML
    public ListView offList;
    private Service service;
    private Hotel hotel;
    ObservableList<String> offModel= FXCollections.observableArrayList();

    public void setService(Service service,Hotel hotel) {
        this.service = service;
        this.hotel=hotel;
        //initModel();
    }

    @FXML
    public void initialize() {
        offList.setItems(offModel);
    }
    private void initModel() {
//        for(Offers offer : this.service.getOffers()){
//            if(this.hotel==offer.getHotel())
//            offModel.add("start: "+offer.getStart() + ", "+"end:" +offer.getEnd()+", percent: "+offer.getPercent());
//        }
    }

    public void handleSearch(ActionEvent event) {
        if (endDate.getValue() == null || startDate.getValue() == null)
            return;
        LocalDate sd = startDate.getValue();
        LocalDate ed = endDate.getValue();
        offModel.clear();
        for (Offers off : service.getOffers()) {
            if (off.getStart().compareTo(sd)<0 && off.getEnd().compareTo(ed)>0 && this.hotel.getId().equals(off.getHotel())) {
                offModel.add(off.getId() + ":"
                        + off.getPercent() + "% începe la "
                        + off.getStart().toString()
                        + " până la " + off.getEnd().toString());
            }
        }
    }



}
