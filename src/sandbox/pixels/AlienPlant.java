package sandbox.pixels;

import java.awt.*;
import java.util.Random;

public class AlienPlant extends Pixel {

    public AlienPlant(Boolean falls) {
        super("alien plant", Color.cyan);
        this
                .setProperty("flammable", 1)
                .setProperty("fuel", 50)
                .setProperty("charcoal", 30)
                .setProperty("ash", 10)
                .setProperty("density", 10)
                .setProperty("growing", 1)
                .setProperty("angle", 20)
                .setProperty("power", 100)
                .setProperty("loss", 3)
                .setProperty("split", 25)
                .setProperty("turning", 0)
                .setProperty("direction", 0);
            
        if(falls)
            this
                .setProperty("gravity", 1)
                .setProperty("support", 1);
    }

}