package org.example.comenzi2.Controllers;

import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;
import org.example.comenzi2.Domain.MenuItem;
import org.example.comenzi2.Service.Service;
import org.example.comenzi2.Utils.Observer;

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;

public class TableController implements Observer {

    private Service service;
    private Integer tableId;

    @FXML
    VBox vboxtable;

    @FXML
    Button order;

    public void setService(Service service, Integer id) {
        this.service = service;
        this.tableId = id;
        service.addObserver(this);
        initModel();
    }

    public void initModel() {
        Iterable<String> categories = service.getCategorii();
        vboxtable.getChildren().clear();
        for (String category : categories) {
            Iterable<MenuItem> menuItems = service.menuItemDupaCategorie(category);

            Label categoryLabel = new Label(category);
            vboxtable.getChildren().add(categoryLabel);

            TableView<MenuItem> tableView = new TableView<>();
            TableColumn<MenuItem, String> itemColumn = new TableColumn<>("Name");
            itemColumn.setCellValueFactory(new PropertyValueFactory<>("item"));
            TableColumn<MenuItem, String> priceColumn = new TableColumn<>("Price");
            priceColumn.setCellValueFactory(cellData -> {
                MenuItem menuItem = cellData.getValue();
                return new SimpleStringProperty(menuItem.getPrice() + " " + menuItem.getCurrency());
            });

            tableView.getColumns().add(itemColumn);
            tableView.getColumns().add(priceColumn);
            tableView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
            for (MenuItem menuItem : menuItems) {
                tableView.getItems().add(menuItem);
            }
            vboxtable.getChildren().add(tableView);
        }
        vboxtable.getChildren().add(order);
    }


    public void handleOrder() {
        Iterable<MenuItem> selectedMenuItems = getSelectedMenuItems();
        service.adaugaComanda(selectedMenuItems, tableId);
    }

    private Iterable<MenuItem> getSelectedMenuItems() {
        List<MenuItem> selectedItems = new ArrayList<>();
        for (javafx.scene.Node node : vboxtable.getChildren()) {
            if (node instanceof TableView) {
                TableView<MenuItem> tableView = (TableView<MenuItem>) node;
                selectedItems.addAll(tableView.getSelectionModel().getSelectedItems());
            }
        }
        return selectedItems;
    }

    @Override
    public void update() {
        initModel();
    }

}
