package problema8.client.gui;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.util.StringConverter;
import problema8.model.AgeGroup;
import problema8.model.Child;
import problema8.model.Event;
import problema8.model.User;
import problema8.services.Service;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public class MainController {
    private Service service;
    private User user;
    private final ObservableList<Child> model = FXCollections.observableArrayList();

    @FXML
    private ComboBox<AgeGroup> comboBoxVarsta;
    @FXML
    private ComboBox<Event> comboBoxProbe;
    @FXML
    private ComboBox<AgeGroup> comboBoxVarsta1;
    @FXML
    private ComboBox<Event> comboBoxProbe1;
    @FXML
    private ComboBox<AgeGroup> comboBoxVarsta2;
    @FXML
    private ComboBox<Event> comboBoxProbe2;

    @FXML
    private Button buttonLogout;
    @FXML
    private Button buttonInscriere;
    @FXML
    private TextField textFieldNume;
    @FXML
    private TextField textFieldCNP;

    @FXML
    private TableColumn<Child, String> cnpChild;
    @FXML
    private TableColumn<Child, String> nameChild;
    @FXML
    private TableView<Child> tableView;

    public void setService(Service service, User user) {
        this.service = service;
        this.user = user;
        initModel();
        setupListeners();
    }

    @FXML
    public void initialize() {
        cnpChild.setCellValueFactory(new PropertyValueFactory<>("CNP"));
        nameChild.setCellValueFactory(new PropertyValueFactory<>("name"));
        tableView.setItems(model);
    }

    private void initModel() {
        Iterable<AgeGroup> ageGroupsIterable = service.getAgeGroups();
        List<AgeGroup> ageGroups = StreamSupport.stream(ageGroupsIterable.spliterator(), false)
                .collect(Collectors.toList());

        initAgeGroupComboBox(comboBoxVarsta, ageGroups);
        initAgeGroupComboBox(comboBoxVarsta1, ageGroups);
        initAgeGroupComboBox(comboBoxVarsta2, ageGroups);
    }

    private void initAgeGroupComboBox(ComboBox<AgeGroup> comboBox, List<AgeGroup> ageGroups) {
        comboBox.getItems().clear();
        comboBox.getItems().addAll(ageGroups);
        comboBox.setConverter(new StringConverter<>() {
            @Override
            public String toString(AgeGroup ageGroup) {
                return ageGroup != null ? ageGroup.getName() : "";
            }

            @Override
            public AgeGroup fromString(String string) {
                return null;
            }
        });
    }

    private void setupListeners() {
        comboBoxVarsta.getSelectionModel().selectedItemProperty().addListener((obs, oldValue, newValue) -> {
            if (newValue != null) {
                updateEventsByAgeGroup(newValue.getId(), comboBoxProbe);
                model.clear(); // Clear the table when a new age group is selected
            }
        });

        comboBoxProbe.getSelectionModel().selectedItemProperty().addListener((obs, oldValue, newValue) -> {
            if (newValue != null) {
                updateChildrenByEvent(newValue.getId());
            }
        });

        comboBoxVarsta1.getSelectionModel().selectedItemProperty().addListener((obs, oldValue, newValue) -> {
            if (newValue != null) updateEventsByAgeGroup(newValue.getId(), comboBoxProbe1);
        });

        comboBoxVarsta2.getSelectionModel().selectedItemProperty().addListener((obs, oldValue, newValue) -> {
            if (newValue != null) updateEventsByAgeGroup(newValue.getId(), comboBoxProbe2);
        });
    }

    private void updateEventsByAgeGroup(Long ageGroupId, ComboBox<Event> comboBoxProbe) {
        comboBoxProbe.getItems().clear();
        Iterable<Event> events = service.getEventsByAgeGroup(ageGroupId);
        for (Event event : events) {
            comboBoxProbe.getItems().add(event);
        }

        comboBoxProbe.setConverter(new StringConverter<>() {
            @Override
            public String toString(Event event) {
                return event != null ? event.getName() : "";
            }

            @Override
            public Event fromString(String string) {
                return null;
            }
        });
    }

    private void updateChildrenByEvent(Long eventId) {
        model.clear();
        Iterable<Child> children = service.getChildrenByEvent(eventId);
        for (Child child : children) {
            model.add(child);
        }
        tableView.setItems(model);
    }

    public void handleButtonLogout() {
        System.out.println("Logout successful");
        buttonLogout.getScene().getWindow().hide();
    }

    public void handleButtonInscriere() {
        String nume = textFieldNume.getText();
        String cnp = textFieldCNP.getText();

        AgeGroup ageGroup1 = comboBoxVarsta1.getValue();
        Event eventName1 = comboBoxProbe1.getValue();

        AgeGroup ageGroup2 = comboBoxVarsta2.getValue();
        Event eventName2 = comboBoxProbe2.getValue();

        if (ageGroup1 != null && eventName1 != null) {
            service.saveChildAndEnrollment(nume, cnp, eventName1);
        }

        if (ageGroup2 != null && eventName2 != null) {
            service.saveChildAndEnrollment(nume, cnp, eventName2);
        }

        if (ageGroup1 == null || eventName1 == null) {
            System.out.println("Inscriere failed");
        } else {
            System.out.println("Inscriere successful");
        }
    }
}
