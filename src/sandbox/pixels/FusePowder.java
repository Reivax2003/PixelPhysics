package sandbox.pixels;

import java.awt.*;

public class FusePowder extends Pixel {

    private static final long serialVersionUID = -994074947660529441L;

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
                .setProperty("walkable", 1)
                .setProperty("heatConduct", 70)
                .setProperty("cost", 3);
    }
}
