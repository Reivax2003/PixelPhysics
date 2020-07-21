package sandbox;

import sandbox.pixels.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

public class MouseHandler implements MouseMotionListener, MouseListener {

    private final JPanel panel;
    private final Grid grid;

    public MouseHandler(JPanel panel, Grid grid) {
        this.panel = panel;
        this.grid = grid;
    }

    @Override
    public void mouseDragged(MouseEvent e) {

        // coordinates of mouse drag
        int x = e.getX();
        int y = e.getY();

        int button;

        if((e.getModifiersEx() & MouseEvent.BUTTON1_DOWN_MASK) == MouseEvent.BUTTON1_DOWN_MASK) {
            // left mouse button
            button = MouseEvent.BUTTON1;
        } else if((e.getModifiersEx() & MouseEvent.BUTTON3_DOWN_MASK) == MouseEvent.BUTTON3_DOWN_MASK) {
            // right mouse button
            button = MouseEvent.BUTTON3;
        } else {
            // other (i.e. scroll wheel click)
            button = MouseEvent.BUTTON2;
        }

        clickPoint(x, y, button);
    }

    @Override
    public void mousePressed(MouseEvent e) {

        // coordinates of mouse drag
        int x = e.getX();
        int y = e.getY();

        clickPoint(x, y, e.getButton());
    }

    // FIXME: misses pixels if mouse moves too fast
    private void clickPoint(int x, int y, int button) {

        // size of panel
        int width = panel.getWidth();
        int height = panel.getHeight();

        // width and height of grid in pixels
        int gridWidth = grid.getWidth();
        int gridHeight = grid.getHeight();

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

        // out of bounds
        if(squareX < 0 || squareX > gridWidth - 1) {
            return;
        }
        if(squareY < 0 || squareY > gridHeight - 1) {
            return;
        }

        if(button == MouseEvent.BUTTON1) {
            grid.setPixel(squareX, squareY, new Sand(squareX, squareY));
        } else if(button == MouseEvent.BUTTON2) {
            grid.setPixel(squareX, squareY, new Water(squareX, squareY));
        } else if(button == MouseEvent.BUTTON3) {
            grid.setPixel(squareX, squareY, new Air(squareX, squareY));
        }

        // repaint panel so it displays
        panel.repaint();
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
