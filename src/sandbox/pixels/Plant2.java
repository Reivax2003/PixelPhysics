package sandbox.pixels;

import java.awt.*;
import java.util.Random;

public class Plant2 extends Pixel {

    public Plant2(int xpos, int ypos) {
        super("plant2", xpos, ypos, Color.green.darker());
        Random r = new Random();
        this
                .setProperty("flammable", 1)
                .setProperty("fuel", 50)
                .setProperty("density", 10)
                .setProperty("gravity", 1)
                .setProperty("support", 1)
                .setProperty("growing", 1)
                .setProperty("angle", 20)
                .setProperty("power", 1)
                .setProperty("loss", 1)
                .setProperty("split", 25)
                .setProperty("turning", r.nextInt(2))
                .setProperty("direction", 0);
    }

}