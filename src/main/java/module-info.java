module lab15 {
    requires javafx.controls;
    requires javafx.fxml;


    opens lab15 to javafx.fxml;
    exports lab15;
}