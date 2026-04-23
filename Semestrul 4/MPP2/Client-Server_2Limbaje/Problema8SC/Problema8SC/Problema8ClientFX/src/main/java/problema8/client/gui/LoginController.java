package problema8.client.gui;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import problema8.model.User;
import problema8.services.IService;
import problema8.services.ProbExceptions;

import java.io.IOException;

public class LoginController {
    private IService server;
    private MainController mainController;
    private User currentUser;

    @FXML
    TextField textFieldUsername;

    @FXML
    PasswordField passwordField;

    @FXML
    Button buttonLogin;

    Parent mainParent;

    public void setParent(Parent p) {
        mainParent = p;
    }

    public void handleButtonLogin(ActionEvent actionEvent) {
        String username = textFieldUsername.getText();
        String password = passwordField.getText();
        currentUser = new User(username, password);
        try{
            server.login(currentUser, mainController);
            Stage stage = new Stage();
            stage.setTitle("Main Window");
            stage.setScene(new Scene(mainParent));
            stage.setOnCloseRequest(event -> {
                try {
                    mainController.logout();
                } catch (Exception e) {
                    System.out.println("Error logging out: " + e.getMessage());
                }
            });
            stage.show();
            mainController.setService(server, currentUser);
            ((Node) (actionEvent.getSource())).getScene().getWindow().hide();
        } catch (ProbExceptions e) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("MPP");
            alert.setHeaderText("Authentication failure");
            alert.setContentText("Wrong username or password");
            alert.showAndWait();
        }
    }

    public void setService(IService service) {
        this.server = service;
    }

    public void setMainController(MainController mainController) {
        this.mainController = mainController;
    }
}
