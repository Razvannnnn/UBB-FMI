package org.example.ajut;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import org.example.ajut.Domain.Oras;
import org.example.ajut.Domain.Persoana;
import org.example.ajut.Service.Service;
import org.example.ajut.Utils.Observer;

import java.util.ArrayList;
import java.util.Arrays;

public class LoginController implements Observer {
    private Service service;

    @FXML
    TextField numeField;
    @FXML
    TextField prenumeField;
    @FXML
    TextField usernameField;
    @FXML
    PasswordField parolaField;
    @FXML
    TextField stradaField;
    @FXML
    TextField numarstradaField;
    @FXML
    TextField telefonField;
    @FXML
    ComboBox<String> orasComboBox;
    @FXML
    ComboBox<String> userComboBox;
    @FXML
    Button registerButton;
    @FXML
    Button loginButton;


    @FXML
    void handleRegister() {
        String nume = numeField.getText();
        String prenume = prenumeField.getText();
        String username = usernameField.getText();
        String parola = parolaField.getText();
        String strada = stradaField.getText();
        String numarstrada = numarstradaField.getText();
        String telefon = telefonField.getText();
        Oras oras = orasComboBox.getValue() != null ? Oras.valueOf(orasComboBox.getValue()) : null;
        Persoana persoana = new Persoana(nume, prenume, username, parola, oras, strada, numarstrada, telefon);
        service.addPersoana(persoana);
        numeField.clear();
        prenumeField.clear();
        usernameField.clear();
        parolaField.clear();
        stradaField.clear();
        numarstradaField.clear();
        telefonField.clear();
    }
    @FXML
    void handleLogin() {
        String username = userComboBox.getValue();
        Persoana persoana = service.getPersoanaByUsername(username);
        if (persoana != null) {
            try {
                FXMLLoader loader = new FXMLLoader(UserWindowController.class.getResource("userWindow.fxml"));
                AnchorPane root = loader.load();

                Stage stage = new Stage();
                stage.setTitle("User Window");
                Scene scene = new Scene(root);
                stage.setScene(scene);

                UserWindowController controller = loader.getController();
                controller.setService(service, persoana);

                Stage currentStage = (Stage) loginButton.getScene().getWindow();
                currentStage.close();
                stage.show();

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @FXML
    private void initialize() {

    }

    public void initModel() {
        orasComboBox.getItems().clear();
        orasComboBox.getItems().addAll(Arrays.stream(Oras.values()).map(Enum::name).toArray(String[]::new));
        userComboBox.getItems().clear();
        Iterable<Persoana> persoane = new ArrayList<>();
        persoane = service.getAllPersoane();
        for (Persoana persoana : persoane) {
            userComboBox.getItems().add(persoana.getUsername());
        }
    }

    public void setService(Service service) {
        this.service = service;
        service.addObserver(this);
        initModel();
    }

    @Override
    public void update() {
        initModel();
    }
}
