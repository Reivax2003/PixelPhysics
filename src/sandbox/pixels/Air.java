package sandbox.pixels;

import java.awt.*;

public class Air extends Pixel {
    public Air(int xpos, int ypos) {
        super("air", xpos, ypos, new Color(30, 30, 30));

        this.setProperty("density", -1000);
    }

    @Override
    public boolean hasMoved() {
        return false;
    }
}
