package sandbox.pixels;

import java.awt.*;

public class Water extends Pixel {

    public Water() {
        super("water", new Color(9, 117, 250));
        this
                .setProperty("gravity", 1)
                .setProperty("density", 50)
                .setProperty("fluidity", 100)
                .setProperty("conductive", 25)
                .setProperty("support", 1)
                .setProperty("temperature",50)
                .setProperty("cost", 1);
    }
}
