package sandbox.pixels;

import java.awt.*;

public class Sand extends Pixel {

    public Sand(int xpos, int ypos) {
        super("sand", xpos, ypos, Color.yellow);

        this
                .setProperty("gravity", 1)
                .setProperty("density", 100);
    }
}
