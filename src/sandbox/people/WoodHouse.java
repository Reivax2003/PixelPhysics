package sandbox.people;

import java.awt.*;
import sandbox.*;
import sandbox.pixels.*;

public class WoodHouse extends Blueprint {

    private static final long serialVersionUID = 3141592;

    private Pixel[][] structure;
    private int x;
    private int y;

    public WoodHouse(){
        name = "Wood House";
        Pixel a = new Air();

        Pixel h = new Wood(); //highlight for wood
        h.setColor(new Color(138, 77, 11));
        h.addProperty("walkable", 0);
        Pixel w = new Wood(); //wood (dark)
        w.setColor(new Color(113, 63, 5));
        w.addProperty("walkable", 0);
        Pixel s = new Wood(); //shadow for wood
        s.setColor(new Color(84, 48, 8));
        s.addProperty("walkable", 0);

        Pixel b = new Wood(); //bright for plank
        b.setColor(new Color(176, 123, 70));
        b.addProperty("walkable", 0);
        Pixel p = new Wood(); //plank (light wood)
        p.setColor(new Color(163, 113, 57));
        p.addProperty("walkable", 0);
        Pixel d = new Wood(); //dark for plank
        d.setColor(new Color(135, 94, 49));
        d.addProperty("walkable", 0);

        Pixel l = new Stone(); //light gray
        l.setColor(new Color(138, 138, 138));
        l.addProperty("walkable", 0);
        Pixel g = new Stone(); //gray
        g.setColor(new Color(122, 122, 122));
        g.addProperty("walkable", 0);
        Pixel r = new Stone(); //dark gray
        r.setColor(new Color(107, 107, 107));
        r.addProperty("walkable", 0);

        /*
                  hh
                 hwww
                hwssww
               hwsbdsww
              hwspbdpsww
             hwsdpbdpbsww
            hwsbdpbdpbdsww
           hwspbdpbdpbdpsww
          hwsdpbdpsspbdpbsww
         hwsbdpbdswwwbdpbdsww
        hwspbdpbswwwwwdpbdpsww
        hwspbdpbswwwwwdpbdpssw
          spbdpbswwwwwdpbdps
          spbllbswwwwwdlbdll
         llplggrswwwwwlgllggr
        lggrgggrswwwgwggggrggr
        lgggrggrswwwwwggggrggr
        lgggrggrswwwwwgggggrgr
        rrrrrrrrswwwwwrrrrrrrr
         */

        structure = new Pixel[][]{
                {a, a, a, a, a, a, a, a, a, a, h, h, a, a, a, a ,a, a, a, a, a, a},
                {a, a, a, a, a, a, a, a, a, h, w, w, w, a, a, a, a, a, a, a, a, a},
                {a, a, a, a, a, a, a, a, h, w, s, s, w, w, a, a, a, a, a, a, a, a},
                {a, a, a, a, a, a, a, h, w, s, b, d, s, w, w, a, a, a, a, a, a, a},
                {a, a, a, a, a, a, h, w, s, p, b, d, p, s, w, w, a, a, a, a, a, a},
                {a, a, a, a, a, h, w, s, d, p, b, d, p, b, s, w, w, a, a, a, a, a},
                {a, a, a, a, h, w, s, b, d, p, b, d, p, b, d, s, w, w, a, a, a, a},
                {a, a, a, h, w, s, p, b, d, p, b, d, p, b, d, p, s, w, w, a, a, a},
                {a, a, h, w, s, d, p, b, d, p, s, s, p, b, d, p, b, s, w, w, a, a},
                {a, h, w, s, b, d, p, b, d, s, w, w, w, b, d, p, b, d, s, w, w, a},
                {h, w, s, p, b, d, p, b, s, w, w, w, w, w, d, p, b, d, p, s, w, w},
                {h, w, s, p, b, d, p, b, s, w, w, w, w, w, d, p, b, d, p, s, s, w},
                {a, a, s, p, b, d, p, b, s, w, w, w, w, w, d, p, b, d, p, s, a, a},
                {a, a, s, p, b, l, l, b, s, w, w, w, w, w, d, l, b, d, l, l, a, a},
                {a, l, l, p, l, g, g, r, s, w, w, w, w, w, l, g, l, l, g, g, r, a},
                {l, g, g, r, g, g, g, r, s, w, w, w, g, w, g, g, g, g, r, g, g, r},
                {l, g, g, g, r, g, g, r, s, w, w, w, w, w, g, g, g, g, r, g, g, r},
                {l, g, g, g, r, g, g, r, s, w, w, w, w, w, g, g, g, g, g, r, g, r},
                {r, r, r, r, r, r, r, r, s, w, w, w, w, w, r, r, r, r, r, r, r, r}
        };
        comfort = 200;

        super.setStructure(structure);
    }
}
