package iss.parkingapp;

import iss.parkingapp.domain.LocParcare;
import iss.parkingapp.domain.Masina;
import iss.parkingapp.domain.Rezervare;
import iss.parkingapp.domain.Utilizator;
import iss.parkingapp.service.Service;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import javafx.util.Duration;
import javafx.util.StringConverter;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.stream.Collectors;

public class MainController {
    private Service service;
    private Utilizator user;
    private final ObservableList<Rezervare> model = FXCollections.observableArrayList();
    private final ObservableList<Masina> modelMasini = FXCollections.observableArrayList();
    private final ObservableList<LocParcare> modelLocuriParcare = FXCollections.observableArrayList();

    // Main menu
    @FXML
    private TableView<Rezervare> tabelRezervarileMele;
    @FXML
    private TableColumn<Rezervare, LocParcare> columnLocParcare;
    @FXML
    private TableColumn<Rezervare, Masina> columnMasina;
    @FXML
    private TableColumn<Rezervare, LocalDateTime> columnRezervare;
    @FXML
    private TableColumn<Rezervare, LocalDateTime> columnSfarsitRezervare;
    @FXML
    private ComboBox<LocParcare> comboBoxLocParcare;
    @FXML
    private ComboBox<Masina> comboBoxMasina;
    @FXML
    private ComboBox<String> comboBoxDurata;
    @FXML
    private CheckBox checkBoxAcum;
    @FXML
    private CheckBox checkBoxRezervare;
    @FXML
    private DatePicker datePicker;
    @FXML
    private ComboBox<String> comboBoxOra;
    @FXML
    private Button buttonRezervare;

    //Profile
    @FXML
    private TableView<Masina> tableViewMasini;
    @FXML
    private TableColumn<Masina, String> columnNrInmatr;
    @FXML
    private TableColumn<Masina, String> columnMarca;
    @FXML
    private TableColumn<Masina, String> columnModel;
    @FXML
    private TextField textFieldNr;
    @FXML
    private TextField textFieldMarca;
    @FXML
    private TextField textFieldModel;
    @FXML
    private Label labelProfil;
    @FXML
    private Button buttonIstoric;
    @FXML
    private Button buttonSterge;

    @FXML
    public void initialize() {
        columnLocParcare.setCellFactory(column -> new TableCell<>() {
            @Override
            protected void updateItem(LocParcare loc, boolean empty) {
                super.updateItem(loc, empty);
                if (empty) {
                    setText(null);
                } else {
                    Rezervare rezervare = getTableView().getItems().get(getIndex());
                    LocParcare lp = modelLocuriParcare.stream()
                            .filter(locP -> locP.getId_loc_parcare() == rezervare.getId_loc_parcare())
                            .findFirst()
                            .orElse(null);
                    setText(lp != null ? lp.getPozitie() : "N/A");
                }
            }
        });

        columnMasina.setCellFactory(column -> new TableCell<>() {
            @Override
            protected void updateItem(Masina masina, boolean empty) {
                super.updateItem(masina, empty);
                if (empty) {
                    setText(null);
                } else {
                    Rezervare rezervare = getTableView().getItems().get(getIndex());
                    Masina m = modelMasini.stream()
                            .filter(mas -> mas.getId_masina() == rezervare.getId_masina())
                            .findFirst()
                            .orElse(null);
                    setText(m != null ? m.getNr_inmatriculare() : "N/A");
                }
            }
        });


        columnRezervare.setCellValueFactory(new PropertyValueFactory<>("data_rezervare"));
        columnSfarsitRezervare.setCellValueFactory(new PropertyValueFactory<>("data_sfarsit_rezervare"));
        tabelRezervarileMele.setItems(model);

        columnNrInmatr.setCellValueFactory(new PropertyValueFactory<>("nr_inmatriculare"));
        columnMarca.setCellValueFactory(new PropertyValueFactory<>("marca"));
        columnModel.setCellValueFactory(new PropertyValueFactory<>("model"));
        tableViewMasini.setItems(modelMasini);

        tableViewMasini.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            buttonSterge.setDisable(newSelection == null);
        });

    }



    public void setComboBoxLocParcare(ComboBox<LocParcare> comboBoxLocParcare) {
        comboBoxLocParcare.setItems(modelLocuriParcare);
        comboBoxLocParcare.setConverter(new StringConverter<>() {
            @Override
            public String toString(LocParcare locParcare) {
                return locParcare == null ? "" : locParcare.getPozitie();
            }

            @Override
            public LocParcare fromString(String string) {
                return comboBoxLocParcare.getItems().stream()
                        .filter(loc -> loc.getPozitie().equals(string))
                        .findFirst()
                        .orElse(null);
            }
        });
    }

    @FXML
    public void handleCheckBoxAcum() {
        if (checkBoxAcum.isSelected()) {
            checkBoxRezervare.setSelected(false);
            datePicker.setDisable(true);
            comboBoxOra.setDisable(true);
        }
    }

    @FXML
    public void handleCheckBoxRezervare() {
        if (checkBoxRezervare.isSelected()) {
            checkBoxAcum.setSelected(false);
            datePicker.setDisable(false);
            comboBoxOra.setDisable(false);
        }
    }


    public void setComboBoxMasina(ComboBox<Masina> comboBoxMasina) {
        comboBoxMasina.setItems(modelMasini);
        comboBoxMasina.setConverter(new StringConverter<>() {
            @Override
            public String toString(Masina masina) {
                return masina == null ? "" : masina.getNr_inmatriculare();
            }

            @Override
            public Masina fromString(String string) {
                return comboBoxMasina.getItems().stream()
                        .filter(masina -> masina.getNr_inmatriculare().equals(string))
                        .findFirst()
                        .orElse(null);
            }
        });
    }

    public void updateTabels() {
        var rezervari = service.getAllRezervariForUser(user);
        var masini = service.getAllMasiniForUser(user);
        var locuri = service.getAllLocuriParcare();

        System.out.println("DEBUG: Rezervari: " + rezervari.size());
        System.out.println("DEBUG: Masini: " + masini.size());
        System.out.println("DEBUG: Locuri parcare: " + locuri.size());

        model.setAll(rezervari);
        modelMasini.setAll(masini);
        modelLocuriParcare.setAll(locuri);

        tabelRezervarileMele.setItems(model);
        tableViewMasini.setItems(modelMasini);

        setComboBoxMasina(comboBoxMasina);
        setComboBoxLocParcare(comboBoxLocParcare);
    }


    public void initModel() {
        labelProfil.setText("Bun venit " + user.getNume() + " " + user.getPrenume() + "!");
        labelProfil.setAlignment(javafx.geometry.Pos.CENTER);
        updateTabels();
        comboBoxDurata.setItems(FXCollections.observableArrayList("1 ora", "2 ore", "3 ore", "4 ore", "5 ore", "6 ore", "1 zi", "2 zile", "3 zile"));
        comboBoxOra.setItems(FXCollections.observableArrayList("08:00", "09:00", "10:00", "11:00", "12:00", "13:00", "14:00", "15:00", "16:00", "17:00"));
        checkBoxAcum.setSelected(true);
        checkBoxRezervare.setSelected(false);
        datePicker.setValue(LocalDateTime.now().toLocalDate());
        datePicker.setDisable(true);
        comboBoxOra.setDisable(true);
    }

    @FXML
    public void handleRezervare() {
        LocParcare locParcare = comboBoxLocParcare.getValue();
        Masina masina = comboBoxMasina.getValue();
        String perioada = comboBoxDurata.getValue();
        LocalDateTime dataInceput = null;
        LocalDateTime dataSfarsit = null;

        if (checkBoxAcum.isSelected()) {
            dataInceput = LocalDateTime.now();

            String[] parts = perioada.split(" ");
            int value = Integer.parseInt(parts[0]);
            String unit = parts[1];
            switch (unit) {
                case "ore", "ora":
                    dataSfarsit = dataInceput.plusHours(value);
                    break;
                case "zile", "zi":
                    dataSfarsit = dataInceput.plusDays(value);
                    break;
                default:
                    Alert alert = new Alert(Alert.AlertType.ERROR, "Perioada invalida!");
                    alert.showAndWait();
                    return;
            }
        } else if (checkBoxRezervare.isSelected()) {
            dataInceput = datePicker.getValue().atStartOfDay();

            String[] parts = perioada.split(" ");
            int value = Integer.parseInt(parts[0]);
            String unit = parts[1];
            switch (unit) {
                case "ora", "ore":
                    dataSfarsit = dataInceput.plusHours(value);
                    break;
                case "zile", "zi":
                    dataSfarsit = dataInceput.plusDays(value);
                    break;
                default:
                    Alert alert = new Alert(Alert.AlertType.ERROR, "Perioada invalida!");
                    alert.showAndWait();
                    return;
            }
        }

        Rezervare rezervare = new Rezervare(user.getId_utilizator(), locParcare.getId_loc_parcare(), masina.getId_masina(), dataInceput, dataSfarsit, "active");
        service.addRezervare(rezervare);
        updateTabels();
    }

    @FXML
    public void handleAdaugaMasina() {
        String nrInmatriculare = textFieldNr.getText();
        String marca = textFieldMarca.getText();
        String model = textFieldModel.getText();
        if (nrInmatriculare.isEmpty() || marca.isEmpty() || model.isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.ERROR, "Toate campurile sunt obligatorii!");
            alert.showAndWait();
            return;
        }
        Masina masina = new Masina(user.getId_utilizator(), nrInmatriculare, marca, model);
        service.addMasina(masina);
        modelMasini.setAll(service.getAllMasiniForUser(user));
        textFieldNr.clear();
        textFieldMarca.clear();
        textFieldModel.clear();
        updateTabels();
    }

    @FXML
    public void handleIstoric() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("istoric.fxml"));
            Parent root = loader.load();

            IstoricController istoricController = loader.getController();
            istoricController.setService(service, user);

            Stage stage = new Stage();
            stage.setTitle("Istoric Rezervari");
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            // Poți afișa alertă de eroare dacă vrei
        }
    }

    @FXML
    public void handleStergeMasina() {
        Masina masinaSelectata = tableViewMasini.getSelectionModel().getSelectedItem();
        if (masinaSelectata != null) {
            Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
            confirm.setTitle("Confirmare ștergere");
            confirm.setHeaderText("Ești sigur că vrei să ștergi această mașină?");
            confirm.setContentText(masinaSelectata.getNr_inmatriculare());

            confirm.showAndWait().ifPresent(result -> {
                if (result == ButtonType.OK) {
                    service.stergeMasina(masinaSelectata); // Presupune că ai această metodă în service
                    updateTabels();
                    buttonSterge.setDisable(true);
                }
            });
        }
    }

    @FXML
    public void handleEditProfil() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("edit.fxml"));
            Parent root = loader.load();

            EditController editController = loader.getController();
            editController.setService(service, user);

            Stage stage = new Stage();
            stage.setTitle("Editare Profil");
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public void startAutoUpdateExpiredRezervari() {
        Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(30), event -> {
            service.updateExpiredRezervari();
            updateTabels();
        }));
        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.play();
    }

    public void setService(Service service, Utilizator user) {
        this.service = service;
        this.user = user;
        initModel();
        startAutoUpdateExpiredRezervari();
    }
}
