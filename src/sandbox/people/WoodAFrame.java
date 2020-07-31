package sandbox.people;

import java.awt.*;
import sandbox.*;
import sandbox.pixels.*;

public class WoodAFrame extends Blueprint {

    private Pixel[][] structure;
    private int x;
    private int y;

    public WoodAFrame(){
        Pixel a = new Air();

        Pixel h = new Wood(); //highlight for wood
        h.setColor(new Color(138, 77, 11));
        Pixel w = new Wood(); //wood (dark)
        w.setColor(new Color(113, 63, 5));
        Pixel s = new Wood(); //shadow for wood
        s.setColor(new Color(84, 48, 8));

        Pixel b = new Wood(); //bright for plank
        b.setColor(new Color(176, 123, 70));
        Pixel p = new Wood(); //plank (light wood)
        p.setColor(new Color(163, 113, 57));
        Pixel d = new Wood(); //dark for plank
        d.setColor(new Color(135, 94, 49));

        a.addProperty("structure", 1);
        a.changeProperty("density", -100000);

        h.addProperty("structure", 1);
        h.changeProperty("density", -100000);
        w.addProperty("structure", 1);
        w.changeProperty("density", -100000);
        s.addProperty("structure", 1);
        s.changeProperty("density", -100000);

        b.addProperty("structure", 1);
        b.changeProperty("density", -100000);
        p.addProperty("structure", 1);
        p.changeProperty("density", -100000);
        d.addProperty("structure", 1);
        d.changeProperty("density", -100000);

        structure = new Pixel[][]{
                {a, a, a, a, a, a, a, a, a, a, h, h, a, a, a, a, a, a, a, a, a, a},
                {a, a, a, a, a, a, a, a, a, h, w, w, w, a, a, a, a, a, a, a, a, a},
                {a, a, a, a, a, a, a, a, a, h, s, s, w, a, a, a, a, a, a, a, a, a},
                {a, a, a, a, a, a, a, a, h, w, b, d, w, w, a, a, a, a, a, a, a, a},
                {a, a, a, a, a, a, a, h, w, s, b, d, s, w, w, a, a, a, a, a, a, a},
                {a, a, a, a, a, a, a, h, w, p, b, d, p, w, w, a, a, a, a, a, a, a},
                {a, a, a, a, a, a, h, w, s, p, b, d, p, s, w, w, a, a, a, a, a, a},
                {a, a, a, a, a, h, w, s, d, p, b, d, p, b, s, w, w, a, a, a, a, a},
                {a, a, a, a, a, h, w, b, d, p, b, d, p, b, d, w, w, a, a, a, a, a},
                {a, a, a, a, h, w, s, b, d, s, s, s, s, b, d, s, w, w, a, a, a, a},
                {a, a, a, h, w, s, p, b, s, w, w, w, w, w, d, p, s, w, w, a, a, a},
                {a, a, a, h, w, d, p, s, w, w, w, w, w, w, w, p, b, w, w, a, a, a},
                {a, a, h, w, s, d, p, s, w, w, w, w, w, w, w, p, b, s, w, w, a, a},
                {a, h, w, s, b, d, p, s, w, w, w, w, d, w, w, p, b, d, s, w, w, a},
                {a, h, w, p, b, d, p, s, w, w, w, w, w, w, w, p, b, d, p, w, w, a},
                {h, w, s, p, b, d, p, b, w, w, w, w, w, w, d, p, b, d, p, s, w, w},
                {h, s, d, d, d, d, d, d, d, w, w, w, w, d, d, d, d, d, d, d, s, w}
        };

        super.setStructure(structure);
    }
}
