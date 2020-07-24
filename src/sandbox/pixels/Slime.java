package sandbox.pixels;

import java.awt.*;

public class Slime extends Pixel {

    public Slime() {
        super("slime", Color.green.darker());

        this
                .setProperty("gravity", 0)
                .setProperty("density", 1000)
                //.setProperty("support", 1)
                .setProperty("group", 1)
                .setProperty("stability", 0)
                .setProperty("distance", 0);
    }

}