package sandbox.pixels;

import java.awt.*;
import java.util.Random;

public class AlienPlant extends Pixel {

    public AlienPlant(int xpos, int ypos) {
        super("alien plant", xpos, ypos, Color.cyan);
        this
                .setProperty("flammable", 1)
                .setProperty("fuel", 50)
                .setProperty("density", 10)
                //.setProperty("gravity", 0)
                //.setProperty("support", 1)
                .setProperty("growing", 1)
                .setProperty("angle", 20)
                .setProperty("power", 100)
                .setProperty("loss", 3)
                .setProperty("split", 25)
                .setProperty("turning", 0)
                .setProperty("direction", 0);
    }

}