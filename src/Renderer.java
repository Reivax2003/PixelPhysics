import javax.swing.*;
import java.awt.*;

public class Renderer extends JPanel {
    @Override
    protected void paintComponent(Graphics g) {
        int width = 100;
        int height = 50;

        // set pixels per square to the smallest dimension, so
        int pixelsPerSquare = Math.min(getWidth() / width, getHeight() / height);

        boolean coloredBlack = true;

        // get the size of the unused margins (getWidth - used space), then divide it by two to get the center
        int xOffset = (getWidth() - width * pixelsPerSquare) / 2;
        int yOffset = (getHeight() - height * pixelsPerSquare) / 2;

        for (double y = 0; y < height * pixelsPerSquare; y += pixelsPerSquare) {
            for (double x = 0; x < width * pixelsPerSquare; x += pixelsPerSquare) {
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
