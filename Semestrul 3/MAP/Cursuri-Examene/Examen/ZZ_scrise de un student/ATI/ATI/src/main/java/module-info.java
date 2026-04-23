module com.example.ati {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires org.kordamp.bootstrapfx.core;
    requires java.sql;

    opens com.example.ati to javafx.fxml;
    opens com.example.ati.Controller to javafx.fxml;
    exports com.example.ati;
    exports com.example.ati.Entities;
}