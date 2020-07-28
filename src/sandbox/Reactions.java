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
        else if (a.getType().equals("wet sand") && (b.getType().equals("fire") || b.getPropOrDefault("temperature", 50) > 110))
            return new Pixel[]{new Sand(), b};
        else if (a.getType().equals("acid") && Math.random() < a.getPropOrDefault("acidity", 100) / 100.0 && Math.random() < b.getPropOrDefault("solubility", 0) / 100.0)
            return new Pixel[]{a, new Air()};
        else if (a.getType().equals("electricity") && b.hasProperty("flammable"))
            return new Pixel[]{new Fire(), b};

        // else if (a.getType().equals("water") && b.getPropOrDefault("temperature", 50) > 60)
        //     return new Pixel[]{new Steam(), b.changeProperty("temperature", b.getProperty("temperature")-50)};
        // else if (a.hasProperty("temperature") && b.hasProperty("temperature"))
        // {
        //     int avgTemp = (a.getProperty("temperature") + b.getProperty("temperature"))/2;
        //     int quarterChange = (a.getProperty("temperature") - avgTemp) / 2;
        //     return new Pixel[]{a.changeProperty("temperature", a.getProperty("temperature") - quarterChange), b.changeProperty("temperature", b.getProperty("temperature") + quarterChange)};
        // }
        else
            return null;
    }

    public Pixel[] getTempChange(Pixel a, Pixel b) {
      if (b.hasProperty("temperature"))
      {
          int avgTemp = (a.getProperty("temperature") + b.getProperty("temperature"))/2;
          //Reduce rate and apply conductivity, heat transfer is currently disabled for air, divides by 10000 to undo the 100% of multiplying by 100 twice
          int quarterChange = ((a.getProperty("temperature") - avgTemp) / 2) * a.getProperty("heatConduct") * b.getProperty("heatConduct") / 10000;
          return new Pixel[]{a.changeProperty("temperature", a.getProperty("temperature") - quarterChange), b.changeProperty("temperature", b.getProperty("temperature") + quarterChange)};
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
