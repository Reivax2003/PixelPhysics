package sandbox;

import sandbox.pixels.Pixel;

import javax.swing.*;
import java.util.TimerTask;

public class GameLogic extends TimerTask {

    private final int DEFAULT_DENSITY = 10000;

    private final Grid grid;
    private final JPanel panel;

    private boolean reverse;

    public GameLogic(Grid grid, JPanel panel) {
        this.grid = grid;
        this.panel = panel;
    }

    @Override
    public void run() {
        //Start left to right
        reverse = false;
        for (int y = grid.getHeight() - 1; y > -1; y--) {
            for (int x = (reverse ? 1 : 0) * (grid.getWidth() - 1); -1 < x && x < grid.getWidth(); x += reverse ? -1 : 1) {
                Pixel currentPixel = grid.getPixel(x, y);
                int currentX = currentPixel.getX();
                int currentY = currentPixel.getY();

                int density = currentPixel.getPropOrDefault("density", Integer.MAX_VALUE);

                if (currentPixel.hasProperty("support")) {
                    int support = currentPixel.getProperty("support");

                    if (!currentPixel.hasMoved() && currentY < grid.getHeight() - 1 && grid.getPixelDown(currentX, currentY).getPropOrDefault("density", DEFAULT_DENSITY) >= density) {
                        double random = Math.random();

                        // down + left
                        if (currentX > 0 && !grid.getPixel(currentX - 1, currentY + 1).hasMoved() && grid.getPixelLeft(currentX, currentY).getPropOrDefault("density", DEFAULT_DENSITY) < density && grid.getPixel(currentX - 1, currentY + 1).getPropOrDefault("density", DEFAULT_DENSITY) < density && random < 0.5) {
                            grid.swapPositions(currentX, currentY, currentX - 1, currentY + 1);
                        }
                        // down + right
                        else if (currentX < grid.getWidth() - 1 && !grid.getPixel(currentX + 1, currentY + 1).hasMoved() && grid.getPixelRight(currentX, currentY).getPropOrDefault("density", DEFAULT_DENSITY) < density && grid.getPixel(currentX + 1, currentY + 1).getPropOrDefault("density", DEFAULT_DENSITY) < density && random >= 0.5) {
                            // TODO: displacement instead of swapping
                            grid.swapPositions(currentX, currentY, currentX + 1, currentY + 1);
                        }
                    }
                }

                if (currentPixel.hasProperty("gravity")) {
                    int gravity = currentPixel.getProperty("gravity");

                    if (!currentPixel.hasMoved()) {
                        //Binds gravity to grid
                        if (gravity > 0) {
                            gravity = (currentY + gravity < grid.getHeight()) ? gravity : (grid.getHeight() - 1 - currentY);
                        } else {
                            gravity = (currentY + gravity >= 0) ? gravity : -currentY;
                        }
                        //Fall to last air or swap with solid if touching
                        int sign = (gravity > 0) ? 1 : -1;
                        if (gravity * sign > 1) {
                            //Fall to block above if a solid exist more than 1 away
                            for (int gravityCheck = gravity * sign; gravityCheck > 1; gravityCheck--) {
                                if (grid.getPixel(currentX, currentY + gravityCheck * sign).getPropOrDefault("density", DEFAULT_DENSITY) > 0) {
                                    gravity = gravityCheck * sign - sign;
                                }
                            }
                            //Check touching
                            if (grid.getPixel(currentX, currentY + sign).getPropOrDefault("density", DEFAULT_DENSITY) > 0) {
                                gravity = sign;
                            }
                        }

                        if (grid.getPixel(currentX, currentY + gravity).getPropOrDefault("density", DEFAULT_DENSITY) < density && !grid.getPixel(currentX, currentY + gravity).hasMoved()) {
                            grid.swapPositions(currentX, currentY, currentX, currentY + gravity);
                        }
                    }
                }

                if (currentPixel.hasProperty("fluidity")) {
                    int fluidity = currentPixel.getProperty("fluidity");

                    if (Math.random() < fluidity / 100.0 && !currentPixel.hasMoved()) {
                        double random = Math.random();

                        // left
                        if (currentX > 0 && !grid.getPixelLeft(currentX, currentY).hasMoved() && grid.getPixelLeft(currentX, currentY).getPropOrDefault("density", DEFAULT_DENSITY) < density && random < 0.5) {
                            grid.swapPositions(currentX, currentY, currentX - 1, currentY);
                        }
                        // right
                        else if (currentX < grid.getWidth() - 1 && !grid.getPixelRight(currentX, currentY).hasMoved() && grid.getPixelRight(currentX, currentY).getPropOrDefault("density", DEFAULT_DENSITY) < density && random >= 0.5) {
                            grid.swapPositions(currentX, currentY, currentX + 1, currentY);
                        }
                    }
                }
            }
            //Alternate direction of updating
            reverse = !reverse;
        }

        for (int x = 0; x < grid.getWidth(); x++) {
            for (int y = 0; y < grid.getHeight(); y++) {
                grid.getPixel(x, y).setMoved(false);
            }
        }

        panel.repaint();
    }
}
