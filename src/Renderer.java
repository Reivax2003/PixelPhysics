import javax.swing.*;
import java.awt.*;

public class Renderer extends JPanel {
    @Override
    protected void paintComponent(Graphics g) {
        int width = 20;
        int height = 20;

        int pixelsPerSquare = Math.min(getWidth() / width, getHeight() / height);

        boolean coloredBlack = true;

        for (double y = 0; y < height * pixelsPerSquare; y += pixelsPerSquare) {
            for (double x = 0; x < width * pixelsPerSquare; x += pixelsPerSquare) {
                if(coloredBlack) {
                    g.setColor(Color.black);
                } else {
                    g.setColor(Color.white);
                }
                coloredBlack = !coloredBlack;
                g.fillRect((int) x, (int) y, pixelsPerSquare, pixelsPerSquare);
            }
            coloredBlack = !coloredBlack;
        }
    }
}
