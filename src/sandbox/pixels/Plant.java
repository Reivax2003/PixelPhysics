package sandbox.pixels;

import java.awt.*;

public class Plant extends Pixel {

    public Plant(int xpos, int ypos) {
        super("plant", xpos, ypos, Color.green.darker());
        this
                .setProperty("flammable", 1)
                .setProperty("fuel", 50)
                .setProperty("density", 10)
                .setProperty("gravity", 1)
                .setProperty("growing", 0)
                .setProperty("minheight", 2)
                .setProperty("maxheight", 5)
                .setProperty("speed", 2)
                .setProperty("solubility", 100);
    }

}