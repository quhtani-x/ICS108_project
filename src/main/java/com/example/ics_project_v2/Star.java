package com.example.ics_project_v2;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.util.Duration;

import java.util.ArrayList;
import java.util.Random;

public class Star {
    private RandomColorGenerator rg = new RandomColorGenerator();
    private final ArrayList<Color> Colors = rg.getColorArr();


    private ArrayList<Edge> edges = new ArrayList<>();
    private ArrayList<Line> lines = new ArrayList<>();

    // Fixed center for the star (center of an 800x800 window)
    private static final double CENTER_X = 400.0;  // Center X for an 800x800 screen
    private static final double CENTER_Y = 400.0;  // Center Y for an 800x800 screen
    private double outerRadius = 200;  // Default outer radius
    private double innerRadius = 100;  // Default inner radius
    private static final int NUM_POINTS = 5;  // Fixed number of points for a 5-pointed star

    public Star() {
        generateStarEdges();  // Generate the initial edges based on the default radii
        convertEdgeToLine();  // Convert edges to lines for rendering
    }

    // Method to generate the edges of the star based on the current radii
    private void generateStarEdges() {
        edges.clear();  // Clear previous edges

        double angle = 360.0 / NUM_POINTS;  // Angle between each point
        double offsetAngle = -90.0;  // Rotate the star to make it upright

        for (int i = 0; i < NUM_POINTS; i++) {
            // Outer point (on the circumference of the circle)
            double outerX = CENTER_X + outerRadius * Math.cos(Math.toRadians(i * angle + offsetAngle));
            double outerY = CENTER_Y + outerRadius * Math.sin(Math.toRadians(i * angle + offsetAngle));

            // Inner point (closer to the center of the circle)
            double innerX = CENTER_X + innerRadius * Math.cos(Math.toRadians(i * angle + angle / 2 + offsetAngle));
            double innerY = CENTER_Y + innerRadius * Math.sin(Math.toRadians(i * angle + angle / 2 + offsetAngle));

            // Add the edge from the outer point to the inner point and then back to the next outer point
            int nextPoint = (i + 1) % NUM_POINTS;
            double nextOuterX = CENTER_X + outerRadius * Math.cos(Math.toRadians(nextPoint * angle + offsetAngle));
            double nextOuterY = CENTER_Y + outerRadius * Math.sin(Math.toRadians(nextPoint * angle + offsetAngle));

            // Create edges alternating between outer and inner points
            edges.add(new Edge(outerX, outerY, innerX, innerY));  // Edge from outer to inner
            edges.add(new Edge(innerX, innerY, nextOuterX, nextOuterY));  // Edge from inner to outer
        }
    }

    // Getter to access the edges
    public ArrayList<Edge> getEdges() {
        return edges;
    }

    // Method to convert edges to Line objects for rendering
    public void convertEdgeToLine() {
        lines.clear();

        int i = 0;
        for (Edge edge : edges) {

            ArrayList<Double> cord = edge.getCordinate();
            double x1 = cord.get(0);
            double y1 = cord.get(1);
            double x2 = cord.get(2);
            double y2 = cord.get(3);
            Line line = new Line(x1, y1, x2, y2);
            line.setStroke(Colors.get(i));  // Set the color of the edge (can be modified if needed)
            lines.add(line);
            i+=1;
        }
    }

    // Method to change the radii of the star
    public void Increment() {
        // Update the radii
        this.outerRadius *= 1.01;
        this.innerRadius *= 1.008;

        // Regenerate the edges with the new radii
        generateStarEdges();

        // Convert the updated edges to lines for rendering
        convertEdgeToLine();
    }

    // Getter to access the lines (for rendering)
    public ArrayList<Line> getLines() {
        return lines;
    }



    }

