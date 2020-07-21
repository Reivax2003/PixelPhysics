import java.awt.Color;

public class Water extends Pixel {

    public Water(int xpos, int ypos) {
        super("water", xpos, ypos, Color.blue);
    }

    public int[] update(Grid grid) {
        int[] newPosition = new int[] {x, y};

        if (grid.getPixel(x, y - 1).getType().equals("air")) {
            newPosition[0] = x;
            newPosition[1] = y - 1;
        }
        else if (grid.getPixel(x-1, y).getType().equals("air") && Math.random() > 0.5) {
            newPosition[0] = x - 1;
            newPosition[1] = y;
        }
        else if (grid.getPixel(x+1, y).getType().equals("air")) {
            newPosition[0] = x + 1;
            newPosition[1] = y;
        }
        return newPosition;
    }
}