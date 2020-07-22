package sandbox.pixels;

import java.awt.Color;

public class Water extends Pixel {

    public Water(int xpos, int ypos) {
        super("water", xpos, ypos, Color.blue);
        this
                .setProperty("gravity", 1)
                .setProperty("density", 50)
                .setProperty("fluidity", 100);
    }

    public Water Clone() {
        return new Water(x, y);
    }
}