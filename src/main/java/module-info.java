module com.example.homeview {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires org.kordamp.bootstrapfx.core;
    requires java.net.http;
    requires com.google.gson;
    requires java.desktop;
    requires static lombok;

    opens com.example.homeview to javafx.fxml, com.google.gson;
    exports com.example.homeview;
    exports com.example.homeview.model;
    opens com.example.homeview.model to com.google.gson, javafx.fxml;
    exports com.example.homeview.servi;
    opens com.example.homeview.servi to com.google.gson, javafx.fxml;

}