package sandbox.pixels;

import java.awt.Color;

public class Ice extends Pixel {

    public Ice() {
        super("ice", new Color(201, 255, 244));

        this
                .setProperty("gravity", 1)
                .setProperty("support", 1)
                .setProperty("density", 40)
                .setProperty("temperature",0)
                .setProperty("heating", -1);
    }
    
}