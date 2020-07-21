package sandbox.pixels;

import sandbox.Grid;

import java.awt.Color;

public class Stone extends Pixel {

    public Stone(int xpos, int ypos) {
        super("stone", xpos, ypos, Color.gray);
    }
    
    public int[] update(Grid grid) {
        return new int[] { x, y };
    }
}