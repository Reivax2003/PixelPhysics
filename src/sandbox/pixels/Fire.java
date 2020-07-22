package sandbox.pixels;

import sandbox.Grid;

import java.awt.*;
import java.util.Random;
import java.util.ArrayList;

public class Fire extends Pixel{

    public Fire(int xpos, int ypos) {
        super("fire", xpos, ypos, Color.red);
        this
                .setProperty("spreads", 1);
    }

}
