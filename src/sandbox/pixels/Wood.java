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
                .setProperty("solubility", 30);
    }

}
