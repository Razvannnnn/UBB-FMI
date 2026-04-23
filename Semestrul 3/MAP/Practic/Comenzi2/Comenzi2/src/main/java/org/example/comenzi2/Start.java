package org.example.comenzi2;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.example.comenzi2.Controllers.MainController;
import org.example.comenzi2.Controllers.TableController;
import org.example.comenzi2.Domain.Table;
import org.example.comenzi2.Repository.MenuItemRepo;
import org.example.comenzi2.Repository.OrderRepo;
import org.example.comenzi2.Repository.TableRepo;
import org.example.comenzi2.Service.Service;

import java.io.IOException;

public class Start extends Application {
    MenuItemRepo menuItemRepo;
    TableRepo tableRepo;
    OrderRepo orderRepo;
    Service service;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) throws Exception {
        menuItemRepo = new MenuItemRepo("jdbc:postgresql://localhost:5432/comenzidb",
                "postgres", "parola");
        tableRepo = new TableRepo("jdbc:postgresql://localhost:5432/comenzidb",
                "postgres", "parola");
        orderRepo = new OrderRepo("jdbc:postgresql://localhost:5432/comenzidb",
                "postgres", "parola");
        service = new Service(menuItemRepo, tableRepo, orderRepo);
        initViewMain(stage);
        stage.setTitle("Staff");
        stage.show();
        Iterable<Table> tables = service.getAllTables();
        for (Table table : tables) {
            Stage tableStage = new Stage();
            initViewTable(tableStage, table.getId());
            tableStage.setTitle("Table " + table.getId());
            tableStage.show();
        }
    }

    private void initViewTable(Stage stage, Integer id) throws IOException {
        FXMLLoader loader = new FXMLLoader(Start.class.getResource("tableWindow.fxml"));
        Scene scene = new Scene(loader.load());
        stage.setScene(scene);
        TableController controller = loader.getController();
        controller.setService(service, id);
    }

    private void initViewMain(Stage stage) throws IOException {
        FXMLLoader loader = new FXMLLoader(Start.class.getResource("main.fxml"));
        Scene scene = new Scene(loader.load());
        stage.setScene(scene);
        MainController controller = loader.getController();
        controller.setService(service);
    }
}
