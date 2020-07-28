package sandbox.pixels;

import java.awt.*;

public class Grass extends Pixel {

    public Grass() {
        super("grass", Color.green.darker());
        this
                .setProperty("flammable", 1)
                .setProperty("fuel", 10)
                .setProperty("charcoal", 2)
                .setProperty("ash", 1)
                .setProperty("density", 10)
                .setProperty("gravity", 1)
                .setProperty("support", 1)
                .setProperty("growing", 0)
                .setProperty("spreads", 1)
                .setProperty("solubility", 100)
                .setProperty("cost", 10);
    }

}