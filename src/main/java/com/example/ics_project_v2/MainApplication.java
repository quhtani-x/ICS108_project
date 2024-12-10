package com.example.ics_project_v2;

import javafx.application.*;
import javafx.stage.*;

public class MainApplication extends Application {


    @Override
    public void start(Stage primaryStage) {

        WelcomeScene welcomeScene = new WelcomeScene(primaryStage);

        primaryStage.setScene(welcomeScene.getScene());
        primaryStage.show();

    }
}
