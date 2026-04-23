package problema8.client.gui;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import problema8.model.User;

import java.io.IOException;

public class LoginController {
    private Service service;

    @FXML
    TextField textFieldUsername;

    @FXML
    PasswordField passwordField;

    @FXML
    Button buttonLogin;

    public void handleButtonLogin() {
        String username = textFieldUsername.getText();
        String password = passwordField.getText();
        User user = service.login(username, password);
        if (user != null && user.getPassword().equals(password)) {
            System.out.println("Login successful");
            try {
                FXMLLoader loader = new FXMLLoader(MainController.class.getResource("main.fxml"));

                AnchorPane pane = loader.load();

                Stage dialogStage = new Stage();
                dialogStage.setTitle("Main");
                dialogStage.initModality(Modality.WINDOW_MODAL);

                Scene scene = new Scene(pane);
                dialogStage.setScene(scene);

                MainController mainController = loader.getController();
                mainController.setService(service, user);

                Stage currentStage = (Stage) buttonLogin.getScene().getWindow();
                currentStage.close();

                dialogStage.show();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            System.out.println("Login failed");
        }
    }

    public void setService(Service service) {
        this.service = service;
    }
}
