package com.example.rezerva.utils;

import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.VBox;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

public class TableViewCustom<EntityType> extends VBox {
    private TableView tableView = new TableView<>();
    private List<EntityType> lst;
    private List<String> headere;
    List<Function<EntityType,String>> functii=new ArrayList<>();
    public TableViewCustom(List<EntityType> lst, List<String> headere, Function<EntityType, String>... functii){
        this.lst=lst;
        this.headere=headere;
        for(Function<EntityType,String> f:functii){
            this.functii.add(f);
        }
        getChildren().add(tableView);
        tableView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        reloadList();
    }

    private void clearTable(){
        for ( int i = 0; i<this.tableView.getItems().size(); i++) {
            this.tableView.getItems().clear();
        }
        this.tableView.getColumns().clear();
    }
    private void seteazaItemi(){
        ObservableList<EntityType> data = FXCollections.observableArrayList(this.lst);
        this.tableView.setItems(data);
    }

    public void reloadList(){
        int i=0;
        clearTable();
        seteazaItemi();
        for(Function<EntityType,String> f:functii){
            TableColumn<EntityType, String> col = new TableColumn<>(headere.get(i));

            col.setCellValueFactory(c -> {
                EntityType entity=c.getValue();
                String valoareCelula = f.apply(entity);
                return new ReadOnlyObjectWrapper<String>(valoareCelula);});
            tableView.getColumns().add(col);

            i++;
        }
    }

    public void setLst(List<EntityType> lst){
        this.lst=lst;
        reloadList();
    }

    public List<EntityType> getLst(){
        return lst;
    }

    public void setOnAction(EventHandler event){
        this.tableView.setOnMouseClicked(event);
    }

    public EntityType getSelectedElement(){
        return (EntityType) this.tableView.getSelectionModel().getSelectedItem();
    }

    public List<EntityType> getSelectedElements(){
        List<EntityType> lst = new ArrayList<>();
        return this.tableView.getSelectionModel().getSelectedItems();
    }
}
