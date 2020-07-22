package sandbox.pixels;

import java.awt.Color;

public class Stone extends Pixel {

    public Stone(int xpos, int ypos) {
        super("stone", xpos, ypos, Color.gray);
    }

    public Stone Clone() {
        return new Stone(x, y);
    }
}