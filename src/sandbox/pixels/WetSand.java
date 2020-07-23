package sandbox.pixels;

import java.awt.*;

public class WetSand extends Pixel {

    public WetSand(int xpos, int ypos) {
        super("wet sand", xpos, ypos, new Color(243, 195, 40).darker());

        this
                .setProperty("gravity", 1)
                .setProperty("density", 70)
                .setProperty("support", 1)
                .setProperty("steepness", 2);
    }

}