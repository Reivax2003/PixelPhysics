package sandbox.pixels;

import java.awt.*;
import java.util.HashMap;

public class Air extends Pixel {
    public Air() {
        super("air", new Color(30, 30, 30));

        this
                .setProperty("density", -1000)
                .setProperty("overwritable", 1);
    }

    @Override
    public boolean hasMoved() {
        return false;
    }

    public Pixel duplicate() { //Needs to be created as an Air to copy hasMoved()
        Pixel copy = new Air();
        copy.properties = new HashMap<>(this.properties);
        return (copy);
    }
}
