package sandbox;

import sandbox.pixels.Pixel;

import javax.swing.*;
import java.util.TimerTask;

public class GameLogic extends TimerTask {
    private final Grid grid;
    private final JPanel panel;

    public GameLogic(Grid grid, JPanel panel) {
        this.grid = grid;
        this.panel = panel;
    }

    @Override
    public void run() {
        for (int y = grid.getHeight() - 1; y > -1; y--) {
            for (int x = 0; x < grid.getWidth(); x++) {
                Pixel curPixel = grid.getPixel(x, y);

                int[] newPos = curPixel.update(grid);
                Pixel newPixel = grid.getPixel(newPos[0], newPos[1]);

                newPixel.setX(x);
                newPixel.setY(y);
                grid.setPixel(x, y, newPixel); //If no movement it gets set to itself twice

                curPixel.setX(newPos[0]);
                curPixel.setY(newPos[1]);
                grid.setPixel(newPos[0], newPos[1], curPixel);
            }
        }

        panel.repaint();
    }
}
