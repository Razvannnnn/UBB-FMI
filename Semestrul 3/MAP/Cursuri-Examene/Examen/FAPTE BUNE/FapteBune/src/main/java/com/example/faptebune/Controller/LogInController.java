package com.example.faptebune.Controller;


import com.example.faptebune.Entities.Persoana;
import com.example.faptebune.HelloApplication;
import com.example.faptebune.Service.NevoieService;
import com.example.faptebune.Service.PersoanaService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.ListView;
import javafx.scene.control.TableView;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.List;

public class LogInController {

    private LogInController logInController;
    private PersoanaService persoanaService;

    private NevoieService nevoieService;
    private Stage logInStage;

    @FXML
    private ObservableList<Persoana> persoaneModel = FXCollections.observableArrayList();

    @FXML
    private ListView<Persoana> listaPersoane;

    @FXML
    public void initialize(){
        listaPersoane.setItems(persoaneModel);
    }

    @FXML
    public void onSignUpButtonClicked() throws IOException {

        logInStage.close();

        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("signUp-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 600, 400);

        Stage signUpStage = new Stage();

        signUpStage.setTitle("SignUp");
        signUpStage.setScene(scene);

        SignUpController signUpController = fxmlLoader.getController();
        signUpController.setPersoanaService(persoanaService);
        signUpController.setOrase();
        signUpController.setSignUpStage(signUpStage);
        signUpController.setLogInStage(logInStage);
        signUpController.setLogInController(logInController);

        signUpStage.show();
    }

    @FXML
    public void onSelectedPerson() throws IOException {
        Persoana persoana = listaPersoane.getSelectionModel().getSelectedItem();

        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("tabs-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 600, 400);

        Stage tabsStage = new Stage();

        tabsStage.setTitle("Tabs");
        tabsStage.setScene(scene);

        TabsController tabsController = fxmlLoader.getController();
        tabsController.setNevoieService(nevoieService);
        tabsController.setTabsStage(tabsStage);
        tabsController.setPersoanaService(persoanaService);
        tabsController.setPersoanaLogata(persoana);
        tabsController.setNevoiTable();
        tabsController.setFapteBuneTable();

        tabsStage.show();

    }

    public void setListaPersoane(){
        List<Persoana> persoane = persoanaService.getAllPersoane();

        persoaneModel.setAll(persoane);

    }

    public void setPersoanaService(PersoanaService persoanaService){
        this.persoanaService = persoanaService;
    }

    public void setNevoieService(NevoieService nevoieService){
        this.nevoieService = nevoieService;
    }

    public void setLogInStage(Stage logInStage){
        this.logInStage = logInStage;
    }

    public void setLogInController(LogInController logInController){
        this.logInController = logInController;
    }

}
