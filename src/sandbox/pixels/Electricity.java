package sandbox.pixels;

import java.awt.*;

public class Electricity extends Pixel {

    private static final long serialVersionUID = -3642192969905888574L;

    public Electricity() {
        super("electricity", Color.yellow);

        this
                .setProperty("gravity", 1)
                .setProperty("density", -1)
                .setProperty("fragile", 1)
                .setProperty("heatConduct", 0)
                .setProperty("cost", 1);
    }
}
