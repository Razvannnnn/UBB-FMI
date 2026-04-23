package iss.parkingapp;

import iss.parkingapp.domain.Utilizator;
import iss.parkingapp.service.Service;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

import java.util.Objects;

public class EditController {
    private Utilizator user;
    private Service service;

    @FXML
    private TextField textFieldNume;
    @FXML
    private TextField textFieldPrenume;
    @FXML
    private TextField textFieldEmail;
    @FXML
    private PasswordField parolaNoua;
    @FXML
    private PasswordField parolaActuala;
    @FXML
    private Button buttonConfirm;

    @FXML
    public void initialize() {
        buttonConfirm.disableProperty().bind(parolaActuala.textProperty().isEmpty());
    }

    public void setService(Service service, Utilizator user) {
        this.service = service;
        this.user = user;
        textFieldNume.setText(user.getNume());
        textFieldPrenume.setText(user.getPrenume());
        textFieldEmail.setText(user.getEmail());
    }

    @FXML
    public void handleButtonConfirm() {
        String nume = textFieldNume.getText();
        String prenume = textFieldPrenume.getText();
        String email = textFieldEmail.getText();
        String parolaN = parolaNoua.getText();
        String parolaA = parolaActuala.getText();

        if (service.updateUser(user, nume, prenume, email, parolaA, parolaN)) {
            System.out.println("User updated successfully");
            textFieldNume.clear();
            textFieldPrenume.clear();
            textFieldEmail.clear();
            parolaActuala.clear();
            parolaNoua.clear();

            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Success");
            alert.setHeaderText("Update Successful");
            alert.setContentText("Your details have been updated successfully.");
            alert.showAndWait();
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Update Failed");
            alert.setContentText("Please check your input and try again.");
            alert.showAndWait();
        }
    }

    @FXML
    public void handleClose() {
        System.out.println("Close button clicked");
        // Logic to close the window or navigate back
        // This could be implemented using a Stage or Scene change in JavaFX
        if (Objects.nonNull(buttonConfirm.getScene())) {
            buttonConfirm.getScene().getWindow().hide();
        }
    }

}
