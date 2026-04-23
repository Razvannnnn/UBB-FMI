package com.example.faptebune.Controller;

import com.example.faptebune.Entities.Nevoie;
import com.example.faptebune.Entities.Persoana;
import com.example.faptebune.HelloApplication;
import com.example.faptebune.Service.NevoieService;
import com.example.faptebune.Service.PersoanaService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.TabPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class TabsController {


    private NevoieService nevoieService;

    private final ObservableList<Nevoie> fapteBuneModel = FXCollections.observableArrayList();
    private final ObservableList<Nevoie> nevoiModel = FXCollections.observableArrayList();
    private Persoana persoanaLogata;

    private PersoanaService persoanaService;

    private Stage tabsStage;

    @FXML
    private TabPane tabsPane;

    @FXML
    private TableView<Nevoie> fapteBuneTable;

    @FXML
    private TableColumn<Nevoie, String> titlu1;

    @FXML
    private TableColumn<Nevoie, String> descriere1;

    @FXML
    private TableColumn<Nevoie, LocalDateTime> deadline1;

    @FXML
    private TableColumn<Nevoie, Long> omInNevoie1;

    @FXML
    private TableColumn<Nevoie, Long> omSalvator1;

    @FXML
    private TableColumn<Nevoie, String> status1;

    @FXML
    private TableView<Nevoie> nevoiTable;

    @FXML
    private TableColumn<Nevoie, String> titlu;

    @FXML
    private TableColumn<Nevoie, String> descriere;

    @FXML
    private TableColumn<Nevoie, LocalDateTime> deadline;

    @FXML
    private TableColumn<Nevoie, Long> omInNevoie;

    @FXML
    private TableColumn<Nevoie, Long> omSalvator;

    @FXML
    private TableColumn<Nevoie, String> status;

    @FXML
    public void initialize(){

        titlu.setCellValueFactory(new PropertyValueFactory<>("titlu"));
        descriere.setCellValueFactory(new PropertyValueFactory<>("descriere"));
        deadline.setCellValueFactory(new PropertyValueFactory<>("deadline"));
        omInNevoie.setCellValueFactory(new PropertyValueFactory<>("omInNevoie"));
        omSalvator.setCellValueFactory(new PropertyValueFactory<>("omSalvator"));
        status.setCellValueFactory(new PropertyValueFactory<>("status"));

        titlu1.setCellValueFactory(new PropertyValueFactory<>("titlu"));
        descriere1.setCellValueFactory(new PropertyValueFactory<>("descriere"));
        deadline1.setCellValueFactory(new PropertyValueFactory<>("deadline"));
        omInNevoie1.setCellValueFactory(new PropertyValueFactory<>("omInNevoie"));
        omSalvator1.setCellValueFactory(new PropertyValueFactory<>("omSalvator"));
        status1.setCellValueFactory(new PropertyValueFactory<>("status"));

        nevoiTable.setItems(nevoiModel);
        fapteBuneTable.setItems(fapteBuneModel);
    }

    public void setPersoanaLogata(Persoana persoanaLogata){
        this.persoanaLogata = persoanaLogata;
    }

    public void setNevoieService(NevoieService nevoieService){
        this.nevoieService = nevoieService;
    }

    public void setPersoanaService(PersoanaService persoanaService) {
        this.persoanaService = persoanaService;
    }

    public void setNevoiTable(){
        List<Nevoie> nevoi = nevoieService.getAllNevoi();

        List<Nevoie> nevoiRez = new ArrayList<>();

        for (Nevoie nevoie : nevoi) {

            Persoana persoanaInNevoie = persoanaService.findOnePersoana(nevoie.getOmInNevoie());

            System.out.println("Persoana logata: " + persoanaLogata.getId());
            System.out.println(nevoie.getOmInNevoie());
            //System.out.println(persoanaInNevoie.getOras());
            System.out.println(persoanaLogata.getOras());

            setFapteBuneTable();

            if (nevoie.getOmInNevoie() != persoanaLogata.getId() && persoanaInNevoie.getOras().toString().equals(persoanaLogata.getOras().toString())) {
                nevoiRez.add(nevoie);
            }
        }

        nevoiModel.setAll(nevoiRez);

    }

    public void setTabsStage(Stage tabsStage){
        this.tabsStage = tabsStage;
    }

    @FXML
    public void onFapteBuneTableClicked() throws IOException {

    }

    @FXML
    public void onTabsPaneClicked() throws IOException {
        String ajutor = tabsPane.getSelectionModel().getSelectedItem().getText();

        if(ajutor.equals("Doresc sa fiu ajutat!")){
            FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("nevoie-view.fxml"));
            Scene scene = new Scene(fxmlLoader.load(), 600, 400);

            Stage nevoieStage = new Stage();

            nevoieStage.setTitle("Nevoie");
            nevoieStage.setScene(scene);

            NevoieController nevoieController = fxmlLoader.getController();
            nevoieController.setPersoanaLogata(persoanaLogata);
            nevoieController.setNevoieService(nevoieService);
            nevoieController.setNevoieStage(nevoieStage);

            nevoieStage.show();
        }
    }

    @FXML
    public void onNevoiTableClicked(){

        Nevoie nevoieSelectata = nevoiTable.getSelectionModel().getSelectedItem();

        Nevoie nevoie = nevoieService.updateNevoie(nevoieSelectata.getId(), persoanaLogata.getId());

        if(nevoie != null){
            setNevoiTable();

            Alert message = new Alert(Alert.AlertType.CONFIRMATION);
            message.initOwner(tabsStage);
            message.setTitle("Sarcina atribuita!");
            message.setContentText("V-ati asumat responsabilitatea rezolvarii unei nevoi!!!");
            message.showAndWait();
        }else {
            System.out.println("Nevoia nu poate fi actualizata!");
        }
    }

    @FXML
    public void setFapteBuneTable(){
        List<Nevoie> nevoi = nevoieService.getAllNevoi();
        List<Nevoie> fapteBune = new ArrayList<>();

        for(Nevoie nevoie : nevoi){
            if(nevoie.getStatus().equals("Erou gasit!") && persoanaLogata.getId().equals(nevoie.getOmSalvator())){
                fapteBune.add(nevoie);
            }
        }

        fapteBuneModel.setAll(fapteBune);

    }

}
