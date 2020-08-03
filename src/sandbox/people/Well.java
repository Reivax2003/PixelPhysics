package sandbox.people;

import java.awt.*;
import sandbox.*;
import sandbox.pixels.*;

public class Well extends Blueprint {

    private Pixel[][] structure;
    private int x;
    private int y;

    public Well(){
        name = "Well";
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

        Pixel l = new Stone(); //light gray
        l.setColor(new Color(138, 138, 138));
        Pixel g = new Stone(); //gray
        g.setColor(new Color(122, 122, 122));
        Pixel r = new Stone(); //dark gray
        r.setColor(new Color(107, 107, 107));

        structure = new Pixel[][]{
                {a, a, w, s, a, a, a, a, a, a, a, a, w, s, a, a},
                {a, b, w, s, d, p, p, l, g, p, g, p, w, s, d, g},
                {a, a, w, s, a, a, a, l, a, a, a, a, w, s, a, g},
                {a, a, w, s, a, a, a, l, a, a, a, a, w, s, a, g},
                {a, a, w, s, a, a, a, h, a, a, a, a, w, s, a, g},
                {a, a, w, s, a, a, h, a, w, a, a, a, w, s, a, a},
                {a, a, w, s, a, a, l, g, r, a, a, a, w, s, a, a},
                {a, a, w, s, a, a, l, g, r, a, a, a, w, s, a, a},
                {a, a, w, s, a, a, l, g, r, a, a, a, w, s, a, a},
                {a, a, w, s, a, a, a, a, a, a, a, a, w, s, a, a},
                {a, a, w, s, a, l, l, a, a, l, l, l, w, s, a, a},
                {a, l, l, s, l, g, g, l, l, g, g, g, l, l, l, a},
                {l, g, g, l, g, g, g, g, r, g, g, l, g, g, g, r},
                {l, g, g, g, r, g, g, g, r, l, l, g, r, l, l, r},
                {l, g, g, g, r, g, g, g, l, g, g, g, g, r, g, r},
                {r, r, r, r, r, r, r, r, r, r, r, r, r, r, r, r}

        };

        super.setStructure(structure);
    }
}
