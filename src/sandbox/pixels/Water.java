package sandbox.pixels;

import java.awt.Color;

public class Water extends Pixel {

    public Water(int xpos, int ypos) {
        super("water", xpos, ypos, new Color(9, 117, 250));
        this
                .setProperty("gravity", 1)
                .setProperty("density", 50)
                .setProperty("fluidity", 100)
                .setProperty("wet", 1);
    }
}