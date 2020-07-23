package sandbox.pixels;

import java.awt.*;

public class Metal extends Pixel {
    public Metal(int xpos, int ypos) {
        super("metal", xpos, ypos, Color.lightGray);

        this.setProperty("conductive", 100);
    }
}
