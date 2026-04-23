package com.example.faptebune.Controller;


import com.example.faptebune.Entities.Nevoie;
import com.example.faptebune.Entities.Persoana;
import com.example.faptebune.Service.NevoieService;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class NevoieController {


    private Stage nevoieStage;

    private NevoieService nevoieService;

    private Persoana persoanaLogata;

    @FXML
    private DatePicker deadline;

    @FXML
    private Spinner<Integer> ora;

    @FXML
    private Spinner<Integer> minut;

    @FXML
    private TextField titlu;

    @FXML
    private TextField descriere;

    @FXML
    public void initialize() {
        minut.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 23, 0));
        ora.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 59, 0));
    }

    public void setNevoieService(NevoieService nevoieService) {
        this.nevoieService = nevoieService;
    }

    public void setPersoanaLogata(Persoana persoanaLogata){
        this.persoanaLogata = persoanaLogata;
    }

    public void setNevoieStage(Stage nevoieStage){
        this.nevoieStage = nevoieStage;
    }

    @FXML
    public void onAdaugaNevoieButtonClicked() {
        String strTitlu = titlu.getText();
        String strDescriere = descriere.getText();


        int minute = Integer.parseInt(String.valueOf(minut.getValue()));
        int hour = Integer.parseInt(String.valueOf(ora.getValue()));

        String strMinut = "";
        String strOra = "";

        if (hour >= 0 && hour <= 9) {
            strOra = "0" + ora.getValue();
        } else {
            strOra = ora.getValue() + "";
        }

        if (minute >= 0 && minute <= 9) {
            strMinut = "0" + minut.getValue();
        } else {
            strMinut = minut.getValue() + "";
        }

        String strDeadline;

        strDeadline = deadline.getValue().toString() + " " + strOra + ":" + strMinut;

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        LocalDateTime localDateTime = LocalDateTime.parse(strDeadline, formatter);

        Nevoie nevoie = new Nevoie(strTitlu, strDescriere, localDateTime, persoanaLogata.getId(), 0L, "Caut erou!");
        nevoie.setId(nevoieService.getMaxId() + 1);

        nevoieStage.close();

        nevoieService.adaugaNevoie(nevoie);

    }


}
