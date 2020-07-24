package sandbox.pixels;

import java.awt.*;

public class Air extends Pixel {
    public Air() {
        super("air", new Color(30, 30, 30));

        this
                .setProperty("density", -1000)
                .setProperty("overwritable", 1);
    }

    @Override
    public boolean hasMoved() {
        return false;
    }
}
