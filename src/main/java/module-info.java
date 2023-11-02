module com.example.escapeyourbedroom {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;


    opens com.example.escapeyourbedroom to javafx.fxml;
    exports com.example.escapeyourbedroom;
}