package com.example.ics_project_v2;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public class MainApplication extends Application {

    //Pane pane = new MainPane().getPane();

    //private Scene mainScene = new Scene(pane, 800, 800, Color.BLACK);

    @Override
    public void start(Stage primaryStage) {
        // Create the WelcomeScene and pass the mainScene
        //pane.setStyle("-fx-background-color: black;");
        WelcomeScene welcomeScene = new WelcomeScene(primaryStage);

        // Set the initial scene to WelcomeScene
        primaryStage.setScene(welcomeScene.getScene());
        primaryStage.show();

        //pane.requestFocus();
    }
}
