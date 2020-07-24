package sandbox.pixels;

import java.awt.*;

public class Slime extends Pixel {

    public Slime(int xpos, int ypos) {
        super("slime", xpos, ypos, Color.green.darker());

        this
                .setProperty("gravity", 0)
                .setProperty("density", 1000)
                //.setProperty("support", 1)
                .setProperty("group", 0)
                .setProperty("stability", 0)
                .setProperty("distance", 0);
    }

}