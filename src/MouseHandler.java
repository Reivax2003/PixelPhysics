import javax.swing.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

public class MouseHandler implements MouseMotionListener, MouseListener {

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

        // coordinates of mouse drag
        int x = e.getX();
        int y = e.getY();

        clickPoint(x, y);
    }

    @Override
    public void mousePressed(MouseEvent e) {

        // coordinates of mouse drag
        int x = e.getX();
        int y = e.getY();

        clickPoint(x, y);
    }

    private void clickPoint(int x, int y) {

        // size of panel
        int width = panel.getWidth();
        int height = panel.getHeight();

        // units per square
        int pixelsPerSquare = Math.min(width / gridWidth, height / gridHeight);

        // offset of grid from top left corner
        int xOffset = (width - gridWidth * pixelsPerSquare) / 2;
        int yOffset = (height - gridHeight * pixelsPerSquare) / 2;

        // coordinates of click with offset removed
        int adjustedX = x - xOffset;
        int adjustedY = y - yOffset;

        // coordinates of the square that was clicked
        int squareX = adjustedX / pixelsPerSquare;
        int squareY = adjustedY / pixelsPerSquare;

        // -1 means "out of bounds"
        if(squareX < 0 || squareX > gridWidth - 1) {
            squareX = -1;
        }
        if(squareY < 0 || squareY > gridHeight - 1) {
            squareY = -1;
        }

        // do something here
        System.out.println(squareX + ", " + squareY);
    }

    @Override
    public void mouseMoved(MouseEvent e) {

    }

    @Override
    public void mouseClicked(MouseEvent e) {

    }

    @Override
    public void mouseReleased(MouseEvent e) {

    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }
}
