package org.example.zboruri;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import org.example.zboruri.Domain.Client;
import org.example.zboruri.Service.Service;
import org.example.zboruri.Utils.Observer;

public class LoginController implements Observer {

    private Service service;

    @FXML
    TextField usernameField;
    @FXML
    Button buttonLogin;
    @FXML
    void handleLogin() {
        String username = usernameField.getText();
        if(service.login(username)) {
            try {
                FXMLLoader loader = new FXMLLoader();
                loader.setLocation(getClass().getResource("main.fxml"));
                AnchorPane root = loader.load();
                Stage stage = new Stage();;
                stage.setScene(new Scene(root));
                MainController mainController = loader.getController();
                Client client = service.getClient(username);
                if(client == null) {
                    throw new Exception("Client not found");
                }
                stage.setTitle(client.getName());
                mainController.setService(service, client);

                Stage currentStage = (Stage) buttonLogin.getScene().getWindow();
                currentStage.close();

                stage.show();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @FXML
    void initialize() {
        System.out.println("Login controller initialized");
    }

    void setService(Service service) {
        this.service = service;
        this.service.addObserver(this);

    }

    @Override
    public void update() {

    }
}
