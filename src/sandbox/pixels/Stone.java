package sandbox.pixels;

import java.awt.Color;

public class Stone extends Pixel implements Solid {

    public Stone(int xpos, int ypos) {
        super("stone", xpos, ypos, Color.gray);
    }

}
