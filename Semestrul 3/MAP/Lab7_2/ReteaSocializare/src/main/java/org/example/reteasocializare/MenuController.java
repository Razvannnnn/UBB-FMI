package org.example.reteasocializare;

import javafx.animation.ScaleTransition;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Duration;
import org.example.reteasocializare.Domain.Prietenie;
import org.example.reteasocializare.Domain.Utilizator;
import org.example.reteasocializare.Domain.UtilizatorCuData;
import org.example.reteasocializare.Repository.Paging.Page;
import org.example.reteasocializare.Repository.Paging.Pageable;
import org.example.reteasocializare.Service.Network;
import org.example.reteasocializare.Service.ServiceMessage;
import org.example.reteasocializare.Utils.Events.UtilizatorEvent;
import org.example.reteasocializare.Utils.Observable;
import org.example.reteasocializare.Utils.Observer;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public class MenuController implements Observer<UtilizatorEvent> {

    private Network network;
    private Utilizator utilizator;
    private ServiceMessage serviceMessage;
    ObservableList<UtilizatorCuData> model = FXCollections.observableArrayList();
    ObservableList<UtilizatorCuData> prieteniiObs = FXCollections.observableArrayList();

    private int pageNumber = 0;
    private int pageSize = 5;

    @FXML
    TableView<UtilizatorCuData> tableView;

    @FXML
    TableColumn<UtilizatorCuData, String> columnNume;

    @FXML
    TableColumn<UtilizatorCuData, String> columnPrenume;

    @FXML
    TableColumn<UtilizatorCuData, LocalDateTime> columnData;

    @FXML
    Button buttonAddNewFriends;

    @FXML
    Button buttonFriendRequests;

    @FXML
    Button buttonLogOut;

    @FXML
    Button buttonPrevious;

    @FXML
    Button buttonNext;

    @FXML
    Button buttonBegin;

    @FXML
    Button buttonEnd;

    @FXML
    Label pageNumberLabel;

    @FXML
    ListView<UtilizatorCuData> listPrietenii;

    @FXML
    public void initialize() {
        listPrietenii.setItems(prieteniiObs);
        addHoverAnimation(buttonAddNewFriends);
        addHoverAnimation(buttonFriendRequests);
        addHoverAnimation(buttonLogOut);
        addHoverAnimation(buttonPrevious);
        addHoverAnimation(buttonNext);
        addHoverAnimation(buttonBegin);
        addHoverAnimation(buttonEnd);

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

    public void handleAddNewFriends() {
        try {
            FXMLLoader loader = new FXMLLoader(MenuController.class.getResource("newFriends.fxml"));

            AnchorPane root = loader.load();

            Stage dialogStage = new Stage();
            dialogStage.setTitle("New Friends");
            dialogStage.initModality(Modality.WINDOW_MODAL);
            Image icon = new Image(getClass().getResourceAsStream("/icon.png"));
            dialogStage.getIcons().add(icon);

            Scene scene = new Scene(root);
            dialogStage.setScene(scene);

            NewFriendsController newFriendsController = loader.getController();
            newFriendsController.setNetwork(network, utilizator);

            dialogStage.show();

        } catch (IOException e) {
            e.printStackTrace();

        }
    }

    public void handleDeleteFriend() {
        UtilizatorCuData selected = listPrietenii.getSelectionModel().getSelectedItem();
        if (selected != null) {
            network.removePrietenie(utilizator.getId(), selected.getId());
            initModel();
        }
    }

    public void handleFriendRequests() {
        try {
            FXMLLoader loader = new FXMLLoader(MenuController.class.getResource("friendRequest.fxml"));

            AnchorPane root = loader.load();

            Stage dialogStage = new Stage();
            dialogStage.setTitle("Friend Requests");
            dialogStage.initModality(Modality.WINDOW_MODAL);
            Image icon = new Image(getClass().getResourceAsStream("/icon.png"));
            dialogStage.getIcons().add(icon);

            Scene scene = new Scene(root);
            dialogStage.setScene(scene);

            FriendRequestController friendRequestController = loader.getController();
            friendRequestController.setNetwork(network, utilizator);

            dialogStage.show();

        } catch (IOException e) {
            e.printStackTrace();

        }
    }

    public void handleMessage() {
        try {
            FXMLLoader loader = new FXMLLoader(MenuController.class.getResource("chat.fxml"));

            UtilizatorCuData selected = listPrietenii.getSelectionModel().getSelectedItem();
            AnchorPane root = loader.load();

            Stage dialogStage = new Stage();
            dialogStage.setTitle("Chat with " + selected.getUtilizator().getNume() + " " + selected.getUtilizator().getPrenume());
            dialogStage.initModality(Modality.WINDOW_MODAL);
            Image icon = new Image(getClass().getResourceAsStream("/icon.png"));
            dialogStage.getIcons().add(icon);

            Scene scene = new Scene(root);
            dialogStage.setScene(scene);

            ChatController chatController = loader.getController();
            chatController.setNetwork(network, serviceMessage, utilizator, selected.getUtilizator());

            dialogStage.show();

        } catch (IOException e) {
            e.printStackTrace();

        }
    }

    public void handleLogOut() {
        try {
            FXMLLoader loader = new FXMLLoader(MenuController.class.getResource("login.fxml"));

            AnchorPane root = loader.load();

            Stage dialogStage = new Stage();
            dialogStage.setTitle("Login");
            dialogStage.initModality(Modality.WINDOW_MODAL);
            Image icon = new Image(getClass().getResourceAsStream("/icon.png"));
            dialogStage.getIcons().add(icon);

            Scene scene = new Scene(root);
            dialogStage.setScene(scene);

            Stage currentStage = (Stage) buttonLogOut.getScene().getWindow();
            currentStage.close();

            LoginController loginController = loader.getController();
            loginController.setNetwork(network, serviceMessage);

            dialogStage.show();

        } catch (IOException e) {
            e.printStackTrace();

        }
    }

    public void initModel() {
        listPrietenii.getItems().clear();

        Page<Prietenie> pagePrietenii = network.findAllUserFriends(new Pageable(pageNumber, pageSize), this.utilizator);

        int maxPagePrietenii = (int) Math.ceil((double) pagePrietenii.getElementCount() / pageSize) - 1;
        if (maxPagePrietenii == -1) maxPagePrietenii = 0;
        if (pageNumber > maxPagePrietenii) {
            pageNumber = maxPagePrietenii;
            pagePrietenii = network.findAllUserFriends(new Pageable(pageNumber, pageSize), this.utilizator);
        }

        int totalNumberOfElementsPrietenie = pagePrietenii.getElementCount();

        buttonPrevious.setDisable(pageNumber == 0);
        buttonBegin.setDisable(pageNumber == 0);
        buttonNext.setDisable((pageNumber + 1) * pageSize >= totalNumberOfElementsPrietenie);
        buttonEnd.setDisable((pageNumber + 1) * pageSize >= totalNumberOfElementsPrietenie);

        for (Prietenie pr : pagePrietenii.getContent()) {
            if(utilizator.getId() == pr.getIdUtilizator1()) prieteniiObs.add(new UtilizatorCuData(network.findUtilizator(pr.getIdUtilizator2()), pr.getDate()));
            else prieteniiObs.add(new UtilizatorCuData(network.findUtilizator(pr.getIdUtilizator1()), pr.getDate()));
        }
        listPrietenii.setItems(prieteniiObs);

        pageNumberLabel.setText("Page " + (pageNumber + 1) + " of " + (maxPagePrietenii + 1));
    }

    public void handleNext() {
        pageNumber++;
        initModel();
    }

    public void handlePrevious() {
        pageNumber--;
        initModel();
    }

    public void handleBegin() {
        pageNumber = 0;
        initModel();
    }

    public void handleEnd() {
        Page<Prietenie> pagePrietenii = network.findAllUserFriends(new Pageable(pageNumber, pageSize), this.utilizator);
        int maxPagePrietenii = (int) Math.ceil((double) pagePrietenii.getElementCount() / pageSize) - 1;
        pageNumber = maxPagePrietenii;
        initModel();
    }

    @Override
    public void update(UtilizatorEvent utilizatorEvent) {
        initModel();
    }

    public void setNetwork(Network network, ServiceMessage serviceMessage, Utilizator utilizator) {
        this.network = network;
        this.utilizator = utilizator;
        this.serviceMessage = serviceMessage;
        network.addObserver(this);
        initModel();
    }
}
