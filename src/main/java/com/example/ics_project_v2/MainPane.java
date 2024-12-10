package com.example.ics_project_v2;
import java.io.*;
import java.util.*;
import javafx.animation.*;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.text.*;
import javafx.util.Duration;

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
    private double growthInterval = 80;
    private double spawnInterval = 2000;
    private RandomColorGenerator colorGenerator = new RandomColorGenerator();
    private ArrayList<Star> stars = new ArrayList<>();
    private Circle ball;
    private Text scoreText;
    private Text chancesText;
    private boolean gameOver = false;

    private Timeline growStarsTimeline;
    private Timeline addStarTimeline;

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

        createAddStarTimeline();
        createGrowStarsTimeline();
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



        Text gameOverText = new Text("GAME OVER");
        gameOverText.setFont(Font.font("System", FontWeight.BOLD, 50));
        gameOverText.setFill(Color.RED);
        gameOverText.setTextAlignment(TextAlignment.CENTER);


        Text nameText = new Text("Name: " + name); // Assume `playerName` is a variable holding the player's name
        nameText.setFont(Font.font("System", FontWeight.NORMAL, 30));
        nameText.setFill(Color.WHITE);

        Text scoreText = new Text("Final Score: " + score);
        scoreText.setFont(Font.font("System", FontWeight.NORMAL, 30));
        scoreText.setFill(Color.WHITE);

        Text colorChangeText = new Text("Number of times color changed: " + (5-ColorChange)); // Assume `colorChangeCount` tracks this
        colorChangeText.setFont(Font.font("System", FontWeight.NORMAL, 30));
        colorChangeText.setFill(Color.WHITE);


        VBox textContainer = new VBox(20);
        textContainer.setPrefWidth(800);
        textContainer.setStyle("-fx-alignment: center;");
        textContainer.setLayoutY(200);


        textContainer.getChildren().addAll(gameOverText, nameText, scoreText, colorChangeText);

        pane.getChildren().add(textContainer);
    }


    public void incrementScore() {
        score++;
        updateScoreText();

        if (score % 3 == 0) {

            growthInterval = Math.max(10, growthInterval / 2);
            growStarsTimeline.stop();
            createGrowStarsTimeline();


            spawnInterval = Math.max(250, spawnInterval / 2); // Ensure a minimum spawn interval
            addStarTimeline.stop();
            createAddStarTimeline();
        }
    }
}
