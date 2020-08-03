package sandbox.pixels;

import java.awt.*;

public class Steam extends Pixel {

    private static final long serialVersionUID = -2661004820913782587L;

    public Steam() {
        super("steam", new Color(172, 220, 211));

        this
                .setProperty("gravity", -1)
                .setProperty("density", -10)
                .setProperty("fluidity", 100)
                .setProperty("temperature",100)
                .setProperty("heatConduct", 70)
                .setProperty("cost", 2);
    }

}
