package sandbox.pixels;

import java.awt.*;

public class WetSand extends Pixel {

    private static final long serialVersionUID = -1889287507882953248L;

    public WetSand() {
        super("wet sand", new Color(243, 195, 40).darker());

        this
                .setProperty("gravity", 1)
                .setProperty("density", 70)
                .setProperty("support", 1)
                .setProperty("steepness", 2)
                .setProperty("solubility", 50)
                .setProperty("walkable", 1)
                .setProperty("heatConduct", 75)
                .setProperty("cost", 2);
    }

}
