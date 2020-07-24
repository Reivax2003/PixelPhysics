package sandbox;

import sandbox.pixels.Pixel;

import javax.swing.*;
import java.awt.*;

public class Renderer extends JPanel {

    private final Grid grid;

    public Renderer(Grid grid) {
        this.grid = grid;
    }

    @Override
    protected void paintComponent(Graphics g) {

        int gridWidth = grid.getWidth();
        int gridHeight = grid.getHeight();

        // set sandbox.pixels per square to the smallest dimension, so
        int pixelsPerSquare = Math.min(getWidth() / gridWidth, getHeight() / gridHeight);

        // get the size of the unused margins (getWidth - used space), then divide it by two to get the center
        int xOffset = (getWidth() - gridWidth * pixelsPerSquare) / 2;
        int yOffset = (getHeight() - gridHeight * pixelsPerSquare) / 2;

        for (int x = 0; x < grid.getWidth(); x++) {
            for (int y = 0; y < grid.getHeight(); y++) {
                Pixel pixel = grid.getPixel(x, y);

                Color color = pixel.getColor();
                if (pixel.getStateOrDefault("recovering", 0) != 0) {
                    color = Color.yellow.darker();
                } else if (pixel.getStateOrDefault("conducting", 0) != 0) {
                    color = Color.yellow;
                }
                if (pixel.getStateOrDefault("flower", 0) != 0) {
                    int flower = pixel.getState("flower");

                    Color[] pastels = new Color[] {
                            new Color(255, 107, 255),
                            new Color(255, 85, 85),
                            new Color(255, 165, 48),
                            new Color(225, 255, 54),
                            new Color(54, 175, 255)
                    };

                    //if flower color has not yet been set, set it
                    flower = flower != -1? flower : (int) (Math.random() * pastels.length) + 1;
                    pixel.setState("flower", flower);
                    color = pastels[flower - 1];
                }
                g.setColor(color);
                g.fillRect(x * pixelsPerSquare + xOffset, y * pixelsPerSquare + yOffset, pixelsPerSquare, pixelsPerSquare);
            }
        }
    }
}
