package com.example.ics_project_v2;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;

import java.util.ArrayList;

public class Edge {


    private Color color = null;
    private double x1 , x2 , y1 , y2;

    private void SetColor() {

        RandomColorGenerator rg = new RandomColorGenerator();
        color = rg.getColor();
    }

    public Color getColor() {
        return color;
    }


    public Edge(double x1, double y1, double x2, double y2) {
        this.x1 = x1;
        this.x2 = x2;
        this.y1 = y1;
        this.y2 = y2;
        SetColor();
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