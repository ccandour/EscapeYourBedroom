module com.example.escapeyourbedroom {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.example.escapeyourbedroom to javafx.fxml;
    exports com.example.escapeyourbedroom;
}