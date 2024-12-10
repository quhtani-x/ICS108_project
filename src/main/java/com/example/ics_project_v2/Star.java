package com.example.ics_project_v2;

import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;

import java.util.ArrayList;

public class Star {
    private double centerX;
    private double centerY;
    private double outerRadius;
    private double innerRadius;
    private int numPoints;
    private ArrayList<Line> lines = new ArrayList<>();
    private ArrayList<Color> colors = new RandomColorGenerator().getColorArr();

    private MainPane mainPane;

    public Star(double centerX, double centerY, double outerRadius, double innerRadius, int numPoints, MainPane mainPane) {
        this.centerX = centerX;
        this.centerY = centerY;
        this.outerRadius = outerRadius;
        this.innerRadius = innerRadius;
        this.numPoints = numPoints;
        this.mainPane = mainPane;

        generateLines();
    }

    private void generateLines() {
        lines.clear();

        double angleIncrement = 360.0 / numPoints;
        double currentAngle = -90.0;

        for (int i = 0; i < numPoints; i++) {
            double outerX1 = centerX + outerRadius * Math.cos(Math.toRadians(currentAngle));
            double outerY1 = centerY + outerRadius * Math.sin(Math.toRadians(currentAngle));

            double innerAngle = currentAngle + angleIncrement / 2.0;
            double innerX = centerX + innerRadius * Math.cos(Math.toRadians(innerAngle));
            double innerY = centerY + innerRadius * Math.sin(Math.toRadians(innerAngle));

            double outerX2 = centerX + outerRadius * Math.cos(Math.toRadians(currentAngle + angleIncrement));
            double outerY2 = centerY + outerRadius * Math.sin(Math.toRadians(currentAngle + angleIncrement));

            Line outerToInner = createLine(outerX1, outerY1, innerX, innerY, i);
            Line innerToOuter = createLine(innerX, innerY, outerX2, outerY2, i + 1);

            lines.add(outerToInner);
            lines.add(innerToOuter);

            currentAngle += angleIncrement;
        }
    }

    private Line createLine(double x1, double y1, double x2, double y2, int colorIndex) {
        Line line = new Line(x1, y1, x2, y2);
        line.setStroke(colors.get(colorIndex % colors.size()));
        line.setStrokeWidth(2);
        return line;
    }

    public ArrayList<Line> getLines() {
        return lines;
    }

    public void incrementSize() {
        this.outerRadius *= 1.01;
        this.innerRadius *= 1.008;
        generateLines();
    }

    public boolean checkCollision(Circle ball) {
        double ballRadius = ball.getRadius();
        double ballX = ball.getCenterX();
        double ballY = ball.getCenterY();

        for (Line line : lines) {
            double x1 = line.getStartX();
            double y1 = line.getStartY();
            double x2 = line.getEndX();
            double y2 = line.getEndY();


            if (circleIntersectsLine(ballX, ballY, ballRadius, x1, y1, x2, y2)) {
                if (line.getStroke() != null && line.getStroke().equals(ball.getFill())) {
                    mainPane.incrementScore();
                    mainPane.removeStar(this);
                    return true;
                } else {
                    mainPane.handleWrongCollision();
                    return true;
                }
            }
        }
        return false;
    }


    private boolean circleIntersectsLine(double cx, double cy, double radius, double x1, double y1, double x2, double y2) {
        double dx = x2 - x1;
        double dy = y2 - y1;

        if (dx == 0 && dy == 0) {
            return Math.hypot(cx - x1, cy - y1) <= radius;
        }

        double t = ((cx - x1) * dx + (cy - y1) * dy) / (dx * dx + dy * dy);

        t = Math.max(0, Math.min(1, t));

        double closestX = x1 + t * dx;
        double closestY = y1 + t * dy;
        double distance = Math.hypot(cx - closestX, cy - closestY);

        return distance <= radius;
    }


}
