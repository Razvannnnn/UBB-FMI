package org.example.reteasocializare;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.controlsfx.control.action.Action;
import org.example.reteasocializare.Controller.MessageUtilizator;
import org.example.reteasocializare.Domain.Utilizator;
import org.example.reteasocializare.Service.Network;
import org.example.reteasocializare.Utils.Events.UtilizatorEvent;
import org.example.reteasocializare.Utils.Observer;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public class UtilizatoriController implements Observer<UtilizatorEvent> {

    Network network;
    ObservableList<Utilizator> model = FXCollections.observableArrayList();

    @FXML
    TableView<Utilizator> tableView;

    @FXML
    TableColumn<Utilizator, Long> tableUserId;

    @FXML
    TableColumn<Utilizator, String> tableUserFirstName;

    @FXML
    TableColumn<Utilizator, String> tableUserLastName;

    @FXML
    public void initialize() {
        tableUserId.setCellValueFactory(new PropertyValueFactory<Utilizator, Long>("id"));
        tableUserFirstName.setCellValueFactory(new PropertyValueFactory<Utilizator, String>("Prenume"));
        tableUserLastName.setCellValueFactory(new PropertyValueFactory<Utilizator, String>("Nume"));
        tableView.setItems(model);
    }

    @FXML
    public void handleAddUtilizator() {
        showAddUtilizatorEditDialog(null);
    }


    public void showAddUtilizatorEditDialog(Utilizator u) {
        try {
            FXMLLoader loader = new FXMLLoader(UtilizatoriController.class.getResource("add-utilizator.fxml"));

            AnchorPane root = (AnchorPane) loader.load();

            Stage dialogStage = new Stage();
            dialogStage.setTitle("Add Utilizator");
            dialogStage.initModality(Modality.WINDOW_MODAL);

            Scene scene = new Scene(root);
            dialogStage.setScene(scene);

            AddUtilizatorController addUtilizatorController =((FXMLLoader) loader).getController();
            addUtilizatorController.setNetwork(network, dialogStage, u);

            dialogStage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    @FXML
    public void handleDeleteUtilizator() {
        Utilizator selectedUtilizator = tableView.getSelectionModel().getSelectedItem();
        if (selectedUtilizator != null) {
            Utilizator utilizatorToDelete = network.removeUtilizator(selectedUtilizator.getId());
            if (utilizatorToDelete != null) {
                MessageUtilizator.showMessage(null, Alert.AlertType.INFORMATION, "Delete",
                        "Utilizatorul (" + utilizatorToDelete.getId() + " " + utilizatorToDelete.getNume() + " " +
                                utilizatorToDelete.getPrenume() + ") a fost sters cu succes!");
            }
        } else {
            MessageUtilizator.showErrorMessage(null, "Nu ati selectat niciun utilizator!");
        }
    }

    @FXML
    public void handleUpdateButton(ActionEvent e) {
        Utilizator utilizatorToUpdate = tableView.getSelectionModel().getSelectedItem();
        if (utilizatorToUpdate != null) {
            showAddUtilizatorEditDialog(null);
            network.removeUtilizator(utilizatorToUpdate.getId());
        } else {
            MessageUtilizator.showErrorMessage(null, "Nu ati selectat niciun utilizator!");
        }
    }

    public void setNetwork(Network n) {
        this.network = n;
        this.network.addObserver(this);
        initModel();
    }

    public void initModel() {
        Iterable<Utilizator> utilizatori = network.getUtilizatori();
        List<Utilizator> utilizatoriList = StreamSupport.stream(utilizatori.spliterator(), false)
                .collect(Collectors.toList());
        model.setAll(utilizatoriList);
    }


    @Override
    public void update(UtilizatorEvent utilizatorEvent) {
        initModel();
    }
}
