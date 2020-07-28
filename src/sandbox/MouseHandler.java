package sandbox;

import sandbox.pixels.Air;
import sandbox.pixels.Pixel;

import java.awt.event.*;

public class MouseHandler implements MouseMotionListener, MouseListener, MouseWheelListener {

    private final Renderer panel;
    private final Grid grid;
    private MenuBar menuBar;

    private int lastMouseX = -1;
    private int lastMouseY = -1;

    public MouseHandler(Renderer panel, Grid grid, MenuBar menuBar) {
        this.panel = panel;
        this.grid = grid;
        this.menuBar = menuBar;
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
        int gridWidth = panel.getRenderWidth();
        int gridHeight = panel.getRenderHeight();

        // units per square
        int pixelsPerSquare = Math.min(width / gridWidth, height / gridHeight);

        // offset of grid from top left corner
        int xOffset = (width - gridWidth * pixelsPerSquare) / 2;
        int yOffset = (height - gridHeight * pixelsPerSquare) / 2;

        // coordinates of click with offset removed
        int adjustedX = x - xOffset;
        int adjustedY = y - yOffset;

        // offset of grid scroll position
        int gridOffsetX = panel.getGridStartOffsetX();
        int gridOffsetY = panel.getGridStartOffsetY();

        // coordinates of the square that was clicked
        int squareX = adjustedX / pixelsPerSquare + gridOffsetX;
        int squareY = adjustedY / pixelsPerSquare + gridOffsetY;

        // out of bounds
        if (squareX < 0 || squareX > grid.getWidth() - 1) {
            return;
        }
        if (squareY < 0 || squareY > grid.getHeight() - 1) {
            return;
        }
        
        //left click to add pixels, right click to remove
        if (lastMouseX == -1) {
            if (button == MouseEvent.BUTTON1) {
                Pixel pixel = menuBar.pixels[menuBar.chosen].duplicate();
                panel.energy = grid.drawPixel(squareX, squareY, pixel, panel.energy);
            } else if (button == MouseEvent.BUTTON2) {
                panel.slimeGoalX = squareX;
                panel.slimeGoalY = squareY;
            } else if (button == MouseEvent.BUTTON3) {
                panel.energy = grid.drawPixel(squareX, squareY, new Air(), panel.energy);
            }
        } else {  //draw line if dragging over screen
            if (button == MouseEvent.BUTTON1) {
                Pixel pixel = menuBar.pixels[menuBar.chosen].duplicate();
                panel.energy = grid.drawLine(squareX, squareY, lastMouseX, lastMouseY, pixel, panel.energy);
            } else if (button == MouseEvent.BUTTON2) {
                panel.slimeGoalX = squareX;
                panel.slimeGoalY = squareY;
            } else if (button == MouseEvent.BUTTON3) {
                panel.energy = grid.drawLine(squareX, squareY, lastMouseX, lastMouseY, new Air(), panel.energy);
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

    @Override
    public void mouseWheelMoved(MouseWheelEvent e) {
        int rotation = e.getWheelRotation() * 5;
        int gridOffsetX = panel.getGridStartOffsetX() + rotation;
        if (gridOffsetX < 0) {
            gridOffsetX = 0;
        }
        if (gridOffsetX > (grid.getWidth() - panel.getRenderWidth())) {
            gridOffsetX = grid.getWidth() - panel.getRenderWidth();
        }
        panel.setGridStartOffsetX(gridOffsetX);
        panel.repaint();
    }
}
