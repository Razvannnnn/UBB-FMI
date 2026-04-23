package org.example.reteasocializare;

import javafx.animation.ScaleTransition;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Duration;
import org.example.reteasocializare.Domain.Message;
import org.example.reteasocializare.Domain.Utilizator;
import org.example.reteasocializare.Service.Network;
import org.example.reteasocializare.Service.ServiceMessage;
import org.example.reteasocializare.Utils.Events.UtilizatorEvent;
import org.example.reteasocializare.Utils.Observer;

import java.util.List;

public class ChatController {

    ObservableList<Utilizator> model = FXCollections.observableArrayList();

    private Utilizator from;
    private Utilizator to;

    private Network network;
    private ServiceMessage serviceMessage;

    @FXML
    private Button buttonSend;

    @FXML
    private Button buttonClose;

    @FXML
    private TextField textFieldMessage;

    @FXML
    private VBox vboxMessages;

    public void setNetwork(Network network, ServiceMessage serviceMessage, Utilizator from, Utilizator to) {
        this.network = network;
        this.from = from;
        this.to = to;
        this.serviceMessage = serviceMessage;

        loadMessages();
    }

    private void loadMessages() {
        if (serviceMessage != null && from != null && to != null) {
            List<Message> messages = serviceMessage.getMessages(from, to);
            for (Message message : messages) {
                boolean isSentByCurrentUser = message.getFrom().equals(from.getId());

                TextField messageField = new TextField(message.getMessage());
                messageField.setEditable(false);
                messageField.setStyle(isSentByCurrentUser
                        ? "-fx-background-color: lightblue; -fx-padding: 5; -fx-border-radius: 5; -fx-background-radius: 5;"
                        : "-fx-background-color: lightgray; -fx-padding: 5; -fx-border-radius: 5; -fx-background-radius: 5;");

                HBox messageContainer = new HBox(messageField);
                messageContainer.setSpacing(10);
                messageContainer.setPrefWidth(vboxMessages.getWidth());
                messageContainer.setStyle("-fx-alignment: " + (isSentByCurrentUser ? "CENTER_RIGHT" : "CENTER_LEFT") + ";");

                vboxMessages.getChildren().add(messageContainer);
            }
        }
    }

    public void handleSend() {
        String message = textFieldMessage.getText();
        if (!message.isBlank() && serviceMessage != null && from != null && to != null) {
            serviceMessage.addMessage(message, from.getId(), to.getId(), java.time.LocalDateTime.now());

            TextField messageField = new TextField(message);
            messageField.setEditable(false);
            messageField.setStyle("-fx-background-color: lightblue; -fx-padding: 5; -fx-border-radius: 5; -fx-background-radius: 5;");

            HBox messageContainer = new HBox(messageField);
            messageContainer.setSpacing(10);
            messageContainer.setPrefWidth(vboxMessages.getWidth());
            messageContainer.setStyle("-fx-alignment: CENTER_RIGHT;");

            vboxMessages.getChildren().add(messageContainer);

            textFieldMessage.clear();
        }
    }

    public void handleClose() {
        Stage stage = (Stage) buttonClose.getScene().getWindow();
        stage.close();
    }

    @FXML
    public void initialize() {
        addHoverAnimation(buttonSend);
        addHoverAnimation(buttonClose);
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
}
