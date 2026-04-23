module com.example.clinicaprivata {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires org.kordamp.bootstrapfx.core;
    requires java.sql;

    opens com.example.clinicaprivata to javafx.fxml;
    opens com.example.clinicaprivata.Controller to javafx.fxml;

    exports com.example.clinicaprivata;
    exports com.example.clinicaprivata.Entities;
}