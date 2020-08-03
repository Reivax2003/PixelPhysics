package sandbox.pixels;

import java.awt.*;
import java.util.HashMap;

public class Air extends Pixel {

    private static final long serialVersionUID = -892575887066476821L;

    public Air() {
        super("air", new Color(127, 200, 227));

        this
                .setProperty("density", -1000)
                .setProperty("duplicateImmune", 1)
                .setProperty("overwritable", 1)
                .setProperty("heatConduct", 0)
                .setProperty("cost", 1);
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
