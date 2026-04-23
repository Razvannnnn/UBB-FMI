module ro.mpp2025.problema8_fx {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.web;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires net.synedra.validatorfx;
    requires org.kordamp.bootstrapfx.core;
    requires eu.hansolo.tilesfx;
    requires org.apache.logging.log4j;
    requires java.sql;

    opens ro.mpp2025.problema8_fx to javafx.fxml;
    exports ro.mpp2025.problema8_fx;
}