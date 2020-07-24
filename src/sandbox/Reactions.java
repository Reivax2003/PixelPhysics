package sandbox;

import sandbox.pixels.*;

//stores each of the reactions where 2 substances combine and give 2 substances as results
//usually use air as the other product when there is 1 product
public class Reactions {

    public Reactions() {
    }

    public Pixel[] getReaction(Pixel a, Pixel b) {
        if (a.getType().equals("sand") && b.getType().equals("water"))
            return new Pixel[]{new WetSand(0, 0), new Air(0, 0)};
        else if (a.getType().equals("fire") && a.getPropOrDefault("strength", 0) == 100 && b.getType().equals("water"))
            return new Pixel[]{new Air(0, 0), new Steam(0, 0)};
        else if (a.getType().equals("wet sand") && b.getType().equals("fire"))
            return new Pixel[]{new Sand(0, 0), b};
        else if (a.getType().equals("acid") && Math.random() < a.getPropOrDefault("acidity", 100) / 100.0 && Math.random() < b.getPropOrDefault("solubility", 0) / 100.0)
            return new Pixel[]{a, new Air(0, 0)};
        else if (a.getType().equals("water") && b.getPropOrDefault("temperature", 50) > 60)
            return new Pixel[]{new Steam(0, 0), b.changeProperty("temperature", b.getProperty("temperature")-50)};
        else if (a.hasProperty("temperature") && b.hasProperty("temperature"))
        { 
            int avgTemp = (a.getProperty("temperature") + b.getProperty("temperature"))/2;
            return new Pixel[]{a.changeProperty("temperature", avgTemp), b.changeProperty("temperature", avgTemp)};
        }
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