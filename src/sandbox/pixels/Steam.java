package sandbox.pixels;

import java.awt.Color;

import sandbox.pixels.Pixel;

public class Steam extends Pixel {

    public Steam(int xpos, int ypos) {
        super("steam", xpos, ypos, Color.lightGray);

        this
                .setProperty("gravity", -1)
                .setProperty("density", -10)
                .setProperty("fluidity", 100);
    }
    
}