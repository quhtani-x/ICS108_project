package com.example.ics_project_v2;
import javafx.scene.paint.Color;

import java.util.ArrayList;

public class Edge {
    RandomColorGenerator rg = new RandomColorGenerator();
    private Color color = rg.getColor();
    private double x1 , x2 , y1 , y2;


    public Color getColor() {
        return color;
    }


    public Edge(double x1, double y1, double x2, double y2) {
        this.x1 = x1;
        this.x2 = x2;
        this.y1 = y1;
        this.y2 = y2;
    }


    public ArrayList<Double> getCordinate(){
        ArrayList<Double> cordinates = new ArrayList<>();
        cordinates.add(x1);
        cordinates.add(y1);
        cordinates.add(x2);
        cordinates.add(y2);
        return cordinates;
    }

}