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
import java.util.Iterator;

public class MainPane {
    private Pane pane;
    private int ChangeColorChance = 5; // Initial number of attempts
    private int score = 0; // Initial score
    private RandomColorGenerator colorGenerator = new RandomColorGenerator();
    private ArrayList<Star> stars = new ArrayList<>();
    private Circle ball;
    private Text scoreText; // Text to display score
    private Text chancesText; // Text to display remaining chances
    private boolean gameOver = false; // Flag to check if the game has ended

    public MainPane() {
        // Initialize the pane
        pane = new Pane();
        ball = new Ball().getBall();

        // Initialize the score and attempts text
        scoreText = new Text("Score: " + score);
        scoreText.setFont(new Font(20));
        scoreText.setFill(Color.WHITE);
        scoreText.setX(10);
        scoreText.setY(30);

        chancesText = new Text("Attempts Left: " + ChangeColorChance);
        chancesText.setFont(new Font(20));
        chancesText.setFill(Color.WHITE);
        chancesText.setX(10);
        chancesText.setY(60);

        // Add the ball, score, and chances text to the pane
        pane.getChildren().addAll(ball, scoreText, chancesText);

        // Update ball's position on mouse move
        pane.setOnMouseMoved(e -> {
            if (!gameOver) {
                ball.setCenterX(e.getX());
                ball.setCenterY(e.getY());
                checkStarCollision();
            }
        });

        // Handle key press event for changing ball color
        pane.setOnKeyPressed(e -> {
            if (!gameOver && e.getCode() == KeyCode.UP) {
                if (ChangeColorChance > 0) {
                    ball.setFill(colorGenerator.getColor());
                    ChangeColorChance--;
                    updateChancesText();
                }
            }
        });

        // Timeline for adding a new star every 2500 milliseconds
        Timeline addStarTimeline = new Timeline(
                new KeyFrame(Duration.millis(2500), e -> {
                    if (!gameOver) {
                        Star newStar = new Star(400, 400, 200, 100, 5, this); // Create a new star
                        stars.add(newStar);
                        pane.getChildren().addAll(newStar.getLines());
                    }
                })
        );
        addStarTimeline.setCycleCount(Timeline.INDEFINITE);
        addStarTimeline.play();

        // Timeline for growing stars every 80 milliseconds
        Timeline growStarsTimeline = new Timeline(
                new KeyFrame(Duration.millis(80), e -> {
                    if (!gameOver) {
                        Iterator<Star> iterator = stars.iterator();
                        while (iterator.hasNext()) {
                            Star star = iterator.next();
                            boolean outOfBounds = star.getLines().stream().anyMatch(line ->
                                    line.getStartX() < 0 || line.getStartY() < 0 || line.getStartX() > 800 || line.getStartY() > 800 ||
                                            line.getEndX() < 0 || line.getEndY() < 0 || line.getEndX() > 800 || line.getEndY() > 800);
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

    public Pane getPane() {
        return pane;
    }

    public void removeStar(Star star) {
        pane.getChildren().removeAll(star.getLines());
        stars.remove(star);
    }

    public void handleWrongCollision() {
        if (gameOver) return;

        ChangeColorChance--; // Decrease attempts when the ball is deleted
        updateChancesText();

        if (ChangeColorChance <= 0) {
            terminateGame();
            return;
        }

        double currentX = ball.getCenterX();
        double currentY = ball.getCenterY();

        pane.getChildren().remove(ball);
        pane.setOnMouseMoved(null);

        // Start a Timeline to respawn the ball after 1 second
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
        chancesText.setText("Attempts Left: " + ChangeColorChance);
    }

    private void terminateGame() {
        gameOver = true;
        pane.getChildren().clear();
        Text gameOverText = new Text("Game Over\nFinal Score: " + score);
        gameOverText.setFont(new Font(40));
        gameOverText.setFill(Color.RED);
        gameOverText.setX(200);
        gameOverText.setY(400);
        pane.getChildren().add(gameOverText);
    }
}
