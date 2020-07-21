package sandbox.pixels;

import sandbox.Grid;
import sandbox.pixels.types.*;
import java.awt.*;

public class Wood extends Pixel implements Solid {

    public Wood(int xpos, int ypos) {
        super("wood", xpos, ypos, Color.orange);
    }
    public int[] update(Grid grid) {
        return new int[] { x, y };
    }
}
