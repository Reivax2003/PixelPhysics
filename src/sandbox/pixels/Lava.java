package sandbox.pixels;

import java.awt.*;

public class Lava extends Pixel {

    private static final long serialVersionUID = 6754540764146491412L;

    public Lava() {
        super("lava", Color.red);
        this                
                .setProperty("gravity", 1)
                .setProperty("density", 50)
                .setProperty("fluidity", 10)
                .setProperty("spreads", 1)
                .setProperty("temperature", 175)
                .setProperty("heating", 1)
                .setProperty("walkable", -1)
                .setProperty("heatConduct", 75)
                .setProperty("cost", 10);
    }

}