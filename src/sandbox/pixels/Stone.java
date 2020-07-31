package sandbox.pixels;

import java.awt.*;

public class Stone extends Pixel {

    public Stone() {
        super("stone", Color.gray);

        this.setProperty("solubility", 100)
            .setProperty("temperature", 50)
            .setProperty("walkable", 1)
            .setProperty("heatConduct", 75)
            .setProperty("cost", 1)
            .setProperty("building", 1)
            .setProperty("stone", 1);
    }

}
