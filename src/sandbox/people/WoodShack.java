package sandbox.people;

import java.awt.*;
import sandbox.*;
import sandbox.pixels.*;

public class WoodShack extends Blueprint {

    private static final long serialVersionUID = 3141592;

    private Pixel[][] structure;
    private int x;
    private int y;

    public WoodShack(){
        Pixel a = new Air();
        a.addProperty("structure", 1);
        a.changeProperty("density", -100000);
        Pixel w = new Wood();
        w.addProperty("structure", 1);
        w.changeProperty("density", -100000);
        Pixel r = new Wood();
        r.addProperty("structure", 1);
        r.setColor(new Color(138, 99, 54));
        r.changeProperty("density", -100000);

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

        super.setStructure(structure);
    }
}
