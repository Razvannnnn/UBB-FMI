module problema8.client.problema8clientfx {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires net.synedra.validatorfx;
    requires org.kordamp.bootstrapfx.core;

    opens problema8.client.problema8clientfx to javafx.fxml;
    exports problema8.client.problema8clientfx;
}