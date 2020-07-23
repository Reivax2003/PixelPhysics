package sandbox;

import sandbox.pixels.*;

public class Reactions {

    public Reactions() {
    }

    public Pixel[] getReaction(Pixel a, Pixel b)
    {
        if(a.getType().equals("sand") && b.getType().equals("water"))
            return new Pixel[] { new WetSand(0, 0), new Air(0, 0) };
        else if(a.getType().equals("fire") && a.getPropOrDefault("strength", 0) == 100 && b.getType().equals("water"))
            return new Pixel[] { new Air(0, 0), new Air(0, 0) };
        else if(a.getType().equals("wet sand") && b.getType().equals("fire"))
            return new Pixel[] { new Sand(0, 0), b };
        else
            return null;
    }

    public Pixel[] getReactionOrDefault(Pixel a, Pixel b, Pixel def1, Pixel def2) {
        Pixel[] result = getReaction(a, b);
        if (result == null)
            return new Pixel[]{def1, def2};
        else
            return result;
    }
}