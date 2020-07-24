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
                .setProperty("temperature", 100)
                .setProperty("heating", 1);
    }

}