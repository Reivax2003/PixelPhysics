package sandbox.pixels;

import java.awt.Color;

public class Acid extends Pixel {

    public Acid(int xpos, int ypos) {
        super("acid", xpos, ypos, Color.green.brighter());
        this
                .setProperty("gravity", 1)
                .setProperty("density", 60)
                .setProperty("fluidity", 100)
                .setProperty("acidity", 10);
    }
    
}