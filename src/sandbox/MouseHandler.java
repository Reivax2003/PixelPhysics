package sandbox;

import sandbox.pixels.Air;
import sandbox.pixels.Pixel;
import sandbox.pixels.Water;

import javax.swing.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

public class MouseHandler implements MouseMotionListener, MouseListener {

    private final JPanel panel;
    private final Grid grid;
    private final KeyHandler keyHandler;

    private int lastMouseX = -1;
    private int lastMouseY = -1;

    public MouseHandler(JPanel panel, Grid grid, KeyHandler keyHandler) {
        this.panel = panel;
        this.grid = grid;
        this.keyHandler = keyHandler;
    }

    @Override
    public void mouseDragged(MouseEvent e) {

        // coordinates of mouse drag
        int x = e.getX();
        int y = e.getY();

        int button;

        if ((e.getModifiersEx() & MouseEvent.BUTTON1_DOWN_MASK) == MouseEvent.BUTTON1_DOWN_MASK) {
            // left mouse button
            button = MouseEvent.BUTTON1;
        } else if ((e.getModifiersEx() & MouseEvent.BUTTON3_DOWN_MASK) == MouseEvent.BUTTON3_DOWN_MASK) {
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
        if (squareX < 0 || squareX > gridWidth - 1) {
            return;
        }
        if (squareY < 0 || squareY > gridHeight - 1) {
            return;
        }

        //left click to add pixels, right click to remove
        if (lastMouseX == -1) {
            if (button == MouseEvent.BUTTON1) {
                Pixel pixel = keyHandler.pixels[keyHandler.chosen].duplicate();
                if(pixel.getType().equals("electricity") && grid.getPixel(squareX, squareY).hasProperty("conductive")) {
                    grid.getPixel(squareX, squareY).setState("conducting", 1);
                } else {
                    pixel.setX(squareX);
                    pixel.setY(squareY);
                    grid.setPixel(squareX, squareY, pixel);
                }
            } else if (button == MouseEvent.BUTTON3) {
                grid.setPixel(squareX, squareY, new Air(squareX, squareY));
            }
        } else {  //draw line if dragging over screen
            if (button == MouseEvent.BUTTON1) {
                Pixel pixel = keyHandler.pixels[keyHandler.chosen].duplicate();
                grid.drawLine(squareX, squareY, lastMouseX, lastMouseY, pixel);
                pixel.setX(squareX);
                pixel.setY(squareY);
            } else if (button == MouseEvent.BUTTON3) {
                grid.drawLine(squareX, squareY, lastMouseX, lastMouseY, new Air(squareX, squareY));
            }
        }
        lastMouseX = squareX;
        lastMouseY = squareY;

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
        lastMouseX = lastMouseY = -1;
    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }
}
