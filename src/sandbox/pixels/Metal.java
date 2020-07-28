package sandbox.pixels;

import java.awt.*;

public class Metal extends Pixel {
    public Metal() {
        super("metal", Color.lightGray);

        this.setProperty("conductive", 100)
        .setProperty("cost", 10);
    }
}
