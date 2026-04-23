module org.example.reteasocializare {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.web;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires net.synedra.validatorfx;
    requires org.kordamp.bootstrapfx.core;
    requires eu.hansolo.tilesfx;
    requires java.sql;

    opens org.example.reteasocializare to javafx.fxml;
    exports org.example.reteasocializare;

    //opens org.example.reteasocializare to javafx.base;
}