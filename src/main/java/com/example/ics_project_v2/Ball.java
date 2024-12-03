package com.example.ics_project_v2;
import javafx.scene.shape.Circle;

public class Ball {

    private Circle ball;
    private RandomColorGenerator colorGenerator = new RandomColorGenerator();


    // Default Constructor
    public Ball() {

        ball = new Circle(200, 200, 10);
        ball.setFill(colorGenerator.getColor());

    }

    public Circle getBall() {

        return ball;
    }


}