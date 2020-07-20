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

        for (double y = 0; y < gridHeight; y ++) {
            for (double x = 0; x < gridWidth; x ++) {
                // alternate colors
                if(coloredBlack) {
                    g.setColor(Color.black);
                } else {
                    g.setColor(Color.white);
                }
                coloredBlack = !coloredBlack;

                g.fillRect((int) x * pixelsPerSquare + xOffset, (int) y * pixelsPerSquare + yOffset, pixelsPerSquare, pixelsPerSquare);
            }
            // also alternate color on y change so it is a checkerboard rather than lines
            coloredBlack = !coloredBlack;
        }
        //g.drawString("Hello, World!", 90, 100);
        for (int x = 0; x < PixelSandbox.grid.length; x++){
            for (int y = 0; y < PixelSandbox.grid.length; y++){
                if (PixelSandbox.grid[x][y].type == "sand"){
                    g.setColor(Color.yellow);
                    g.fillRect(x * pixelsPerSquare + xOffset, y * pixelsPerSquare + yOffset, pixelsPerSquare, pixelsPerSquare);
                }
            }
        }
    }
}
