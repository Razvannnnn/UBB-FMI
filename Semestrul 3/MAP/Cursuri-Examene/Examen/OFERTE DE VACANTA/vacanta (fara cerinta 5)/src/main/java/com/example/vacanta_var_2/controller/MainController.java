package com.example.vacanta_var_2.controller;

import com.example.vacanta_var_2.HelloApplication;
import com.example.vacanta_var_2.domain.Hotel;
import com.example.vacanta_var_2.domain.Location;
import com.example.vacanta_var_2.service.Service;
import javafx.beans.binding.Bindings;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;


import javax.security.auth.callback.Callback;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public class MainController {

    @FXML
    public ComboBox<String> locations;
    @FXML
    public TableView<Hotel> hotelTable;
    @FXML
    private TableColumn<Hotel, String> numeColoana;
    @FXML
    public TableColumn<Hotel, String> typeColoana;
    @FXML
    public TableColumn<Hotel, Integer> pretColoana;
    @FXML
    public TableColumn<Hotel,Integer> roomsColoana;
    @FXML
    public TableColumn<Hotel,Integer> columnX;

    private Service service;

    ObservableList<Hotel> hotelList= FXCollections.observableArrayList();
    ObservableList<String> locationsList= FXCollections.observableArrayList();

    public void setService(Service service) {
        this.service = service;
        initModel();
    }

    @FXML
    public void initialize() {
        locations.setItems(locationsList);

        numeColoana.setCellValueFactory(new PropertyValueFactory<Hotel, String>("name"));
        typeColoana.setCellValueFactory(new PropertyValueFactory<Hotel, String>("type"));
        roomsColoana.setCellValueFactory(new PropertyValueFactory<Hotel, Integer>("rooms"));
        pretColoana.setCellValueFactory(new PropertyValueFactory<Hotel, Integer>("price"));

        hotelTable.setItems(hotelList);
    }
    private void initModel() {
        for(Location location : this.service.getLocations()){
            locationsList.add(location.getName());
        }

        //!! aici nu pun hoteluri ca le pun doar acnd se selecteaza ceva din ComboBox
    }

    public void handleCombo(ActionEvent event) {
        if(locations.getSelectionModel().getSelectedItem() == null)
            return;
        String location = locations.getSelectionModel().getSelectedItem();
        Double lid = service.getLocationId(location);

        hotelList.clear();
        for(Hotel hotel: service.getHotels()){
            if(hotel.getLid().intValue() == lid.intValue()){
                hotelList.add(hotel);
            }
        }
    }

    public void handleOff(ActionEvent event) {
        if(hotelTable.getSelectionModel().getSelectedItem() == null)
            return;
        Hotel hotel = (Hotel) hotelTable.getSelectionModel().getSelectedItem();
        try{
            FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("/views/offerView.fxml"));
            Parent root = (Parent) fxmlLoader.load();
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setTitle("OFERTE PAGE");
            OffController offController = fxmlLoader.getController();

            offController.setService(service,hotel);
            stage.show();
            //((Node)(event.getSource())).getScene().getWindow().hide(); //ca sa ascunda prima fereastra
        } catch(Exception e) {
            MessageAlert.showErrorMessage(null, e.getMessage());
        }
    }


}
