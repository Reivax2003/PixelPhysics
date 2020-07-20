import javax.swing.*;
import java.awt.*;

public class Renderer extends JPanel {

    private final int gridWidth;
    private final int gridHeight;

    public Renderer(int width, int height) {
        this.gridWidth = width;
        this.gridHeight = height;
    }

    @Override
    protected void paintComponent(Graphics g) {

        // set pixels per square to the smallest dimension, so
        int pixelsPerSquare = Math.min(getWidth() / gridWidth, getHeight() / gridHeight);

        boolean coloredBlack = true;

        // get the size of the unused margins (getWidth - used space), then divide it by two to get the center
        int xOffset = (getWidth() - gridWidth * pixelsPerSquare) / 2;
        int yOffset = (getHeight() - gridHeight * pixelsPerSquare) / 2;

        for (double y = 0; y < gridHeight * pixelsPerSquare; y += pixelsPerSquare) {
            for (double x = 0; x < gridWidth * pixelsPerSquare; x += pixelsPerSquare) {
                // alternate colors
                if(coloredBlack) {
                    g.setColor(Color.black);
                } else {
                    g.setColor(Color.white);
                }
                coloredBlack = !coloredBlack;

                g.fillRect((int) x + xOffset, (int) y + yOffset, pixelsPerSquare, pixelsPerSquare);
            }
            // also alternate color on y change so it is a checkerboard rather than lines
            coloredBlack = !coloredBlack;
        }
    }
}
