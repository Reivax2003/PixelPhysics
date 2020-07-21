package sandbox.pixels;

import java.awt.Color;

import sandbox.pixels.types.Empty;

public class Air extends Pixel implements Empty {

    public Air(int xpos, int ypos) {
        super("air", xpos, ypos, Color.black);
    }
    
}