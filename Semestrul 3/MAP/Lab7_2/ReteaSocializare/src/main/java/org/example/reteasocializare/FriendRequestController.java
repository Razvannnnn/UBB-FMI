package org.example.reteasocializare;

import javafx.animation.ScaleTransition;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Duration;
import org.example.reteasocializare.Domain.Utilizator;
import org.example.reteasocializare.Service.Network;
import org.example.reteasocializare.Utils.Events.UtilizatorEvent;
import org.example.reteasocializare.Utils.Observable;
import org.example.reteasocializare.Utils.Observer;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public class FriendRequestController implements Observer<UtilizatorEvent> {

    private Network network;
    private Utilizator utilizator;
    ObservableList<Utilizator> model = FXCollections.observableArrayList();
    ObservableList<Utilizator> model2 = FXCollections.observableArrayList();

    @FXML
    TableView<Utilizator> tableView;

    @FXML
    TableView<Utilizator> tableView2;

    @FXML
    TableColumn<Utilizator, String> columnNume;

    @FXML
    TableColumn<Utilizator, String> columnPrenume;

    @FXML
    TableColumn<Utilizator, String> columnNume2;

    @FXML
    TableColumn<Utilizator, String> columnPrenume2;

    @FXML
    Button buttonBack;

    @FXML
    Button buttonAccept;

    @FXML
    Button buttonDecline;

    @FXML
    public void initialize() {
        columnNume.setCellValueFactory(new PropertyValueFactory<Utilizator, String>("Nume"));
        columnPrenume.setCellValueFactory(new PropertyValueFactory<Utilizator, String>("Prenume"));
        columnNume2.setCellValueFactory(new PropertyValueFactory<Utilizator, String>("Nume"));
        columnPrenume2.setCellValueFactory(new PropertyValueFactory<Utilizator, String>("Prenume"));
        tableView.setItems(model);
        tableView2.setItems(model2);
        addHoverAnimation(buttonBack);
        addHoverAnimation(buttonAccept);
        addHoverAnimation(buttonDecline);
    }

    private void addHoverAnimation(Button button) {
        // Scale up on hover
        if (button != null) {
            button.setOnMouseEntered(event -> {

                ScaleTransition scaleTransition = new ScaleTransition(Duration.millis(200), button);
                scaleTransition.setToX(1.1);
                scaleTransition.setToY(1.1);
                scaleTransition.play();
            });

            // Scale back on exit
            button.setOnMouseExited(event -> {
                ScaleTransition scaleTransition = new ScaleTransition(Duration.millis(200), button);
                scaleTransition.setToX(1.0);
                scaleTransition.setToY(1.0);
                scaleTransition.play();
            });
        }
    }

    public void handleAcceptRequest() {
        Utilizator selected = tableView.getSelectionModel().getSelectedItem();
        if (selected != null) {
            network.acceptFriendRequest(utilizator, selected);
            initModel();
        }
    }

    public void handleDeclineRequest() {
        Utilizator selected = tableView.getSelectionModel().getSelectedItem();
        if (selected != null) {
            network.declineFriendRequest(utilizator, selected);
            initModel();
        }
    }

    public void handleGoBack() {
        Stage stage = (Stage) buttonBack.getScene().getWindow();

        stage.close();
        System.out.println("Back to menu");
    }

    public void initModel() {
        Iterable<Utilizator> utilizatori = network.FriendRequests(utilizator);
        List<Utilizator> utilizatoriList = StreamSupport.stream(utilizatori.spliterator(), false)
                .collect(Collectors.toList());
        model.setAll(utilizatoriList);

        Iterable<Utilizator> utilizatori2 = network.pendingFriendRequests(utilizator);
        List<Utilizator> utilizatoriList2 = StreamSupport.stream(utilizatori2.spliterator(), false)
                .collect(Collectors.toList());
        model2.setAll(utilizatoriList2);
    }


    @Override
    public void update(UtilizatorEvent utilizatorEvent) {
        initModel();
    }

    public void setNetwork(Network network, Utilizator utilizator) {
        this.network = network;
        this.utilizator = utilizator;
        network.addObserver(this);
        initModel();
    }

}
