module com.budget.expansetracker {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.budget.expansetracker to javafx.fxml;
    exports com.budget.expansetracker;
}