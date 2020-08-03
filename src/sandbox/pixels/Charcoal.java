package sandbox.pixels;

import java.awt.*;

public class Charcoal extends Pixel {

    private static final long serialVersionUID = 8126228318857844044L;

    public Charcoal() {
        super("charcoal", new Color(58, 43, 28));
        this
                .setProperty("flammable", 1)
                .setProperty("fuel", 500)
                .setProperty("burnspeed", 3)
                .setProperty("ash", 30)
                .setProperty("gravity", 1)
                .setProperty("density", 40)
                .setProperty("support", 1)
                .setProperty("solubility", 30)
                .setProperty("walkable", 1)
                .setProperty("heatConduct", 75)
                .setProperty("cost", 5);
    }
}