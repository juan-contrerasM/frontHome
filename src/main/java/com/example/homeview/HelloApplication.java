package com.example.homeview;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.fxml.LoadException;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class HelloApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("hello-view.fxml"));
            Scene scene = new Scene(fxmlLoader.load(), 597, 557);
            stage.setTitle("Hello!");
            stage.setScene(scene);
            stage.show();
        }catch (LoadException e){
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        launch();
    }
}