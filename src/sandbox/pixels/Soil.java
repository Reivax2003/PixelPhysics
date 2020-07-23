package sandbox.pixels;

import java.awt.*;

public class Soil extends Pixel {
    public Soil(int xpos, int ypos) {
        super("soil", xpos, ypos, Color.orange.darker().darker());

        this
                .setProperty("gravity", 2)
                .setProperty("density", 80)
                .setProperty("support", 1);
    }
}
