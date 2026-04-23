package com.example.guiex1.controller;

import com.example.guiex1.domain.Utilizator;
import com.example.guiex1.domain.ValidationException;
import com.example.guiex1.services.UtilizatorService;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.stage.Stage;



public class EditUserController {
    @FXML
    private TextField textFieldId;
    @FXML
    private TextField textFieldFirstName;
    @FXML
    private TextField textFieldLastName;


    private UtilizatorService service;
    Stage dialogStage;
    Utilizator utilizator;

    @FXML
    private void initialize() {
    }

    public void setService(UtilizatorService service,  Stage stage, Utilizator u) {
        this.service = service;
        this.dialogStage=stage;
        this.utilizator =u;
        if (null != u) {
            setFields(u);
            textFieldId.setEditable(false);
        }
    }

    @FXML
    public void handleSave(){
        String id=textFieldId.getText();
        String firstNameText= textFieldFirstName.getText();
        String lastNameText= textFieldLastName.getText();

        Utilizator utilizator1=new Utilizator(firstNameText,lastNameText);
        utilizator1.setId(Long.valueOf(id));
        if (null == this.utilizator)
            saveMessage(utilizator1);
        else
            updateMessage(utilizator1);
    }

    private void updateMessage(Utilizator m)
    {
        try {
            Utilizator r= this.service.updateUtilizator(m);
            if (r==null)
                MessageAlert.showMessage(null, Alert.AlertType.INFORMATION,"Modificare user","Userul a fost modificat");
        } catch (ValidationException e) {
            MessageAlert.showErrorMessage(null,e.getMessage());
        }
        dialogStage.close();
    }


    private void saveMessage(Utilizator m)
    {
        // TODO
        try {
            Utilizator r= this.service.addUtilizator(m);
            if (r==null)
                dialogStage.close();
            MessageAlert.showMessage(null, Alert.AlertType.INFORMATION,"Slavare user","Mesajul a fost salvat");
        } catch (ValidationException e) {
            MessageAlert.showErrorMessage(null,e.getMessage());
        }
        dialogStage.close();

    }

    private void clearFields() {
        textFieldId.setText("");
        textFieldFirstName.setText("");
        textFieldLastName.setText("");

    }
    private void setFields(Utilizator u)
    {
        textFieldId.setText(u.getId().toString());
        textFieldFirstName.setText(u.getFirstName());
        textFieldLastName.setText(u.getLastName());

    }

    @FXML
    public void handleCancel(){
        dialogStage.close();
    }
}
