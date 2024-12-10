package com.example.ics_project_v2;
import java.io.*;
import java.util.Scanner;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.util.Duration;

import java.util.ArrayList;
import java.util.Iterator;

public class MainPane {

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
    private Text ChangeColorText;
    private int ColorChange= 5;
    private String name= null;
    private Pane pane;
    private int Attempts = 3;
    private int score = 0;
    private double growthInterval = 80; // Initial growth interval in milliseconds
    private double spawnInterval = 2000; // Initial spawn interval in milliseconds
    private RandomColorGenerator colorGenerator = new RandomColorGenerator();
    private ArrayList<Star> stars = new ArrayList<>();
    private Circle ball;
    private Text scoreText;
    private Text chancesText;
    private boolean gameOver = false;

    private Timeline growStarsTimeline; // To dynamically adjust growth speed
    private Timeline addStarTimeline;   // To dynamically adjust spawn interval

    public MainPane() {

        try {
            Scanner inp = new Scanner(new FileInputStream("LeaderBoard.txt"));

        }
        catch (FileNotFoundException e){
            System.out.println("File not found");
        }
        pane = new Pane();
        ball = new Ball().getBall();

        scoreText = new Text("Score: " + score);
        scoreText.setFont(new Font(20));
        scoreText.setFill(Color.WHITE);
        scoreText.setX(10);
        scoreText.setY(30);

        ChangeColorText = new Text("Change Color Chances left: " + ColorChange);
        ChangeColorText.setX(10);
        ChangeColorText.setY(90);
        ChangeColorText.setFont(new Font(20));
        ChangeColorText.setFill(Color.WHITE);

        chancesText = new Text("Attempts Left: " + Attempts);
        chancesText.setFont(new Font(20));
        chancesText.setFill(Color.WHITE);
        chancesText.setX(10);
        chancesText.setY(60);

        pane.getChildren().addAll(ball, scoreText, chancesText ,ChangeColorText );

        pane.setOnMouseMoved(e -> {
            if (!gameOver) {
                ball.setCenterX(e.getX());
                ball.setCenterY(e.getY());
                checkStarCollision();
            }
        });

        pane.setOnKeyPressed(e -> {
            if (!gameOver && e.getCode() == KeyCode.UP) {
                if (ColorChange > 0) {
                    ball.setFill(colorGenerator.getColor());
                    ColorChange--;
                    //updateChancesText();
                    this.ChangeColorText.setText("Change Color Chances left: " + ColorChange);
                }
            }
        });

        createAddStarTimeline(); // Initialize and start the spawn timeline
        createGrowStarsTimeline(); // Initialize and start the growth timeline
    }

    private void createGrowStarsTimeline() {
        growStarsTimeline = new Timeline(
                new KeyFrame(Duration.millis(growthInterval), e -> {
                    if (!gameOver) {
                        Iterator<Star> iterator = stars.iterator();
                        while (iterator.hasNext()) {
                            Star star = iterator.next();
                            boolean outOfBounds = star.getLines().stream().anyMatch(line ->
                                    line.getStartX() < -50 || line.getStartY() < -50 || line.getStartX() > 850 || line.getStartY() > 850 ||
                                            line.getEndX() < -50 || line.getEndY() < -50 || line.getEndX() > 850 || line.getEndY() > 850);
                            if (outOfBounds) {
                                removeStar(star);
                                iterator.remove();
                            } else {
                                pane.getChildren().removeAll(star.getLines());
                                star.incrementSize();
                                pane.getChildren().addAll(star.getLines());
                            }
                        }
                    }
                })
        );
        growStarsTimeline.setCycleCount(Timeline.INDEFINITE);
        growStarsTimeline.play();
    }

    private void createAddStarTimeline() {
        addStarTimeline = new Timeline(
                new KeyFrame(Duration.millis(spawnInterval), e -> {
                    if (!gameOver) {
                        Star newStar = new Star(400, 400, 200, 100, 5, this);
                        stars.add(newStar);
                        pane.getChildren().addAll(newStar.getLines());
                    }
                })
        );
        addStarTimeline.setCycleCount(Timeline.INDEFINITE);
        addStarTimeline.play();
    }

    public Pane getPane() {
        return pane;
    }

    public void removeStar(Star star) {
        pane.getChildren().removeAll(star.getLines());
        stars.remove(star);
    }

    public void handleWrongCollision() {
        if (gameOver) return;

        Attempts--;
        updateChancesText();

        if (Attempts <= 0) {
            terminateGame();
            return;
        }

        double currentX = ball.getCenterX();
        double currentY = ball.getCenterY();

        pane.getChildren().remove(ball);
        pane.setOnMouseMoved(null);

        Timeline respawnTimeline = new Timeline(
                new KeyFrame(Duration.seconds(1), e -> {
                    ball.setCenterX(currentX);
                    ball.setCenterY(currentY);
                    ball.setFill(colorGenerator.getColor());

                    pane.getChildren().add(ball);
                    pane.setOnMouseMoved(event -> {
                        ball.setCenterX(event.getX());
                        ball.setCenterY(event.getY());
                        checkStarCollision();
                    });
                })
        );
        respawnTimeline.setCycleCount(1);
        respawnTimeline.play();
    }

    private void checkStarCollision() {
        for (Star star : stars) {
            if (star.checkCollision(ball)) {
                return; // Stop checking after the first collision
            }
        }
    }

    private void updateScoreText() {
        scoreText.setText("Score: " + score);
    }

    private void updateChancesText() {
        chancesText.setText("Attempts Left: " + Attempts);
    }

    private void terminateGame() {
        gameOver = true;
        pane.getChildren().clear();
        growStarsTimeline.stop();
        addStarTimeline.stop();


        // Create the "GAME OVER" text
        Text gameOverText = new Text("GAME OVER");
        gameOverText.setFont(Font.font("System", FontWeight.BOLD, 50));
        gameOverText.setFill(Color.RED);
        gameOverText.setTextAlignment(TextAlignment.CENTER);

        // Create the additional texts
        Text nameText = new Text("Name: " + name); // Assume `playerName` is a variable holding the player's name
        nameText.setFont(Font.font("System", FontWeight.NORMAL, 30));
        nameText.setFill(Color.WHITE);

        Text scoreText = new Text("Final Score: " + score);
        scoreText.setFont(Font.font("System", FontWeight.NORMAL, 30));
        scoreText.setFill(Color.WHITE);

        Text colorChangeText = new Text("Number of times color changed: " + (5-ColorChange)); // Assume `colorChangeCount` tracks this
        colorChangeText.setFont(Font.font("System", FontWeight.NORMAL, 30));
        colorChangeText.setFill(Color.WHITE);

        // Use a VBox to stack the texts vertically
        VBox textContainer = new VBox(20); // 20px spacing between texts
        textContainer.setPrefWidth(800); // Match the pane width
        textContainer.setStyle("-fx-alignment: center;"); // Center the VBox content
        textContainer.setLayoutY(200); // Position the VBox on the screen

        // Add texts to the VBox
        textContainer.getChildren().addAll(gameOverText, nameText, scoreText, colorChangeText);

        // Add the VBox to the pane
        pane.getChildren().add(textContainer);
    }


    public void incrementScore() {
        score++;
        updateScoreText();

        // Check if the score is a multiple of 3
        if (score % 3 == 0) {
            // Halve the growth interval
            growthInterval = Math.max(10, growthInterval / 2);
            growStarsTimeline.stop();
            createGrowStarsTimeline();

            // Halve the spawn interval
            spawnInterval = Math.max(250, spawnInterval / 2); // Ensure a minimum spawn interval
            addStarTimeline.stop();
            createAddStarTimeline();
        }
    }
}
