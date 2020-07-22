package sandbox.pixels;

import java.awt.*;

public class Smoke extends Pixel {
    public Smoke(int xpos, int ypos) {
        super("smoke", xpos, ypos, Color.darkGray);

        this
                .setProperty("gravity", -1)
                .setProperty("density", -10)
                .setProperty("fluidity", 100);
    }
}
