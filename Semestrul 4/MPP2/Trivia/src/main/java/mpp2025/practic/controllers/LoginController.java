package mpp2025.practic.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import mpp2025.domain.User;
import mpp2025.practic.models.Client;
import mpp2025.service.AppService;
import mpp2025.utils.LoginRequest;
import mpp2025.utils.LoginResponse;


public class LoginController {
    @FXML
    private TextField usernameField;
    @FXML
    private Button buttonStart;
    @FXML
    private Label infoText;

    private AppService service;
    private Client client;
    private Runnable onLoginSuccess;
    private User user;

    public void setClient(Client client) {
        this.client = client;
    }
    public void setService(AppService service) {
        this.service = service;
    }

    public void setOnLoginSuccess(Runnable o) {
        this.onLoginSuccess = o;
    }

    public User getUser() {
        return user;
    }

    @FXML
    public void handleStart() {
        String username = usernameField.getText();

        if(username == null || username.isEmpty()) {
            infoText.setText("Username-ul nu poate fi gol!");
            return;
        }

        LoginRequest loginRequest = new LoginRequest(username);

        client.login(loginRequest).thenAccept(response -> {
            if(response instanceof LoginResponse.Success){
                System.out.println("Login success");
                if(onLoginSuccess != null){
                    this.user = new User(username);
                    javafx.application.Platform.runLater(onLoginSuccess);
                }
            }
            else if(response instanceof LoginResponse.WrongUsername){
                System.out.println("Login failed: " + ((LoginResponse.WrongUsername) response).toString());
                javafx.application.Platform.runLater(() -> infoText.setText("Username incorect!"));
            }

        }).exceptionally(ex -> {
            ex.printStackTrace();
            javafx.application.Platform.runLater(() -> showAlert(
                    "Connection Error", "Could not contact server: " + ex.getMessage(), Alert.AlertType.ERROR
            ));
            return null;
        });
    }

    private void showAlert(String title, String message, Alert.AlertType type) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

}
