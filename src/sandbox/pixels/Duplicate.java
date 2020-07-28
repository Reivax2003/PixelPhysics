package sandbox.pixels;

import java.awt.*;

public class Duplicate extends Pixel {

    public Duplicate() {
        super("duplicate", new Color(130, 50, 38));

        this
                .setProperty("active", 1)
                .setProperty("duplicateImmune", 1)
                .setProperty("conductive", 100)
                .setProperty("cost", 100);
    }

}