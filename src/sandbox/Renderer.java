package sandbox;

import sandbox.pixels.Pixel;

import javax.swing.*;
import java.awt.*;

public class Renderer extends JPanel {

    private final Grid grid;
    private final MenuBar menuBar;
    private final PeopleManager peopleManager;
    public boolean slimeExists = false;
    public int slimeGoalX;
    public int slimeGoalY;

    public float hOffset = 0;
    public boolean bool = false;

    private boolean paused = false;

    // width and height of the renderer in grid pixels - i.e. how much of the grid to show
    private int renderWidth;
    private int renderHeight;

    // offset from the start of the grid
    private int gridStartOffsetX = 0;
    private int gridStartOffsetY = 0;

    public Renderer(Grid grid, MenuBar menuBar, PeopleManager peopleManager, int width, int height) {
        this.grid = grid;
        this.menuBar = menuBar;
        this.peopleManager = peopleManager;
        this.renderWidth = width;
        this.renderHeight = height;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        // set sandbox.pixels per square to the smallest dimension, so
        int pixelsPerSquare = Math.min(getWidth() / renderWidth, (getHeight() + 1 /* energy bar */ ) / renderHeight);

        // get the size of the unused margins (getWidth - used space), then divide it by two to get the center
        int xOffset = (getWidth() - renderWidth * pixelsPerSquare) / 2;
        int yOffset = (getHeight() - renderHeight * pixelsPerSquare) / 2;

        for (int x = gridStartOffsetX; x < grid.getWidth() && x < gridStartOffsetX + renderWidth; x++) {
            for (int y = gridStartOffsetY; y < grid.getHeight() && y < gridStartOffsetY + renderHeight; y++) {
                Pixel pixel = grid.getPixel(x, y);

                Color color = pixel.getColor();
                if (pixel.getStateOrDefault("recovering", 0) != 0) {
                    color = Color.yellow.darker();
                } else if (pixel.getStateOrDefault("conducting", 0) != 0) {
                    color = Color.yellow;
                }
                if (pixel.getStateOrDefault("flower", 0) != 0) {
                    int flower = pixel.getState("flower");

                    Color[] pastels = new Color[]{
                            new Color(255, 107, 255),
                            new Color(255, 85, 85),
                            new Color(255, 165, 48),
                            new Color(225, 255, 54),
                            new Color(54, 175, 255)
                    };

                    //if flower color has not yet been set, set it
                    flower = flower != -1 ? flower : (int) (Math.random() * pastels.length) + 1;
                    pixel.setState("flower", flower);
                    color = pastels[flower - 1];
                }

                if (grid.getView() == 1 && !pixel.getType().equals("air")) { // Heat map
                    int temperature = pixel.getPropOrDefault("temperature", 50);
                    color = gradientColor(temperature, 200, Color.blue.darker(), Color.red.brighter());
                }

                g.setColor(bool ? nudgeColor(color) : color);
                g.fillRect((x - gridStartOffsetX) * pixelsPerSquare + xOffset, (y - gridStartOffsetY) * pixelsPerSquare + yOffset, pixelsPerSquare, pixelsPerSquare);
            }
        }

        if (slimeExists) {
            g.setColor(Color.red);
            g.drawRect((slimeGoalX - gridStartOffsetX) * pixelsPerSquare + xOffset, (slimeGoalY - gridStartOffsetY) * pixelsPerSquare + yOffset, pixelsPerSquare, pixelsPerSquare);
            //test
            g.drawLine(0, 0, 50, 50);
        }

        //energy bar
        if(menuBar.infiniteEnergy) {
            grid.energy = 1000000;
        }
        g.setColor(Color.yellow);
        g.fillRect(xOffset, yOffset - pixelsPerSquare / 2, Math.min(grid.energy, grid.MAX_ENERGY) * pixelsPerSquare / 10, pixelsPerSquare / 2);

        g.setColor(new Color(255, 255, 255, 127));
        // render horizontal scrollbar
        if (grid.getWidth() > renderWidth) {
            // fraction of the grid renderable by the renderer
            double widthFraction = renderWidth / (double) grid.getWidth();

            // width necessary to cover fraction of the screen
            double width = (renderWidth * pixelsPerSquare) * widthFraction;
            g.fillRect((int) (xOffset + (gridStartOffsetX * widthFraction) * pixelsPerSquare), yOffset + renderHeight * pixelsPerSquare - pixelsPerSquare, (int) width, pixelsPerSquare);
        }

        // render vertical scrollbar
        if (grid.getHeight() > renderHeight) {
            // fraction of the grid renderable by the renderer
            double heightFraction = renderHeight / (double) grid.getHeight();

            // height necessary to cover fraction of the screen
            double height = (renderHeight * pixelsPerSquare) * heightFraction;
            g.fillRect(xOffset, (int) (yOffset + (gridStartOffsetY * heightFraction) * pixelsPerSquare), pixelsPerSquare, (int) height);
        }
    }

    public void setGridStartOffsetX(int offset) {
        this.gridStartOffsetX = offset;
    }

    public void setGridStartOffsetY(int offset) {
        this.gridStartOffsetY = offset;
    }

    public int getGridStartOffsetX() {
        return gridStartOffsetX;
    }

    public int getGridStartOffsetY() {
        return gridStartOffsetY;
    }

    public int getRenderWidth() {
        return renderWidth;
    }

    public int getRenderHeight() {
        return renderHeight;
    }

    public void setPaused(boolean paused) {
        this.paused = paused;
    }

    private Color nudgeColor(Color color) {
        float[] hsb = Color.RGBtoHSB(color.getRed(), color.getGreen(), color.getBlue(), null);
        float h = hsb[0];
        h = (h + hOffset) % 1.0f;
        return Color.getHSBColor(h, hsb[1], hsb[2]);
    }

    private Color gradientColor(int level, int max, Color low, Color high) {
        double portionHigh = level / (double) max;

        double red = high.getRed() * portionHigh + low.getRed() * (1 - portionHigh);
        double green = high.getGreen() * portionHigh + low.getGreen() * (1 - portionHigh);
        double blue = high.getBlue() * portionHigh + low.getBlue() * (1 - portionHigh);

        return new Color((int) red, (int) green, (int) blue);
    }
}
