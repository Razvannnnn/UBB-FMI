package iss.parkingapp;

import iss.parkingapp.domain.Utilizator;
import iss.parkingapp.service.Service;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class SignUpController {

    Service service;

    @FXML
    private TextField textFieldNume;

    @FXML
    private TextField textFieldEmail;

    @FXML
    private TextField textFieldParola;

    @FXML
    private TextField textFieldPrenume;

    public void handleSignup() {
        String nume = textFieldNume.getText();
        String prenume = textFieldPrenume.getText();
        String email = textFieldEmail.getText();
        String parola = textFieldParola.getText();

        if (nume.isEmpty() || prenume.isEmpty() || email.isEmpty() || parola.isEmpty()) {
            System.out.println("All fields are required");
            return;
        }

        Utilizator utilizator = new Utilizator(nume, prenume, email, parola);
        service.addUtilizator(utilizator);

        System.out.println("User registered successfully");
    }

    public void handleClose() {
        Stage stage = (Stage) textFieldNume.getScene().getWindow();
        stage.close();
        System.out.println("Signup closed");
    }


    public void setService(Service service) {
        this.service = service;
    }
}
