package com.example.ics_project_v2;
import javafx.scene.paint.Color;
import java.util.Random;
import java.util.ArrayList;


public class RandomColorGenerator {


    private ArrayList<Color> ColorArr = new ArrayList<Color>();

    private void initializeColors() {
        ColorArr.add(Color.rgb(255, 0, 0)); // Red
        ColorArr.add(Color.rgb(0, 255, 0)); // Green
        ColorArr.add(Color.rgb(0, 0, 255)); // Blue
        ColorArr.add(Color.rgb(0, 255, 255)); // Cyan
        ColorArr.add(Color.rgb(255, 255, 0)); // Yellow
        ColorArr.add(Color.rgb(128, 0, 128)); // Purple
        ColorArr.add(Color.rgb(255, 192, 203)); // Pink
        ColorArr.add(Color.rgb(255, 191, 0)); // Amber
        ColorArr.add(Color.rgb(128, 128, 0)); // Olive
        ColorArr.add(Color.rgb(0, 128, 128)); // Teal
    }
    public RandomColorGenerator() {

        initializeColors();
    }

    public Color getColor() {



        Random rand = new Random();
        return ColorArr.get(rand.nextInt(ColorArr.size()));

    }
}
