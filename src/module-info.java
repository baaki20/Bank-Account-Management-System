module Bank.Account.Management.System {
    requires javafx.controls;
    requires javafx.fxml;

    opens controller to javafx.fxml;    // For controller classes
    opens view to javafx.graphics;      // For FXML files
    opens main to javafx.graphics;
}