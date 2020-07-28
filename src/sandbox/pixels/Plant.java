package sandbox.pixels;

import java.awt.*;

public class Plant extends Pixel {

    public Plant() {
        super("plant", Color.green.darker());
        this
                .setProperty("flammable", 1)
                .setProperty("fuel", 50)
                .setProperty("charcoal", 10)
                .setProperty("ash", 3)
                .setProperty("density", 10)
                .setProperty("gravity", 1)
                .setProperty("support", 1)
                .setProperty("growing", 0)
                .setProperty("minheight", 2)
                .setProperty("maxheight", 5)
                .setProperty("speed", 2)
                .setProperty("solubility", 100)
                .setProperty("cost", 10);
    }

}