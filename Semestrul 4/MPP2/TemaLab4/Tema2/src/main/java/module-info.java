module ro.mpp2025.tema2 {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires net.synedra.validatorfx;
    requires org.kordamp.bootstrapfx.core;
    requires spring.context;
    requires org.apache.logging.log4j;
    requires java.sql;

    opens ro.mpp2025.tema2 to javafx.fxml;
    exports ro.mpp2025.tema2;
}