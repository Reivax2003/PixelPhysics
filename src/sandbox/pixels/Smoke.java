package sandbox.pixels;

import java.awt.*;

public class Smoke extends Pixel {
    public Smoke() {
        super("smoke", Color.darkGray);

        this
                .setProperty("gravity", -1)
                .setProperty("density", -10)
                .setProperty("fluidity", 100)
                .setProperty("duration", 100);
    }
}
