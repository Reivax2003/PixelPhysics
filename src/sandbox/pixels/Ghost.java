package sandbox.pixels;

import java.awt.*;

public class Ghost extends Pixel {

    public Ghost() {
        super("ghost", new Color(174, 206, 209));
        this
                .setProperty("substantial", 1)
                .setProperty("x", -1)
                .setProperty("y", -1)
                .setProperty("r", 174)
                .setProperty("g", 206)
                .setProperty("b", 209)
                .setProperty("conductive", 100)
                .setProperty("density", Integer.MAX_VALUE);
    }

}
