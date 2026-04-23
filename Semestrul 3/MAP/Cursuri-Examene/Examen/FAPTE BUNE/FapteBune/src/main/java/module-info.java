module com.example.faptebune {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires org.kordamp.bootstrapfx.core;
    requires java.sql;


    opens com.example.faptebune to javafx.fxml;
    opens com.example.faptebune.Controller to javafx.fxml;

    exports com.example.faptebune;
    exports com.example.faptebune.Entities;
}