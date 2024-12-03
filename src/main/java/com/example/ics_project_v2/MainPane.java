package com.example.ics_project_v2;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.util.Duration;

public class MainPane {
    private Pane pane = null;
    private int ChangeColorChance = 5;
    private RandomColorGenerator colorGenerator = new RandomColorGenerator();
    private Star star; // The star instance
    private Circle ball; // The ball instance

    public MainPane() {
        // Create the Text label for chances left
        Text ChancesLeft = new Text("Color change Chances left: " + ChangeColorChance);
        ChancesLeft.setFont(new Font(12)); // Set font size
        ChancesLeft.setFill(Color.WHITE); // Set text color
        ChancesLeft.setX(10); // Position X
        ChancesLeft.setY(10); // Position Y

        // Initialize the pane
        pane = new Pane();
        ball = new Ball().getBall();  // Create the ball
        star = new Star();  // Create the star

        // Add the ball and star to the pane
        pane.getChildren().add(ball);
        pane.getChildren().addAll(star.getLines());  // Add the star's lines to the pane
        pane.getChildren().add(ChancesLeft);

        // Update ball's position on mouse move
        pane.setOnMouseMoved(e -> {
            ball.setCenterX(e.getX());
            ball.setCenterY(e.getY());
        });

        // Handle key press event for changing ball color
        pane.setOnKeyPressed(e -> {
            if (e.getCode() == KeyCode.UP) {
                if (ChangeColorChance > 0) {
                    ball.setFill(colorGenerator.getColor());
                    ChangeColorChance--;
                    ChancesLeft.setText("Color change Chances left: " + ChangeColorChance);
                } else {
                    ChancesLeft.setText("You are out of chances idiot");
                }
            }
        });

        Timeline timeline = new Timeline(
                new KeyFrame(Duration.seconds(1), e -> {
                    // Gradually increase the radii of the star
                      // Max outer radius is 300
                        pane.getChildren().removeAll(star.getLines());
                        star.Increment();
                        pane.getChildren().addAll(star.getLines());

                })
        );
        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.play();
    }

    public Pane getPane() {
        return pane;
    }
}
