package sandbox.pixels;

import java.awt.*;

public class Charcoal extends Pixel {

    public Charcoal() {
        super("charcoal", Color.darkGray);
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