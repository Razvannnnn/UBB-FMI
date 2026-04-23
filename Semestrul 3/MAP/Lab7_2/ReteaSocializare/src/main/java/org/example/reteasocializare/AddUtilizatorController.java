package org.example.reteasocializare;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.example.reteasocializare.Domain.Utilizator;
import org.example.reteasocializare.Service.Network;

public class AddUtilizatorController {
    @FXML
    private TextField textFieldId;

    @FXML
    private TextField textFieldNume;

    @FXML
    private TextField textFieldPrenume;

    private Network network;
    Stage dialogStage;
    Utilizator utilizator;

    @FXML
    public void initialize() {
    }

    public void setNetwork(Network network, Stage dialogStage, Utilizator utilizator) {
        this.network = network;
        this.dialogStage = dialogStage;
        this.utilizator = utilizator;
        if (null != utilizator) {
            setFields(utilizator);
        }
    }

    private void setFields(Utilizator utilizator) {
        textFieldId.setText(utilizator.getId().toString());
        textFieldNume.setText(utilizator.getNume());
        textFieldPrenume.setText(utilizator.getPrenume());
    }

    /*
    @FXML
    public void handleSave() {
        String id = textFieldId.getText();
        String nume = textFieldNume.getText();
        String prenume = textFieldPrenume.getText();

        Utilizator utilizator = new Utilizator(nume, prenume);
        if(null == this.utilizator) {
            saveUtilizator(utilizator);
        }
     */



    private void saveUtilizator(Utilizator utilizator) {
        try {
            Utilizator newUtilizator = this.network.addUtilizator(utilizator);
            if(newUtilizator == null) {
                dialogStage.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        dialogStage.close();
    }

    public void handleCancel() {
        dialogStage.close();
    }

}
