package sandbox.pixels;

import java.awt.*;

public class Fuse extends Pixel {
    public Fuse() {
        super("fuse", Color.GREEN.darker().darker());

        this
                .setProperty("flammable", 1)
                .setProperty("burnspeed", 5)
                .setProperty("fuel", 50);
    }
}
