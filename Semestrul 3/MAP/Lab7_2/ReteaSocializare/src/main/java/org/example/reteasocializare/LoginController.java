package org.example.reteasocializare;

import javafx.animation.Transition;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.example.reteasocializare.Domain.Utilizator;
import org.example.reteasocializare.Service.Network;
import org.example.reteasocializare.Service.ServiceMessage;

import java.io.IOException;
import java.util.Set;

import javafx.animation.ScaleTransition;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.util.Duration;

public class LoginController {

    private Network network;
    private ServiceMessage serviceMessage;

    @FXML
    private Button buttonLogIn;

    @FXML
    private Button buttonCreateAccount;

    @FXML
    private TextField textFieldUsername;

    @FXML
    private PasswordField passwordField;

    @FXML
    public void initialize() {
        addHoverAnimation(buttonLogIn);
        addHoverAnimation(buttonCreateAccount);
        textFieldUsername.setPromptText("Username");
        passwordField.setPromptText("Password");
    }

    private void addHoverAnimation(Button button) {
        button.setOnMouseEntered(event -> {

            ScaleTransition scaleTransition = new ScaleTransition(Duration.millis(200), button);
            scaleTransition.setToX(1.1);
            scaleTransition.setToY(1.1);
            scaleTransition.play();
        });

        button.setOnMouseExited(event -> {
            ScaleTransition scaleTransition = new ScaleTransition(Duration.millis(200), button);
            scaleTransition.setToX(1.0);
            scaleTransition.setToY(1.0);
            scaleTransition.play();
        });
    }

    public void handleLogIn() {
        String username = textFieldUsername.getText();
        String password = passwordField.getText();
        Utilizator utilizator = network.findUtilizatorByUsername(username);
        if (utilizator != null && utilizator.getPassword().equals(password)) {
            System.out.println("Log in successful!");
            try {
                FXMLLoader loader = new FXMLLoader(MenuController.class.getResource("menu.fxml"));

                AnchorPane root = loader.load();

                Stage dialogStage = new Stage();
                dialogStage.setTitle("Menu");
                dialogStage.initModality(Modality.WINDOW_MODAL);
                Image icon = new Image(getClass().getResourceAsStream("/icon.png"));
                dialogStage.getIcons().add(icon);

                Scene scene = new Scene(root);
                dialogStage.setScene(scene);

                MenuController menuController = loader.getController();
                menuController.setNetwork(network, serviceMessage, utilizator);

                Stage currentStage = (Stage) buttonLogIn.getScene().getWindow();
                currentStage.close();

                dialogStage.show();

                if(network.numberOfFriendRequests(utilizator) > 0) {
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Notification");
                    alert.setHeaderText("Notification");
                    alert.setContentText("You have new friend requests!");

                    Stage alertStage = (Stage) alert.getDialogPane().getScene().getWindow();
                    alertStage.setWidth(300);
                    alertStage.setHeight(200);

                    alert.showAndWait();
                }

            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            System.out.println("Log in failed!");
        }
    }

    public void handleRegister() {
        // Your logic to handle registration
        System.out.println("Register clicked!");

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("register.fxml"));
            AnchorPane root = loader.load();
            Stage dialogStage = new Stage();
            dialogStage.setTitle("Register");
            dialogStage.initModality(Modality.WINDOW_MODAL);
            Image icon = new Image(getClass().getResourceAsStream("/icon.png"));
            dialogStage.getIcons().add(icon);

            Scene scene = new Scene(root);
            dialogStage.setScene(scene);
            dialogStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void setNetwork(Network network, ServiceMessage serviceMessage) {
        this.network = network;
        this.serviceMessage = serviceMessage;
    }
}
