package com.example.ics_project_v2;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.util.Duration;

public class MainApplication extends Application {
    //BallPane ballPane = new BallPane();

    Pane pane = new MainPane().getPane();





    private Scene scene = new Scene(pane ,800 , 800 , Color.BLACK);


    public void start(Stage primaryStage) {



        primaryStage.setScene(scene);
        primaryStage.show();
        pane.requestFocus();



    }



}