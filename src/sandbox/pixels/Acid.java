package sandbox.pixels;

import java.awt.*;

public class Acid extends Pixel {

    private static final long serialVersionUID = 4447587211276473881L;

    public Acid() {
        super("acid", Color.green.brighter());
        this
                .setProperty("gravity", 1)
                .setProperty("density", 60)
                .setProperty("fluidity", 100)
                .setProperty("acidity", 10)
                .setProperty("walkable", -1)
                .setProperty("heatConduct", 85)
                .setProperty("cost", 5);
    }

}