package sandbox.pixels;

import java.awt.*;

public class WetSand extends Pixel {

    public WetSand(int xpos, int ypos) {
        super("wet sand", xpos, ypos, new Color(155, 155, 0));

        this
                .setProperty("gravity", 1)
                .setProperty("density", 70)
                .setProperty("support", 1)
                .setProperty("steepness", 2);
    }

}
