package sandbox.pixels;

import java.awt.*;

public class FusePowder extends Pixel {
    public FusePowder() {
        super("fuse powder", new Color(119, 198, 109));

        this
                .setProperty("flammable", 1)
                .setProperty("fuel", 25)
                .setProperty("burnspeed", 5)
                .setProperty("ash", 5)
                .setProperty("support", 1)
                .setProperty("gravity", 1)
                .setProperty("density", 10)
                .setProperty("cost", 3);
    }
}
