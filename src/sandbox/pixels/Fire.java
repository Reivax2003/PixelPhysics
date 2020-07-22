package sandbox.pixels;

import sandbox.Grid;

import java.awt.*;
import java.util.Random;
import java.util.ArrayList;

public class Fire extends Pixel{

    public Fire(int xpos, int ypos) {
        super("fire", xpos, ypos, new Color(1.0f, 0.9f, 0.0f));
        this
                .setProperty("spreads", 1)
                .setProperty("density", 0)
                .setProperty("strength", 100); //property for fire to make flickering flames
    }

    public boolean check(Grid grid, int x, int y){
        return (grid.getPixel(x,y).type.equals("air"));
    }
}
