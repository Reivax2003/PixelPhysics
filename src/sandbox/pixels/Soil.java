package sandbox.pixels;

import java.awt.*;

public class Soil extends Pixel {
    public Soil() {
        super("soil", Color.orange.darker().darker());

        this
                .setProperty("gravity", 1)
                .setProperty("density", 80)
                .setProperty("support", 1)
                .setProperty("fertile", 2)
                .setProperty("solubility", 50)
                .setProperty("walkable", 1)
                .setProperty("heatConduct", 75)
                .setProperty("cost", 5);
    }
}
