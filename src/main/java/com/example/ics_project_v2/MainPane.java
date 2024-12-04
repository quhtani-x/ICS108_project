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

import java.util.ArrayList;

public class MainPane {
    private Pane pane = null;
    private int ChangeColorChance = 5;
    private RandomColorGenerator colorGenerator = new RandomColorGenerator();
    private ArrayList<Star> stars = new ArrayList<>();  // Store all stars
    private Circle ball;  // The ball instance

    public MainPane() {
        // Create the Text label for chances left
        Text ChancesLeft = new Text("Color change Chances left: " + ChangeColorChance);
        ChancesLeft.setFont(new Font(12));  // Set font size
        ChancesLeft.setFill(Color.WHITE);  // Set text color
        ChancesLeft.setX(10);  // Position X
        ChancesLeft.setY(10);  // Position Y

        // Initialize the pane
        pane = new Pane();
        ball = new Ball().getBall();  // Create the ball

        // Add the ball to the pane
        pane.getChildren().add(ball);
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
                    ChancesLeft.setText("You are out of chances!");
                }
            }
        });

        // Timeline for adding a new star every 200 milliseconds
        Timeline addStarTimeline = new Timeline(
                new KeyFrame(Duration.millis(1000), e -> {
                    // Create a new star and add it to the list of stars
                    Star newStar = new Star();
                    stars.add(newStar);

                    // Add the new star's lines to the pane
                    pane.getChildren().addAll(newStar.getLines());
                })
        );
        addStarTimeline.setCycleCount(Timeline.INDEFINITE);  // Repeat indefinitely
        addStarTimeline.play();  // Start the animation loop for adding stars

        // Timeline for growing stars every 45 milliseconds
        Timeline growStarsTimeline = new Timeline(
                new KeyFrame(Duration.millis(70), e -> {
                    // Update all stars (including new ones) by incrementing their size
                    for (Star star : stars) {
                        pane.getChildren().removeAll(star.getLines());  // Remove old lines
                        star.Increment();  // Increment the size of the star
                        pane.getChildren().addAll(star.getLines());  // Add the updated lines
                    }
                })
        );
        growStarsTimeline.setCycleCount(Timeline.INDEFINITE);  // Repeat indefinitely
        growStarsTimeline.play();  // Start the animation loop for growing stars
    }

    public Pane getPane() {
        return pane;
    }
}
