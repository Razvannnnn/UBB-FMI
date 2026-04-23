module com.example.vacanta_var_2 {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;


    opens com.example.vacanta_var_2 to javafx.fxml;
    exports com.example.vacanta_var_2;

    opens com.example.vacanta_var_2.domain to javafx.fxml;
    exports com.example.vacanta_var_2.domain;

    opens com.example.vacanta_var_2.repo to javafx.fxml;
    exports com.example.vacanta_var_2.repo;

    opens com.example.vacanta_var_2.service to javafx.fxml;
    exports com.example.vacanta_var_2.service;

    opens com.example.vacanta_var_2.controller to javafx.fxml;
    exports com.example.vacanta_var_2.controller;
}