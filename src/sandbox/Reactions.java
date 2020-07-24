package sandbox;

import sandbox.pixels.*;

//stores each of the reactions where 2 substances combine and give 2 substances as results
//usually use air as the other product when there is 1 product
public class Reactions {

    public Reactions() {
    }

    public Pixel[] getReaction(Pixel a, Pixel b) {
        if (a.getType().equals("sand") && b.getType().equals("water"))
            return new Pixel[]{new WetSand(), new Air()};
        else if (a.getType().equals("fire") && a.getPropOrDefault("strength", 0) == 100 && b.getType().equals("water"))
            return new Pixel[]{new Air(), new Steam()};
        else if (a.getType().equals("wet sand") && b.getType().equals("fire"))
            return new Pixel[]{new Sand(), b};
        else if (a.getType().equals("acid") && Math.random() < a.getPropOrDefault("acidity", 100) / 100.0 && Math.random() < b.getPropOrDefault("solubility", 0) / 100.0)
            return new Pixel[]{a, new Air()};
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