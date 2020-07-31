package sandbox.pixels;

import java.awt.*;

public class Wood extends Pixel {

    public Wood() {
        super("wood", new Color(113, 62, 5));
        this
                .setProperty("flammable", 1)
                .setProperty("fuel", 200)
                .setProperty("charcoal", 50)
                .setProperty("burnspeed", 3)
                .setProperty("ash", 50)
                .setProperty("gravity", 0)
                .setProperty("solubility", 30)
                .setProperty("walkable", 1)
                .setProperty("heatConduct", 75)
                .setProperty("cost", 10)
                .setProperty("building", 1)
                .setProperty("wood", 1);
    }

}
