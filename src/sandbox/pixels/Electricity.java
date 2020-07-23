package sandbox.pixels;

import java.awt.*;

public class Electricity extends Pixel {
    public Electricity(int xpos, int ypos) {
        super("electricity", xpos, ypos, Color.yellow);

        this
                .setProperty("gravity", 1)
                .setProperty("density", -1)
                .setProperty("fragile", 1);
    }
}
