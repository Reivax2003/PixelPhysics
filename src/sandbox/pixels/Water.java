package sandbox.pixels;

import sandbox.Grid;
import sandbox.pixels.types.*;

import java.awt.Color;

public class Water extends Pixel implements Liquid {

    public Water(int xpos, int ypos) {
        super("water", xpos, ypos, Color.blue);
    }

    public int[] update(Grid grid) {
        int[] newPosition = new int[] {x, y};

        if (y < grid.getHeight()-1 && (grid.getPixel(x, y+1) instanceof Empty || grid.getPixel(x, y+1) instanceof Gas)) {
            newPosition[0] = x;
            newPosition[1] = y + 1;
        }
        else if (x > 0 && (grid.getPixel(x-1, y) instanceof Empty || grid.getPixel(x-1, y) instanceof Gas) && Math.random() > 0.5) {
            newPosition[0] = x - 1;
            newPosition[1] = y;
        }
        else if (x < grid.getWidth()-1 && (grid.getPixel(x+1, y) instanceof Empty || grid.getPixel(x+1, y) instanceof Gas)) {
            newPosition[0] = x + 1;
            newPosition[1] = y;
        }
        return newPosition;
    }
}