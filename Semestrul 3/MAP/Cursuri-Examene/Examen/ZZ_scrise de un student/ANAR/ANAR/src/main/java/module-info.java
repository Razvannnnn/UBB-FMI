module com.example.anar {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;


    opens com.example.anar to javafx.fxml;
    exports com.example.anar;
    exports com.example.anar.controller;
    opens com.example.anar.controller to javafx.fxml;
}