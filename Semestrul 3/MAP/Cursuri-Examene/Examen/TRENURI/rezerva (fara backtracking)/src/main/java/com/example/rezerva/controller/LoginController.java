package com.example.rezerva.controller;

import com.example.rezerva.HelloApplication;
import com.example.rezerva.service.Service;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class LoginController {
    private Service service;
    @FXML
    private Button loginButton;

    public void setService(Service service) {
        this.service = service;
    }

    public void handleLogin(ActionEvent actionEvent){
        try{
            FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("/views/clientView.fxml"));
            Parent root = (Parent) fxmlLoader.load();
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            ClientController clientController = fxmlLoader.getController();
            clientController.setService(service);
            stage.show();
            //((Node)(actionEvent.getSource())).getScene().getWindow().hide();
        } catch(Exception e) {
            e.printStackTrace();
        }
    }
}
