package org.example.reteasocializare;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.ImageCursor;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import org.example.reteasocializare.Domain.Validators.MessageValidator;
import org.example.reteasocializare.Domain.Validators.PrietenieValidator;
import org.example.reteasocializare.Domain.Validators.UtilizatorValidator;
import org.example.reteasocializare.Repository.DB.MessageDBRepository;
import org.example.reteasocializare.Repository.DB.PrietenieDBRepository;
import org.example.reteasocializare.Repository.DB.UtilizatorDBRepository;
import org.example.reteasocializare.Service.Network;
import org.example.reteasocializare.Service.ServiceMessage;

import java.io.File;
import java.io.IOException;

public class Start extends Application {

    UtilizatorDBRepository utilizatorDBRepository;
    PrietenieDBRepository prietenieDBRepository;
    MessageDBRepository messageDBRepository;
    Network network;
    ServiceMessage serviceMessage;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) throws IOException {
        utilizatorDBRepository = new UtilizatorDBRepository("jdbc:postgresql://localhost:5432/postgres",
                "postgres", "parola", new UtilizatorValidator());
        prietenieDBRepository = new PrietenieDBRepository("jdbc:postgresql://localhost:5432/postgres",
                "postgres", "parola", new PrietenieValidator(utilizatorDBRepository));
        messageDBRepository = new MessageDBRepository(new MessageValidator(),"jdbc:postgresql://localhost:5432/postgres",
                "postgres", "parola");
        network = new Network(utilizatorDBRepository, prietenieDBRepository);
        serviceMessage = new ServiceMessage(messageDBRepository);
        initView(stage);
        stage.setTitle("Login");
        Image icon = new Image(getClass().getResourceAsStream("/icon.png"));
        stage.getIcons().add(icon);
        stage.show();
    }

    private void initView(Stage stage) throws IOException {
        FXMLLoader loader = new FXMLLoader(Start.class.getResource("login.fxml"));
        Scene scene = new Scene(loader.load());
        stage.setScene(scene);
        LoginController controller = loader.getController();
        controller.setNetwork(network, serviceMessage);
    }
}
