package sandbox.pixels;

import java.awt.*;

public class Lava extends Pixel {
    
    public Lava() {
        super("lava", Color.red);
        this                
                .setProperty("gravity", 1)
                .setProperty("density", 50)
                .setProperty("fluidity", 50)
                .setProperty("spreads", 1)
                .setProperty("temperature", 175)
                .setProperty("heating", 1)
                .setProperty("cost", 10);
    }

}