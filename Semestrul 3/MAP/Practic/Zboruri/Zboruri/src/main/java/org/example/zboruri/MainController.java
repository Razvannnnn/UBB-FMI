package org.example.zboruri;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import org.example.zboruri.Domain.Client;
import org.example.zboruri.Domain.Flight;
import org.example.zboruri.Service.Service;
import org.example.zboruri.Utils.Observer;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

public class MainController implements Observer {

    private Service service;
    private Client client;

    private Integer pageNumber = 0;
    private Integer pageSize = 5;

    @FXML
    Label usernameLabel;
    @FXML
    ComboBox<String> fromComboBox;
    @FXML
    ComboBox<String> toComboBox;
    @FXML
    DatePicker datePicker;
    @FXML
    TableView tabelZboruri;

    @FXML
    TableColumn<Flight, String> fromColumn;
    @FXML
    TableColumn<Flight, String> toColumn;
    @FXML
    TableColumn<Flight, LocalDateTime> departureTimeColumn;
    @FXML
    TableColumn<Flight, LocalDateTime> landingTimeColumn;
    @FXML
    TableColumn<Flight, Integer> seatsColumn;
    @FXML
    TableColumn<Flight, Integer> locuriDisponibileColumn;
    @FXML
    Button buttonBuy;
    @FXML
    Button buttonNext;
    @FXML
    Button buttonPrevious;
    @FXML
    Label pageNumberLabel;

    @FXML
    public void initialize() {

    }

    @FXML
    public void handleBuyTicket() {
        Flight flight = (Flight) tabelZboruri.getSelectionModel().getSelectedItem();
        if(flight != null) {
            service.buyTicket(flight, client);
        }
    }

    @FXML
    public void handleNext() {
        pageNumber++;
        initModel();
    }

    @FXML
    public void handlePrevious() {
        pageNumber--;
        initModel();
    }

    public void setService(Service service, Client client) {
        this.service = service;
        this.client = client;
        this.service.addObserver(this);
        initModel();
    }

    public void initModel() {
        usernameLabel.setText(client.getName());
        Iterable<String> fromCities = service.getFromCities();
        for(String city: fromCities){
            fromComboBox.getItems().add(city);
        }
        Iterable<String> toCities = service.getToCities();
        for(String city: toCities){
            toComboBox.getItems().add(city);
        }
        fromColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getFrom()));
        toColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getTo()));
        departureTimeColumn.setCellValueFactory(new PropertyValueFactory<Flight, LocalDateTime>("departureTime"));
        landingTimeColumn.setCellValueFactory(new PropertyValueFactory<Flight, LocalDateTime>("landingTime"));
        seatsColumn.setCellValueFactory(new PropertyValueFactory<Flight, Integer>("seats"));
        locuriDisponibileColumn.setCellValueFactory(cellData -> new SimpleIntegerProperty(cellData.getValue().getSeats() - service.numberOfTicketsForFlight(cellData.getValue().getId())).asObject());

        buttonPrevious.setDisable(pageNumber == 0);
        buttonNext.setDisable((pageNumber + 1) * pageSize >= service.getAllFlights().spliterator().getExactSizeIfKnown());

        String from = fromComboBox.getValue();
        String to = toComboBox.getValue();
        LocalDateTime date = datePicker.getValue() != null ? datePicker.getValue().atStartOfDay() : null;

        if(from != null && to != null && date != null) {
            Iterable<Flight> flights = service.getAllFlightsFilter(from, to, date);
            tabelZboruri.getItems().clear();
            for(Flight f: flights){
                tabelZboruri.getItems().add(f);
            }
        } else {
            Iterable<Flight> flights = service.getAllFlights();
            tabelZboruri.getItems().clear();
            for(Flight f: flights){
                tabelZboruri.getItems().add(f);
            }
        }
        pageNumberLabel.setText("Page " + (pageNumber + 1) + " of " + (int) Math.ceil((double) service.getAllFlights().spliterator().getExactSizeIfKnown() / pageSize));
    }



    @Override
    public void update() {
        initModel();
    }
}
