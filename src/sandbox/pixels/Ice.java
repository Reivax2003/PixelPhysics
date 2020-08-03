package sandbox.pixels;

import java.awt.Color;

public class Ice extends Pixel {

    private static final long serialVersionUID = -6576878356347022909L;

    public Ice() {
        super("ice", new Color(201, 255, 244));

        this
                .setProperty("gravity", 1)
                .setProperty("support", 1)
                .setProperty("density", 40)
                .setProperty("temperature",0)
                .setProperty("heating", -1)
                .setProperty("walkable", 1)
                .setProperty("heatConduct", 95)
                .setProperty("cost", 5);
    }
    
}