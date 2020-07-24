package sandbox.pixels;

import java.awt.Color;

public class Ash extends Pixel {
    public Ash() {
        super("ash", Color.gray);

        this
                .setProperty("gravity", 1)
                .setProperty("density", 40)
                .setProperty("support", 1)
                .setProperty("solubility", 50)
                .setProperty("duration", 300);
    }
}