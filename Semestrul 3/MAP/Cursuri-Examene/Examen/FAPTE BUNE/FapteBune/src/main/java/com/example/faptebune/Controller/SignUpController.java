package com.example.faptebune.Controller;

import com.example.faptebune.Entities.Oras;
import com.example.faptebune.Entities.Persoana;
import com.example.faptebune.Service.PersoanaService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.List;

public class SignUpController {


    private LogInController logInController;
    private Stage logInStage;

    private Stage signUpStage;

    private PersoanaService persoanaService;

    private final ObservableList<String> oraseModel = FXCollections.observableArrayList();

    @FXML
    private TextField numePersoana;

    @FXML
    private TextField prenumePersoana;

    @FXML
    private TextField usernamePersoana;

    @FXML
    private TextField parolaPersoana;

    @FXML
    private TextField stradaPersoana;

    @FXML
    private TextField numarStradaPersoana;

    @FXML
    private TextField telefonPersoana;

    @FXML
    private ComboBox<String> orase;

    @FXML
    public void initialize(){
        orase.setItems(oraseModel);
    }

    public void setPersoanaService(PersoanaService persoanaService) {
        this.persoanaService = persoanaService;
    }

    public void setOrase(){
        List<String> orase = new ArrayList<>();

        for(Oras oras : Oras.values()){
            orase.add(oras.name());
        }

        oraseModel.setAll(orase);
    }

    @FXML
    public void onSignUpButtonClicked(){
        String nume = numePersoana.getText();
        String prenume = prenumePersoana.getText();
        String username = usernamePersoana.getText();
        String parola = parolaPersoana.getText();
        String strada = stradaPersoana.getText();
        String numarStrada = numarStradaPersoana.getText();
        String telefon = telefonPersoana.getText();
        String oras = orase.getValue();

        Persoana persoana = new Persoana(nume, prenume, username, parola, Oras.valueOf(oras), strada, numarStrada, telefon);
        persoana.setId(persoanaService.getMaxId() + 1);

        if (!persoanaService.verifyIfAPersonExist(username)) {
            persoanaService.addPersoana(persoana);
            System.out.println("Persoana s-a adaugat cu succes");
            signUpStage.close();
        }else {
            System.out.println("Persoana deja existenta");
        }

        logInController.setListaPersoane();
        logInStage.show();

    }

    public void setLogInStage(Stage logInStage){
        this.logInStage = logInStage;
    }

    public void setSignUpStage(Stage signUpStage){
        this.signUpStage = signUpStage;
    }

    public void setLogInController(LogInController logInController){
        this.logInController = logInController;
    }

}
