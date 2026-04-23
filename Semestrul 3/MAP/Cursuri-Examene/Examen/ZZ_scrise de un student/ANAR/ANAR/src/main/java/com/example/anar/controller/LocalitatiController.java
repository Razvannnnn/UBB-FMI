package com.example.anar.controller;

import com.example.anar.domain.Localitate;
import com.example.anar.observer.Observer;
import com.example.anar.service.Service;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.*;
import javafx.scene.paint.Color;

public class LocalitatiController implements Observer {
    public TableView<Localitate> riskTable;
    public TableColumn<Localitate, String> numeColumn;
    public TableColumn<Localitate, String> riskColumn;
    public ObservableList<Localitate> localitati = FXCollections.observableArrayList();
    private Service service;

    public void initTable() {
        numeColumn.setCellValueFactory(cellData -> new ReadOnlyObjectWrapper<>(cellData.getValue().getNume()));
        riskColumn.setCellValueFactory(cellData -> {
            Localitate localitate = cellData.getValue();
            int cotaRau = service.getCotaRauNameById(localitate.getRauId());
            if (cotaRau < localitate.getCmdr()) {
                return new ReadOnlyObjectWrapper<>("Risc scazut");
            } else if (cotaRau < localitate.getCma()) {
                return new ReadOnlyObjectWrapper<>("Risc mediu");
            } else {
                return new ReadOnlyObjectWrapper<>("Risc ridicat");
            }
        });
        numeColumn.setCellFactory(column -> {
            return new TableCell<Localitate, String>() {
                @Override
                protected void updateItem(String item, boolean empty) {
                    super.updateItem(item, empty);
                    if (item == null || empty) {
                        setText(null);
                        setStyle("");
                    } else {
                        setText(item);
                        Localitate localitate = service.getLocalitateByName(item);
                        int cotaRau = service.getCotaRauNameById(localitate.getRauId());
                        if (cotaRau < localitate.getCmdr()) {
                            setTextFill(Color.GREEN);
                        } else if (cotaRau < localitate.getCma()) {
                            setTextFill(Color.ORANGE);
                        } else {
                            setTextFill(Color.RED);
                        }
                    }
                }
            };
        });
        riskColumn.setCellFactory(column -> {
            return new TableCell<Localitate, String>() {
                @Override
                protected void updateItem(String item, boolean empty) {
                    super.updateItem(item, empty);
                    if (item == null || empty) {
                        setText(null);
                        setStyle("");
                    } else {
                        setText(item);
                        if (item.equals("Risc scazut")) {
                            setTextFill(Color.GREEN);
                        } else if (item.equals("Risc mediu")) {
                            setTextFill(Color.ORANGE);
                        } else {
                            setTextFill(Color.RED);
                        }
                    }
                }
            };
        });

        riskTable.setRowFactory(tv -> {
            TableRow<Localitate> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (!row.isEmpty()) {
                    Localitate localitate = row.getItem();
                    String numeRau = service.getNumeRauById(localitate.getRauId());
                    String message = "Localitatea " + localitate.getNume() + " este afectata de " + numeRau + ", are limitele: \nCMDR = "
                            + localitate.getCmdr() + " \nCMA = " + localitate.getCma() + ".\n";
                    new Alert(Alert.AlertType.INFORMATION, message).showAndWait();
                }
            });
            return row;
        });
        riskTable.setItems(localitati);
        loadTable();
    }

    private void loadTable() {
        localitati.clear();
        localitati.addAll(service.getLocalitati());
    }

    public void setService(Service service) {
        this.service = service;
        service.addObserver(this);
        initTable();
    }

    @Override
    public void update() {
        loadTable();
        riskColumn.setSortType(TableColumn.SortType.DESCENDING);
        riskColumn.setComparator((o1, o2) -> {
            if (o1.equals(o2)) {
                return 0;
            } else if (o1.equals("Risc ridicat")) {
                return 1;
            } else if (o2.equals("Risc ridicat")) {
                return -1;
            } else if (o1.equals("Risc mediu")) {
                return 1;
            } else {
                return -1;
            }
        });
        riskTable.getSortOrder().add(riskColumn);
        riskTable.sort();
    }
}
