package sandbox.people;

import java.awt.*;
import sandbox.*;
import sandbox.pixels.*;

public class FirePit extends Blueprint {

    private Pixel[][] structure;
    private int x;
    private int y;

    public FirePit(){
        name = "Fire Pit";
        Pixel a = new Air();
        Pixel f = new Fire();
        f.changeProperty("spreads", 0);

        Pixel w = new Wood(); //wood (dark)
        w.setColor(new Color(113, 63, 5));
        w.changeProperty("fuel", Integer.MAX_VALUE);
        w.changeProperty("ash", 0);
        w.changeProperty("charcoal", 0);
        w.changeProperty("burnspeed", 0);
        Pixel s = new Wood(); //shadow for wood
        s.setColor(new Color(84, 48, 8));
        s.changeProperty("fuel", Integer.MAX_VALUE);
        s.changeProperty("ash", 0);
        s.changeProperty("charcoal", 0);
        s.changeProperty("burnspeed", 0);

        Pixel l = new Stone(); //light gray
        l.setColor(new Color(138, 138, 138));
        Pixel g = new Stone(); //gray
        g.setColor(new Color(122, 122, 122));
        Pixel r = new Stone(); //dark gray
        r.setColor(new Color(107, 107, 107));

        structure = new Pixel[][]{
                {a, a, a, f, f, f, a, a, a, a},
                {a, a, f, w, w, s, f, f, a, a},
                {a, f, w, f, s, s, w, s, f, a},
                {a, l, r, s, l, l, r, s, l, a},
                {l, g, g, l, g, g, g, r, g, r},
                {r, r, r, r, r, r, r, r, r, r}

        };

        super.setStructure(structure);
    }
}
