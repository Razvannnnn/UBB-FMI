package com.example.anar.controller;

import com.example.anar.domain.Rau;
import com.example.anar.service.Service;
import javafx.beans.Observable;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.scene.control.Alert;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;

public class MainController {

    public TableView<Rau> rauTable;
    public TableColumn<Rau, String> nameColumn;
    public TableColumn<Rau, Integer> cotaMedieColumn;
    public ObservableList<Rau> rauri = FXCollections.observableArrayList();
    public Service service;
    public TextField textField;

    public void setService(Service service) {
        this.service = service;
        initTable();
    }

    public void initTable() {
        nameColumn.setCellValueFactory(cellData -> new ReadOnlyObjectWrapper<>(cellData.getValue().getNume()));
        cotaMedieColumn.setCellValueFactory(cellData -> new ReadOnlyObjectWrapper<>(cellData.getValue().getCota()));
        rauTable.setItems(rauri);
        loadTable();
    }

    private void loadTable() {
        rauri.clear();
        rauri.addAll(service.getRauri());
    }

    public void modify(ActionEvent actionEvent) {
        if (rauTable.getSelectionModel().getSelectedItem() == null) {
            Alert alert = new Alert(Alert.AlertType.ERROR, "Nu ati selectat niciun rau!");
            alert.show();
            return;
        }
        Integer cota = Integer.valueOf(textField.getText());
        Rau rau = rauTable.getSelectionModel().getSelectedItem();
        service.modifyRau(rau, cota);
        loadTable();
    }
}