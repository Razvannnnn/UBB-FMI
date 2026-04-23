package com.example.rezerva.controller;

import com.example.rezerva.HelloApplication;
import com.example.rezerva.domain.Client;
import com.example.rezerva.service.Service;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.time.LocalDate;

public class LoginController {
    private Service service;
    @FXML
    private TextField usernameField;
    @FXML
    private Button loginButton;

    public void setService(Service service) {
        this.service = service;
        //initModel();
    }

    public void handleLogin(ActionEvent actionEvent){
        try{
            String username = usernameField.getText();
            Client c = service.getClientByUsername(username);

            FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("/views/clientView.fxml"));
            Parent root = (Parent) fxmlLoader.load();
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            ClientController clientController = fxmlLoader.getController();
            clientController.setService(service,c);
            stage.show();
            //((Node)(actionEvent.getSource())).getScene().getWindow().hide();
        } catch(Exception e) {
            e.printStackTrace();
        }
    }
}
