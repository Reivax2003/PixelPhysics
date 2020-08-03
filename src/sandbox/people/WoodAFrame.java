package sandbox.people;

import java.awt.*;
import sandbox.*;
import sandbox.pixels.*;

public class WoodAFrame extends Blueprint {

    private Pixel[][] structure;
    private int x;
    private int y;

    public WoodAFrame(){
        name = "Wood A-Frame";
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

        /*
                      hh
                     hwww
                     hssw
                    hwbdww
                   hwsbdsww
                   hwpbdpww
                  hwspbdpsww
                 hwsdpbdpbsww
                 hwbdpbdpbdww
                hwsbdssssbdsww
               hwspbswwwwwdpsww
               hwdpswwwwwwwpbww
              hwsdpswwwwwwwpbsww
             hwsbdpswwwwdwwpbdsww
             hwpbdpswwwwwwwpbdpww
            hwspbdpbwwwwwwdpbdpsww
            hsdddddddwwwwdddddddsw
         */
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
