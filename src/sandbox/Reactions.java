package sandbox;

import java.util.HashMap;

import sandbox.pixels.*;

public class Reactions {
    private HashMap<String, HashMap<String, Pixel>> reactions = new HashMap<String, HashMap<String, Pixel>>();

    public Reactions()
    {
        reactions.put("sand", new HashMap<String, Pixel>(){{
            put("water", new WetSand(0, 0));
        }});
    }

    public Pixel getReaction(String a, String b)
    {
        return reactions.get(a).get(b);
    }

    public Pixel getReactionOrDefault(String a, String b, Pixel def)
    {
        HashMap<String, Pixel> h = reactions.getOrDefault(a, null);
        if(h == null)
            return def;
        else
            return h.getOrDefault(b, null);
    }
}