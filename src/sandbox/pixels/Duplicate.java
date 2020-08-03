package sandbox.pixels;

import java.awt.*;

public class Duplicate extends Pixel {

    private static final long serialVersionUID = 8067846866305636835L;

    public Duplicate() {
        super("duplicate", new Color(178, 68, 53));

        this
                .setProperty("active", 1)
                .setProperty("duplicateImmune", 1)
                .setProperty("conductive", 100)
                .setProperty("walkable", 1)
                .setProperty("heatConduct", 100)
                .setProperty("cost", 100);
    }

}