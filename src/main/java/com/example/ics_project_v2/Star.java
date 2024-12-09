package com.example.ics_project_v2;

import javafx.scene.paint.Color;
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

    // Generate lines for the star
    private void generateLines() {
        lines.clear();

        double angleIncrement = 360.0 / numPoints; // Angle between outer points
        double currentAngle = -90.0; // Start angle (rotated for upright star)

        for (int i = 0; i < numPoints; i++) {
            // Outer point
            double outerX1 = centerX + outerRadius * Math.cos(Math.toRadians(currentAngle));
            double outerY1 = centerY + outerRadius * Math.sin(Math.toRadians(currentAngle));

            // Inner point
            double innerAngle = currentAngle + angleIncrement / 2.0;
            double innerX = centerX + innerRadius * Math.cos(Math.toRadians(innerAngle));
            double innerY = centerY + innerRadius * Math.sin(Math.toRadians(innerAngle));

            // Next outer point
            double outerX2 = centerX + outerRadius * Math.cos(Math.toRadians(currentAngle + angleIncrement));
            double outerY2 = centerY + outerRadius * Math.sin(Math.toRadians(currentAngle + angleIncrement));

            // Create lines
            Line outerToInner = createLine(outerX1, outerY1, innerX, innerY, i);
            Line innerToOuter = createLine(innerX, innerY, outerX2, outerY2, i + 1);

            lines.add(outerToInner);
            lines.add(innerToOuter);

            // Increment angle
            currentAngle += angleIncrement;
        }
    }

    // Create a line with a specific color
    private Line createLine(double x1, double y1, double x2, double y2, int colorIndex) {
        Line line = new Line(x1, y1, x2, y2);
        line.setStroke(colors.get(colorIndex % colors.size()));
        line.setStrokeWidth(2); // Set line thickness if desired
        return line;
    }

    // Increment the star's size
    public void incrementSize() {
        this.outerRadius *= 1.01;
        this.innerRadius *= 1.008;
        generateLines();
    }

    // Get the star's lines for rendering
    public ArrayList<Line> getLines() {
        return lines;
    }

    // Check for collisions with a ball
    public boolean checkCollision(javafx.scene.shape.Circle ball) {
        double ballRadius = ball.getRadius();

        for (Line line : lines) {
            double lineWidth = ballRadius * 0.8; // Narrow bounding box
            double x1 = line.getStartX();
            double y1 = line.getStartY();
            double x2 = line.getEndX();
            double y2 = line.getEndY();

            // Create a custom bounding box around the line
            double minX = Math.min(x1, x2) - lineWidth / 2;
            double minY = Math.min(y1, y2) - lineWidth / 2;
            double maxX = Math.max(x1, x2) + lineWidth / 2;
            double maxY = Math.max(y1, y2) + lineWidth / 2;

            // Check if the ball's bounding box intersects the custom bounding box
            if (ball.getBoundsInParent().intersects(minX, minY, maxX - minX, maxY - minY)) {
                if (line.getStroke() != null && line.getStroke().equals(ball.getFill())) {
                    // Correct collision
                    mainPane.removeStar(this);
                    return true;
                } else {
                    // Wrong collision
                    mainPane.handleWrongCollision();
                    return true;
                }
            }
        }
        return false;
    }
}
