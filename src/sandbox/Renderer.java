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

        boolean coloredBlack = true;

        // get the size of the unused margins (getWidth - used space), then divide it by two to get the center
        int xOffset = (getWidth() - gridWidth * pixelsPerSquare) / 2;
        int yOffset = (getHeight() - gridHeight * pixelsPerSquare) / 2;

        for (int x = 0; x < grid.getWidth(); x++) {
            for (int y = 0; y < grid.getHeight(); y++) {
                Pixel pixel = grid.getPixel(x, y);

                Color color = pixel.getColor();
                if(pixel.getStateOrDefault("recovering", 0) != 0) {
                    color = Color.yellow.darker();
                }
                else if(pixel.getStateOrDefault("conducting", 0) != 0) {
                    color = Color.yellow;
                }
                g.setColor(color);
                g.fillRect(x * pixelsPerSquare + xOffset, y * pixelsPerSquare + yOffset, pixelsPerSquare, pixelsPerSquare);
            }
        }
    }
}
