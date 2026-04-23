package org.example.comenzi2.Controllers;

import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import org.example.comenzi2.Domain.Order;
import org.example.comenzi2.Service.Service;
import org.example.comenzi2.Utils.Observer;
import org.example.comenzi2.Domain.MenuItem;

import java.time.LocalDateTime;
import java.util.List;


public class MainController implements Observer {

    private Service service;

    @FXML
    Label label = new Label();

    @FXML
    TableView tablePlacedOrders;

    @FXML
    TableColumn<Order, Integer> table;

    @FXML
    TableColumn<Order, String> menuItems;

    @FXML
    TableColumn<Order, String> date;

    @FXML
    VBox vboxid;

    @FXML
    HBox hBox;

    @FXML
    public void initialize() {

    }

    public void initModel() {
        Iterable<Order> placedOrders = service.getPlacedOrders();
        tablePlacedOrders.getItems().clear();

        table.setCellValueFactory(new PropertyValueFactory<Order, Integer>("table"));
        menuItems.setCellValueFactory(cellData -> {
            Order order = (Order) cellData.getValue();
            StringBuilder sb = new StringBuilder();
            List<MenuItem> menuItems1 = service.getMenuItemsById(order.getMenuItems());
            for (MenuItem m : menuItems1) {
                sb.append(m.getItem()).append(",");
            }
            return new SimpleStringProperty(sb.toString());
        });
        date.setCellValueFactory(new PropertyValueFactory<Order, String>("date"));

        for (Order order : placedOrders) {
            tablePlacedOrders.getItems().add(order);
        }
        tablePlacedOrders.getSortOrder().add(date);
    }


    @Override
    public void update() {
        initModel();
    }

    public void setService(Service service) {
        this.service = service;
        this.service.addObserver(this);
        initModel();
    }
}
