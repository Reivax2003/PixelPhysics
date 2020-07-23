package sandbox.pixels;

import java.awt.Color;

public class Charcoal extends Pixel{

    public Charcoal(int xpos, int ypos) {
        super("charcoal", xpos, ypos, Color.darkGray);
        this
                .setProperty("flammable", 1)
                .setProperty("gravity", 1)
                .setProperty("density", 40)
                .setProperty("support", 1)
                .setProperty("solubility", 30);
    }
}