package sandbox.pixels;

import java.awt.*;

public class Electricity extends Pixel {
    public Electricity() {
        super("electricity", Color.yellow);

        this
                .setProperty("gravity", 1)
                .setProperty("density", -1)
                .setProperty("fragile", 1)
                .setProperty("cost", 1);
    }
}
