package sandbox;

import sandbox.pixels.Pixel;

import javax.swing.*;
import java.util.TimerTask;

public class GameLogic extends TimerTask {

    private final int DEFAULT_DENSITY = 10000;

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
                Pixel currentPixel = grid.getPixel(x, y);
                int currentX = currentPixel.getX();
                int currentY = currentPixel.getY();

                int density = currentPixel.getPropOrDefault("density", Integer.MAX_VALUE);

                if(currentPixel.hasProperty("support")) {
                    int support = currentPixel.getProperty("support");

                    if(currentY < grid.getHeight() - 1 && grid.getPixelDown(currentX, currentY).getPropOrDefault("density", DEFAULT_DENSITY) >= density) {
                        double random = Math.random();

                        // down + left
                        if(currentX > 0 && grid.getPixel(currentX - 1, currentY + 1).getPropOrDefault("density", DEFAULT_DENSITY) < density && random < 0.5) {
                            grid.swapPositions(currentX, currentY, currentX - 1, currentY + 1);
                        }
                        // down + right
                        else if(currentX < grid.getWidth() - 1 && grid.getPixel(currentX + 1, currentY + 1).getPropOrDefault("density", DEFAULT_DENSITY) < density && random >= 0.5) {
                            // TODO: displacement instead of swapping
                            grid.swapPositions(currentX, currentY, currentX + 1, currentY + 1);
                        }
                    }
                }

                if(currentPixel.hasProperty("gravity")) {
                    int gravity = currentPixel.getProperty("gravity");

                    if(currentY + gravity < grid.getHeight() && currentY + gravity >= 0) {
                        if(grid.getPixel(currentX, currentY + gravity).getPropOrDefault("density", DEFAULT_DENSITY) < density) {
                            grid.swapPositions(currentX, currentY, currentX, currentY + gravity);
                        }
                    }
                }

                if(currentPixel.hasProperty("fluidity")) {
                    int fluidity = currentPixel.getProperty("fluidity");

                    if(Math.random() < fluidity / 100.0) {
                        double random = Math.random();

                        // left
                        if(currentX > 0 && grid.getPixelLeft(currentX, currentY).getPropOrDefault("density", DEFAULT_DENSITY) < density && random < 0.5) {
                            grid.swapPositions(currentX, currentY, currentX - 1, currentY);
                        }
                        // right
                        else if(currentX < grid.getWidth() - 1 && grid.getPixelRight(currentX, currentY).getPropOrDefault("density", DEFAULT_DENSITY) < density && random >= 0.5) {
                            grid.swapPositions(currentX, currentY, currentX + 1, currentY);
                        }
                    }
                }
            }
        }

        panel.repaint();
    }
}
