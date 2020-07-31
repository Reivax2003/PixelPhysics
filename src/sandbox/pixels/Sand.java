package sandbox.pixels;

import java.awt.*;

public class Sand extends Pixel {

    public Sand() {
        super("sand", new Color(243, 195, 40));

        this
                .setProperty("gravity", 1)
                .setProperty("density", 70)
                .setProperty("support", 1)
                .setProperty("fertile", 1)
                .setProperty("solubility", 50)
                .setProperty("walkable", 1)
                .setProperty("heatConduct", 65)
                .setProperty("cost", 1);
    }

}
