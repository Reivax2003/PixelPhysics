package sandbox.pixels;

import java.awt.*;

public class Slime extends Pixel {

    public Slime() {
        super("slime", Color.green.darker());

        this
                .setProperty("gravity", 1)
                .setProperty("density", -1)
                .setProperty("support", 1)
                .setProperty("group", 1)
                .setProperty("stable", 0)
                .setProperty("distance", 0)
                .setProperty("cost", 10);
    }

}