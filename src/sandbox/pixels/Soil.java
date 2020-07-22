package sandbox.pixels;

import java.awt.*;

public class Soil extends Pixel {
    public Soil(int xpos, int ypos) {
        super("soil", xpos, ypos, Color.orange.darker().darker());

        this
                .setProperty("gravity", 1)
                .setProperty("density", 80)
                .setProperty("support", 1);
    }

    public Soil Clone() {
        return new Soil(x, y);
    }
}
