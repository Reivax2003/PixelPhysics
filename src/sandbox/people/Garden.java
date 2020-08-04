package sandbox.people;

import java.awt.*;
import sandbox.*;
import sandbox.pixels.*;

public class Garden extends Blueprint {

    private Pixel[][] structure;
    private int x;
    private int y;

    public Garden(){
        name = "Garden";
        Pixel a = new Air();

        Pixel s = new Soil();  //it seems s is not used in the blueprint
        s.addProperty("walkable", 0);

        Pixel g = new Grass();
        g.addProperty("walkable", 0);

        Pixel b = new Wood(); //bright for plank
        b.setColor(new Color(176, 123, 70));
        b.addProperty("walkable", 0);
        Pixel p = new Wood(); //plank (light wood)
        p.setColor(new Color(163, 113, 57));
        p.addProperty("walkable", 0);
        Pixel d = new Wood(); //dark for plank
        d.setColor(new Color(135, 94, 49));
        d.addProperty("walkable", 0);


        structure = new Pixel[][]{
                {a, a, a, g, g, g, a, g, a, a, g, g, g, g, g, g, a, a, a, g, g, a, a, a},
                {a, g, g, d, d, d, g, d, g, g, d, d, d, d, d, d, g, g, g, d, d, g, g, a},
                {g, d, d, d, d, d, d, d, d, d, d, d, d, d, d, d, d, d, d, d, d, d, d, g},
                {b, b, p, p, p, p, p, p, p, p, p, p, p, p, p, p, p, p, p, p, p, p, d, d},
                {b, b, p, p, p, p, p, p, p, p, p, p, p, p, p, p, p, p, p, p, p, p, d, d}
        };

        super.setStructure(structure);
    }
}
