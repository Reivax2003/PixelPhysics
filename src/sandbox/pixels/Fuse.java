package sandbox.pixels;

import java.awt.*;

public class Fuse extends Pixel {

    private static final long serialVersionUID = -9036934665534310832L;

    public Fuse() {
        super("fuse", Color.GREEN.darker().darker());

        this
                .setProperty("flammable", 1)
                .setProperty("burnspeed", 5)
                .setProperty("fuel", 50)
                .setProperty("walkable", 1)
                .setProperty("heatConduct", 100)
                .setProperty("cost", 3);
    }
}
