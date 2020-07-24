package sandbox.pixels;

import java.awt.*;

public class Plant3 extends Pixel {

    public Plant3() {
        super("plant3", Color.green);
        this
                .setProperty("flammable", 1)
                .setProperty("fuel", 50)
                .setProperty("density", 10)
                .setProperty("gravity", 1)
                .setProperty("support", 1)
                .setProperty("growing", 0)
                .setProperty("speed", 10)       //chance to grow
                .setProperty("split", 40)       //chance to split
                .setProperty("direction", 0)    //current moving direction (delta x)
                .setProperty("turning", 30)     //chance to change direction
                .setProperty("power", 80);      //chance to keep growing
    }

}