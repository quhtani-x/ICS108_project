package com.example.ics_project_v2;

import javafx.application.Application;
import javafx.stage.Stage;

public class FlyingStarsGame extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        MainApplication  app = new MainApplication();
        app.start(primaryStage);


    }
}