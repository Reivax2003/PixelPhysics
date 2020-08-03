package sandbox.pixels;

import java.awt.*;

public class Metal extends Pixel {

    private static final long serialVersionUID = -5400208934527088023L;

    public Metal() {
        super("metal", Color.lightGray);

        this.setProperty("conductive", 100)
            .setProperty("walkable", 1)
            .setProperty("heatConduct", 100)
            .setProperty("cost", 10);
    }
}
