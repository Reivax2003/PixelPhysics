package sandbox.pixels;

import java.awt.*;

public class WetSand extends Pixel {

    public WetSand() {
        super("wet sand", new Color(243, 195, 40).darker());

        this
                .setProperty("gravity", 1)
                .setProperty("density", 70)
                .setProperty("support", 1)
                .setProperty("steepness", 2)
                .setProperty("solubility", 50);
    }

}
