package sandbox.pixels;

import java.awt.*;

public class Fire extends Pixel {

    public Fire() {
        super("fire", new Color(1.0f, 0.9f, 0.0f));
        this
                .setProperty("spreads", 1)
                .setProperty("density", 0)
                .setProperty("strength", 100) //property for fire to make flickering flames
                .setProperty("temperature", 175)
                .setProperty("heating", 2)
                .setProperty("maxgap", 5);
    }

}
