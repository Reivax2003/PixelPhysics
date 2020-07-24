package sandbox.pixels;

import java.awt.*;
import java.util.HashMap;

public class Air extends Pixel {
    public Air(int xpos, int ypos) {
        super("air", xpos, ypos, new Color(30, 30, 30));

        this.setProperty("density", -1000);
    }

    @Override
    public boolean hasMoved() {
        return false;
    }

    public Pixel duplicate() { //Needs to be created as an Air to copy hasMoved()
        Pixel copy = new Air(x, y);
        copy.properties = new HashMap<>(this.properties);
        return (copy);
    }
}
