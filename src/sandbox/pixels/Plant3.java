package sandbox.pixels;

import java.awt.*;

public class Plant3 extends Pixel {

    private static final long serialVersionUID = -8969096600779838529L;

    public Plant3() {
        super("plant3", Color.green);
        this
                .setProperty("flammable", 1)
                .setProperty("fuel", 50)
                .setProperty("charcoal", 10)
                .setProperty("ash", 3)
                .setProperty("density", 10)
                .setProperty("gravity", 1)
                .setProperty("support", 1)
                .setProperty("growing", 0)
                .setProperty("speed", 10)       //chance to grow
                .setProperty("split", 40)       //chance to split
                .setProperty("direction", 0)    //current moving direction (delta x)
                .setProperty("turning", 30)     //chance to change direction
                .setProperty("power", 100)       //chance to keep growing
                .setProperty("loss", 5)
                .setProperty("heatConduct", 75)
                .setProperty("cost", 50)
                .setProperty("nutrients", 50);
    }

}