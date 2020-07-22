package sandbox.pixels;

import java.awt.*;

public class Sand extends Pixel {

    public Sand(int xpos, int ypos) {
        super("sand", xpos, ypos, new Color(243, 195, 40));

        this
                .setProperty("gravity", 1)
                .setProperty("density", 70)
                .setProperty("support", 1);
    }

}
