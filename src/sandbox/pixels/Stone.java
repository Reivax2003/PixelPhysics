package sandbox.pixels;

import java.awt.*;

public class Stone extends Pixel {

    public Stone() {
        super("stone", Color.gray);

        this.setProperty("solubility", 100);
    }

}
