package sandbox.pixels;

import java.awt.*;

public class Slime extends Pixel {

    public Slime() {
        super("slime", Color.green.darker());

        this
                .setProperty("gravity", 2)
                .setProperty("density", 1000)
                .setProperty("group", 1)
                .setProperty("stable", 0)
                .setProperty("distance", 0);
    }

}