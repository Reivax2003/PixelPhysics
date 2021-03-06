package sandbox.pixels;

import java.awt.*;

public class Smoke extends Pixel {

    private static final long serialVersionUID = -3527112226200948313L;

    public Smoke() {
        super("smoke", Color.darkGray);

        this
                .setProperty("gravity", -1)
                .setProperty("density", -10)
                .setProperty("fluidity", 100)
                .setProperty("duration", 100)
                .setProperty("heatConduct", 60)
                .setProperty("cost", 1);
    }
}
