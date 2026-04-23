module com.example.rezerva {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires org.kordamp.bootstrapfx.core;

    opens com.example.rezerva to javafx.fxml;
    exports com.example.rezerva;

    opens com.example.rezerva.domain to javafx.fxml;
    exports com.example.rezerva.domain;

    opens com.example.rezerva.controller to javafx.fxml;
    exports com.example.rezerva.controller;

    opens com.example.rezerva.service to javafx.fxml;
    exports com.example.rezerva.service;
}