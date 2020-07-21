import java.awt.*;

public class Granular {

    public int[] update(Grid grid) {
        int[] newPosition = new int[] {x, y};

        if (y < grid.getHeight()-1 && grid.getPixel(x, y + 1).getType().equals("air")) {
            newPosition[0] = x;
            newPosition[1] = y + 1;
        }
        else if (x > 0 && y < grid.getHeight()-1 && grid.getPixel(x-1, y + 1).getType().equals("air")) {
            newPosition[0] = x - 1;
            newPosition[1] = y + 1;
        }
        else if (x < grid.getWidth()-1 && y < grid.getHeight()-1 && grid.getPixel(x+1, y + 1).getType().equals("air")) {
            newPosition[0] = x + 1;
            newPosition[1] = y + 1;
        }
        return newPosition;
    }
}
