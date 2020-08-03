package sandbox.pixels;

import java.awt.*;

public class Gasoline extends Pixel {

    private static final long serialVersionUID = 7258793349868256661L;

    public Gasoline() {
        super("gasoline", new Color(181, 168, 105));
        this
                .setProperty("gravity", 1)
                .setProperty("density", 50)
                .setProperty("fluidity", 100)
                .setProperty("support", 1)
                .setProperty("flammable", 1)
                .setProperty("fuel", 100)
                .setProperty("burnspeed", 5)
                .setProperty("walkable", -1)
                .setProperty("heatConduct", 85)
                .setProperty("cost", 4);
    }
}
