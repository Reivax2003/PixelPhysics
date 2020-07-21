package sandbox.pixels;

import sandbox.Grid;
import sandbox.pixels.types.*;
import java.awt.*;

public class Sand extends Pixel implements Granular {

    public Sand(int xpos, int ypos) {
        super("sand", xpos, ypos, Color.yellow);
    }

    public int[] update(Grid grid) {
        int[] newPosition = new int[] {x, y};

        if ((y < grid.getHeight()-1 && (grid.getPixel(x, y + 1) instanceof Empty ||  grid.getPixel(x, y + 1) instanceof Liquid ||  grid.getPixel(x, y + 1) instanceof Gas))) {
            newPosition[0] = x;
            newPosition[1] = y + 1;
        }
        else if (x > 0 && y < grid.getHeight()-1 && (grid.getPixel(x-1, y + 1) instanceof Empty || grid.getPixel(x-1, y + 1) instanceof Liquid || grid.getPixel(x-1, y + 1) instanceof Gas)) {
            newPosition[0] = x - 1;
            newPosition[1] = y + 1;
        }
        else if (x < grid.getWidth()-1 && y < grid.getHeight()-1 && (grid.getPixel(x+1, y + 1) instanceof Empty || grid.getPixel(x+1, y + 1) instanceof Liquid || grid.getPixel(x+1, y + 1) instanceof Gas)) {
            newPosition[0] = x + 1;
            newPosition[1] = y + 1;
        }
        return newPosition;
    }
}
