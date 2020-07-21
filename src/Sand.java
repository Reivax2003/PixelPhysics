import java.awt.*;

public class Sand extends Pixel {

    public Sand(int xpos, int ypos) {
        super("sand", xpos, ypos, Color.yellow);
    }

    public int[] update(Grid grid) {
        int[] newPosition = new int[] {x, y};

        if (grid.getPixel(x, y + 1).getType().equals("air")) {
            newPosition[0] = x;
            newPosition[1] = y + 1;
            this.x = newPosition[0];
            this.y = newPosition[1];
        }
        return newPosition;
    }
}
