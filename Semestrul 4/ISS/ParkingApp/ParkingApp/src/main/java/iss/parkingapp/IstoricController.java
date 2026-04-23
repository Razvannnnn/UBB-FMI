package iss.parkingapp;

import iss.parkingapp.domain.LocParcare;
import iss.parkingapp.domain.Masina;
import iss.parkingapp.domain.Rezervare;
import iss.parkingapp.domain.Utilizator;
import iss.parkingapp.service.Service;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.time.LocalDateTime;

public class IstoricController {

    private Utilizator user;
    private Service service;
    private final ObservableList<Rezervare> rezervari = FXCollections.observableArrayList();

    @FXML
    private Button buttonCloseWindow;
    @FXML
    private TableView<Rezervare> tableAllRezervari;
    @FXML
    private TableColumn<Rezervare, Long> columnLocParcare;
    @FXML
    private TableColumn<Rezervare, Long> columnMasina;
    @FXML
    private TableColumn<Rezervare, LocalDateTime> columnRezervare;
    @FXML
    private TableColumn<Rezervare, LocalDateTime> columnSfarsitRezervare;
    @FXML
    private TableColumn<Rezervare, String> columnStatus;


    @FXML
    public void initialize() {
        columnLocParcare.setCellValueFactory(new PropertyValueFactory<>("id_loc_parcare"));
        columnMasina.setCellValueFactory(new PropertyValueFactory<>("id_masina"));
        columnRezervare.setCellValueFactory(new PropertyValueFactory<>("data_rezervare"));
        columnSfarsitRezervare.setCellValueFactory(new PropertyValueFactory<>("data_sfarsit_rezervare"));
        columnStatus.setCellValueFactory(new PropertyValueFactory<>("status"));

        columnLocParcare.setCellFactory(column -> new TableCell<Rezervare, Long>() {
            @Override
            protected void updateItem(Long locParcareId, boolean empty) {
                super.updateItem(locParcareId, empty);
                if (empty || locParcareId == null) {
                    setText(null);
                } else {
                    LocParcare loc = service.getLocParcareById(locParcareId);
                    setText(loc != null ? loc.getPozitie() : "Unknown");
                }
            }
        });

        columnMasina.setCellFactory(column -> new TableCell<Rezervare, Long>() {
            @Override
            protected void updateItem(Long masinaId, boolean empty) {
                super.updateItem(masinaId, empty);
                if (empty || masinaId == null) {
                    setText(null);
                } else {
                    Masina masina = service.getMasinaById(masinaId);
                    setText(masina != null ? masina.getNr_inmatriculare() : "Unknown");
                }
            }
        });
    }


    public void setService(Service service, Utilizator user) {
        this.service = service;
        this.user = user;
        initTable();
    }

    public void initTable() {
        var historicRezervari = service.getHistoryRezervariUser(user);
        rezervari.clear();
        rezervari.addAll(historicRezervari);
        tableAllRezervari.setItems(rezervari);
    }

    @FXML
    public void handleButtonCloseWindow() {
        System.out.println("Close button clicked");
        if (buttonCloseWindow.getScene() != null) {
            buttonCloseWindow.getScene().getWindow().hide();
        }
    }


}
