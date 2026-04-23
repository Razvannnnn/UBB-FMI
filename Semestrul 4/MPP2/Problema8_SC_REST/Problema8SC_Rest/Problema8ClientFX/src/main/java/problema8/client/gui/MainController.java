package problema8.client.gui;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.util.StringConverter;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import problema8.model.*;
import problema8.services.IObserver;
import problema8.services.IService;
import problema8.services.ProbExceptions;

import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public class MainController implements Initializable, IObserver {
    private IService service;
    private User user;
    private final ObservableList<Child> model = FXCollections.observableArrayList();
    private final ObservableList<Map<String, Object>> modelDetails = FXCollections.observableArrayList();
    private static Logger logger = LogManager.getLogger(MainController.class);

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
    @FXML
    private TableView<Map<String, Object>> tableView2;
    @FXML
    private TableColumn<Map<String, Object>, String> ageChild2;
    @FXML
    private TableColumn<Map<String, Object>, String> nameChild2;
    @FXML
    private TableColumn<Map<String, Object>, String> nrEventsChild2;


    public void setService(IService service, User user) {
        this.service = service;
        this.user = user;
        initModel();
        setupListeners();
        loadChildrenWithDetails();
    }

    @FXML
    public void initialize(URL url, ResourceBundle resourceBundle) {
        nameChild.setCellValueFactory(new PropertyValueFactory<>("name"));
        cnpChild.setCellValueFactory(new PropertyValueFactory<>("CNP"));

        nameChild2.setCellValueFactory(cellData ->
                new SimpleStringProperty((String) cellData.getValue().get("name"))
        );
        ageChild2.setCellValueFactory(cellData ->
                new SimpleStringProperty(String.valueOf(cellData.getValue().get("age")))
        );
        nrEventsChild2.setCellValueFactory(cellData ->
                new SimpleStringProperty(String.valueOf(cellData.getValue().get("numberOfEvents")))
        );

        tableView2.setItems(modelDetails);
    }

    private void loadChildrenWithDetails() {
        modelDetails.clear();
        List<Map<String, Object>> childrenDetails = service.getDetailsForAllChildren();
        if (childrenDetails != null) {
            modelDetails.addAll(childrenDetails);
        } else {
            logger.warn("No children details returned.");
        }

        /*

        Iterable<Child> children = service.getChildren();

        if (children != null) {
            for (Child child : children) {
                int age = AgeConverter.getAgeFromCNP(child.getCNP());
                long eventCount = service.getNumberOfEvents(child.getId());

                Map<String, Object> data = new HashMap<>();
                data.put("name", child.getName());
                data.put("age", age);
                data.put("numberOfEvents", eventCount);

                modelDetails.add(data);
            }
        } else {
            logger.warn("No children returned for details view.");
        }

         */
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
        if (events != null) {
            for (Event event : events) {
                comboBoxProbe.getItems().add(event);
            }
        } else {
            logger.warn("No events returned for age group ID: " + ageGroupId);
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

        if (children != null) {
            for (Child child : children) {
                model.add(child);
            }
        } else {
            logger.warn("No children returned for event ID: " + eventId);
        }

        tableView.setItems(model);

        nameChild.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getName()));
        cnpChild.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getCNP()));
    }


    public void handleButtonLogout() {
        try {
            logout();
        } catch (ProbExceptions e) {
            logger.error("Logout error " + e);
        }
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
            //updateChildrenByEvent(eventName1.getId());
        }
    }

    @Override
    public void userLoggedIn(User user) throws ProbExceptions {

    }

    @Override
    public void userLoggedOut(User user) throws ProbExceptions {

    }

    @Override
    public void getChildrenByEvent(List<Child> childrens) throws ProbExceptions {
        logger.info("Received children by event: " + childrens);
        model.clear();
        model.addAll(childrens);
        tableView.setItems(model);
    }

    @Override
    public void saveChildAndEnrollment(Child child, Enrollment enrollment) throws ProbExceptions {
        logger.info("Received child enrollment notification: " + child);
        javafx.application.Platform.runLater(() -> {
            Event selectedEvent = comboBoxProbe.getValue();
            if (selectedEvent != null) {
                updateChildrenByEvent(selectedEvent.getId());
                loadChildrenWithDetails();
            }
        });
        logger.info("Child and enrollment saved: " + child + ", " + enrollment);
    }

    public void logout() {
        try {
            service.logout(user, this);
        } catch (ProbExceptions e) {
            logger.error("Logout error " + e);
        }
    }
}
