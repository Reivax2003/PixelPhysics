package sandbox.pixels;

import sandbox.Grid;
import sandbox.pixels.types.*;
import java.awt.Color;

public class Stone extends Pixel implements Solid {

    public Stone(int xpos, int ypos) {
        super("stone", xpos, ypos, Color.gray);
    }

    public int[] update(Grid grid) {
        return new int[] { x, y };
    }
}
