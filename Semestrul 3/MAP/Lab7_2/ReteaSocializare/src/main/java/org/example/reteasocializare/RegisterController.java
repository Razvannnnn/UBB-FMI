package org.example.reteasocializare;

import javafx.animation.ScaleTransition;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import javafx.util.Duration;
import org.example.reteasocializare.Domain.Utilizator;
import org.example.reteasocializare.Service.Network;

public class RegisterController {

    private Network network;

    @FXML
    private Button buttonRegister;

    @FXML
    private Button buttonBack;

    @FXML
    private TextField textFieldUsername;

    @FXML
    private TextField textFieldNume;

    @FXML
    private TextField textFieldPrenume;

    @FXML
    private PasswordField passwordField;


    public void handleBack() {
        Stage stage = (Stage) buttonBack.getScene().getWindow();
        Image icon = new Image(getClass().getResourceAsStream("/icon.png"));
        stage.getIcons().add(icon);
        stage.close();
        System.out.println("Back to login");
    }

    public void handleRegister() {
        String username = textFieldUsername.getText();
        String nume = textFieldNume.getText();
        String prenume = textFieldPrenume.getText();
        String password = passwordField.getText();
        network.addUtilizator(new Utilizator(username, nume, prenume, password));
        System.out.println("Register successful!");
    }

    @FXML
    public void initialize() {
        addHoverAnimation(buttonRegister);
        addHoverAnimation(buttonBack);
        textFieldUsername.setPromptText("Username");
        textFieldNume.setPromptText("Nume");
        textFieldPrenume.setPromptText("Prenume");
        passwordField.setPromptText("Password");
    }

    private void addHoverAnimation(Button button) {
        // Scale up on hover
        button.setOnMouseEntered(event -> {

            ScaleTransition scaleTransition = new ScaleTransition(Duration.millis(200), button);
            scaleTransition.setToX(1.1);
            scaleTransition.setToY(1.1);
            scaleTransition.play();
        });

        // Scale back on exit
        button.setOnMouseExited(event -> {
            ScaleTransition scaleTransition = new ScaleTransition(Duration.millis(200), button);
            scaleTransition.setToX(1.0);
            scaleTransition.setToY(1.0);
            scaleTransition.play();
        });
    }

    public void setNetwork(Network network) {
        this.network = network;
    }
}
