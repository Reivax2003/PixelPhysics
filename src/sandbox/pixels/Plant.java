package sandbox.pixels;

import java.awt.*;

public class Plant extends Pixel {

    private static final long serialVersionUID = 650213227462159597L;

    public Plant() {
        super("plant", new Color(0,200,0));
        this
                .setProperty("flammable", 1)
                .setProperty("fuel", 50)
                .setProperty("charcoal", 10)
                .setProperty("ash", 3)
                .setProperty("density", 10)
                .setProperty("gravity", 1)
                .setProperty("support", 1)
                .setProperty("growing", 0)
                .setProperty("minheight", 2)
                .setProperty("maxheight", 5)
                .setProperty("speed", 2)
                .setProperty("solubility", 100)
                .setProperty("heatConduct", 75)
                .setProperty("cost", 10)
                .setProperty("nutrients", 35);
    }

}