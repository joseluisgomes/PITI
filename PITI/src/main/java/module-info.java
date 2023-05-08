module javafxui {
    requires javafx.controls;
    requires javafx.fxml;
    requires jssc;

    opens ui to javafx.fxml;
    opens ui.controllers to javafx.fxml;

    exports ui;
}