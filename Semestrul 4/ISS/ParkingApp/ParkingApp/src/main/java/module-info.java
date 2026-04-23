module iss.parkingapp {
    // JavaFX
    requires javafx.controls;
    requires javafx.fxml;

    // Hibernate
    requires org.hibernate.orm.core;
    requires java.naming;
    requires jakarta.persistence;
    requires jakarta.validation;

    // ControlsFX
    requires org.controlsfx.controls;

    // Logging
    requires org.apache.logging.log4j;

    // Open packages
    opens iss.parkingapp to javafx.fxml;
    opens iss.parkingapp.domain to org.hibernate.orm.core;
    opens iss.parkingapp.utils to org.hibernate.orm.core;
}