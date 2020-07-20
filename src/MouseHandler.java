import javax.swing.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;

public class MouseHandler implements MouseMotionListener {

    private final JPanel panel;
    private final int gridWidth;
    private final int gridHeight;

    public MouseHandler(JPanel panel, int gridWidth, int gridHeight) {
        this.panel = panel;
        this.gridWidth = gridWidth;
        this.gridHeight = gridHeight;
    }

    @Override
    public void mouseDragged(MouseEvent e) {

        int x = e.getX();
        int y = e.getY();

        int width = panel.getWidth();
        int height = panel.getHeight();

        int pixelsPerSquare = Math.min(width / gridWidth, height / gridHeight);

        int xOffset = (width - gridWidth * pixelsPerSquare) / 2;
        int yOffset = (height - gridHeight * pixelsPerSquare) / 2;

        int adjustedX = x - xOffset;
        int adjustedY = y - yOffset;

        int squareX = adjustedX / pixelsPerSquare;
        int squareY = adjustedY / pixelsPerSquare;

        if(squareX < 0 || squareX > gridWidth - 1) {
            squareX = -1;
        }

        if(squareY < 0 || squareY > gridHeight - 1) {
            squareY = -1;
        }

        System.out.println(squareX + ", " + squareY);
    }

    @Override
    public void mouseMoved(MouseEvent e) {

    }
}
