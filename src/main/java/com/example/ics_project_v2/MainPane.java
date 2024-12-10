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
    private int ChangeColorChance = 5;
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
        pane = new Pane();
        ball = new Ball().getBall();

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

        pane.getChildren().addAll(ball, scoreText, chancesText);

        pane.setOnMouseMoved(e -> {
            if (!gameOver) {
                ball.setCenterX(e.getX());
                ball.setCenterY(e.getY());
                checkStarCollision();
            }
        });

        pane.setOnKeyPressed(e -> {
            if (!gameOver && e.getCode() == KeyCode.UP) {
                if (ChangeColorChance > 0) {
                    ball.setFill(colorGenerator.getColor());
                    ChangeColorChance--;
                    updateChancesText();
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

        ChangeColorChance--;
        updateChancesText();

        if (ChangeColorChance <= 0) {
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
        chancesText.setText("Attempts Left: " + ChangeColorChance);
    }

    private void terminateGame() {
        gameOver = true;
        pane.getChildren().clear();
        growStarsTimeline.stop();
        addStarTimeline.stop();
        Text gameOverText = new Text("Game Over\nFinal Score: " + score);
        gameOverText.setFont(new Font(40));
        gameOverText.setFill(Color.RED);
        gameOverText.setX(200);
        gameOverText.setY(400);
        pane.getChildren().add(gameOverText);
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
