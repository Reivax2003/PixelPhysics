package sandbox.pixels;

import java.awt.*;

public class Charcoal extends Pixel {

    public Charcoal(int xpos, int ypos) {
        super("charcoal", xpos, ypos, Color.darkGray);
        this
                .setProperty("flammable", 1)
                .setProperty("fuel", 1000)
                .setProperty("ash", 30)
                .setProperty("gravity", 1)
                .setProperty("density", 40)
                .setProperty("support", 1)
                .setProperty("solubility", 30);
    }
}