package sandbox.pixels;

import java.awt.*;

public class Stone extends Pixel {

    public Stone(int xpos, int ypos) {
        super("stone", xpos, ypos, Color.gray);

        this.setProperty("solubility", 100);
    }

}
