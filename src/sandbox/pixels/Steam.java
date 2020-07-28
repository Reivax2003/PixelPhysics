package sandbox.pixels;

import java.awt.*;

public class Steam extends Pixel {

    public Steam() {
        super("steam", new Color(172, 220, 211));

        this
                .setProperty("gravity", -1)
                .setProperty("density", -10)
                .setProperty("fluidity", 100)
                .setProperty("temperature",100)
                .setProperty("cost", 2);
    }

}
