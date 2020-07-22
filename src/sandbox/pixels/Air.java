package sandbox.pixels;

import java.awt.*;

public class Air extends Pixel {
    public Air(int xpos, int ypos) {
        super("air", xpos, ypos, Color.black);

        this.setProperty("density", -1);
    }

    public Air Clone() {
        return new Air(x, y);
    }
}
