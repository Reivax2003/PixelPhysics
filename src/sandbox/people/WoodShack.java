package sandbox.people;

import java.awt.*;
import sandbox.*;
import sandbox.pixels.*;

public class WoodShack extends Blueprint {

    private static final long serialVersionUID = 3141592;

    private Pixel[][] structure;  //not sure why there are 2 sets of these variables
    private int x;
    private int y;

    public WoodShack(){
        name = "Wood Shack";
        Pixel a = new Air();
        Pixel w = new Wood();
        Pixel r = new Wood();
        r.setColor(new Color(138, 99, 54));

        structure = new Pixel[][]{
                {a, a, a, a, a, a, a, a, a, a, a, a, a, a, a, a, a, r, w, r, w, w},
                {a, a, a, a, a, a, a, a, a, a, w, w, w, w, w, w, w, r, r, w, w, a},
                {a, r, w, r, w, w, w, w, w, w, w, w, w, w, w, w, w, a, w, r, a, a},
                {w, w, r, w, w, w, w, w, w, w, a, a, a, a, a, a, a, a, w, w, a, a},
                {a, a, r, r, a, a, a, a, a, a, a, a, a, a, a, a, a, w, w, a, a, a},
                {a, a, a, w, w, a, a, a, a, a, a, a, a, a, a, a, a, w, w, a, a, a},
                {a, a, a, w, w, a, a, a, a, a, a, a, a, a, a, a, a, w, w, a, a, a},
                {a, a, a, w, w, a, a, a, a, a, a, a, a, a, a, a, a, w, w, a, a, a},
                {a, a, a, w, w, a, a, a, a, a, a, a, a, a, a, a, w, w, a, a, a, a},
                {a, a, a, w, w, a, a, a, a, a, a, a, a, a, a, a, w, w, a, a, a, a},
                {a, a, a, a, w, w, a, a, a, a, a, a, a, a, a, a, w, w, a, a, a, a},
                {a, a, a, a, w, w, a, a, a, a, a, a, a, a, a, a, w, w, a, a, a, a},
                {a, a, a, a, w, w, a, a, a, a, a, a, a, a, a, a, w, w, a, a, a, a},
                {a, a, a, a, w, w, a, a, a, a, a, a, a, a, a, a, w, w, a, a, a, a}
        };
        this.comfort = 100;

        super.setStructure(structure);
    }
}
