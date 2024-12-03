module com.example.ics_project_v2 {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.desktop;


    opens com.example.ics_project_v2 to javafx.fxml;
    exports com.example.ics_project_v2;

}