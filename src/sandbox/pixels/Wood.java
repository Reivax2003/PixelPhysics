package sandbox.pixels;

import java.awt.*;

public class Wood extends Pixel {

    public Wood(int xpos, int ypos) {
        super("wood", xpos, ypos, new Color(113, 62, 5));
        this
                .setProperty("flammable", 1);
    }

}
