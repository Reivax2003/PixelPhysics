package sandbox;

import sandbox.people.Person;
import sandbox.people.Blueprint;
import sandbox.pixels.Air;
import sandbox.pixels.Pixel;

import java.awt.event.*;

public class MouseHandler implements MouseMotionListener, MouseListener, MouseWheelListener {

    private final Renderer panel;
    private final Grid grid;
    private final PeopleManager peopleManager;
    private MenuBar menuBar;

    private int lastMouseX = -1;
    private int lastMouseY = -1;

    private int lastGridOffsetX = 0;
    private int lastGridOffsetY = 0;

    private int selectedPerson = -1;
    private Blueprint selectedbuilding = null;

    public MouseHandler(Renderer panel, Grid grid, MenuBar menuBar, PeopleManager peopleManager){
        this.panel = panel;
        this.grid = grid;
        this.menuBar = menuBar;
        this.peopleManager = peopleManager;
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

        clickPoint(x, y, button, (e.getModifiersEx() & MouseEvent.CTRL_DOWN_MASK) != 0);
    }

    @Override
    public void mousePressed(MouseEvent e) {

        // coordinates of mouse drag
        int x = e.getX();
        int y = e.getY();

        clickPoint(x, y, e.getButton(), (e.getModifiersEx() & MouseEvent.CTRL_DOWN_MASK) != 0);
    }

    private void clickPoint(int x, int y, int button, boolean controlHeld) {

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
            if (!controlHeld) {
                switch (button) {
                    case MouseEvent.BUTTON1 -> {
                        int person = getPerson(squareX, squareY);
                        if(person!=-1){
                            selectedPerson = person;
                            peopleManager.getPerson(person).setShowInventory(!peopleManager.getPerson(person).getShowInventory());
                            peopleManager.getPerson(person).setDragged(true);
                            // System.out.println("person"+person+" happiness: "+peopleManager.getPerson(person).getHappiness());
                        }else if (grid.getPixel(squareX, squareY).getBuilding() != null && selectedbuilding == null){
                            selectedbuilding = grid.getPixel(squareX, squareY).getBuilding();
                        }else if (selectedbuilding != null){
                            if (grid.getPixel(squareX, squareY).getBuilding() == null){
                                selectedbuilding.destroy(grid);
                                selectedbuilding.build(grid, squareX, squareY);
                                selectedbuilding = null;
                            }
                        }
                        else{
                            Pixel pixel = menuBar.pixels[menuBar.chosen].duplicate();
                            grid.drawPixel(squareX, squareY, pixel);
                        }
                    }
                    case MouseEvent.BUTTON2 -> {
                        panel.slimeGoalX = squareX;
                        panel.slimeGoalY = squareY;
                    }
                    case MouseEvent.BUTTON3 -> grid.drawPixel(squareX, squareY, new Air());
                }
            }
        } else {  //draw line if dragging over screen
            if (!controlHeld) {
                switch (button) {
                    case MouseEvent.BUTTON1:
                        if (selectedPerson != -1) {
                            peopleManager.getPerson(selectedPerson).setRoot(squareX, squareY);
                        } else {
                            Pixel pixel = menuBar.pixels[menuBar.chosen].duplicate();
                            grid.drawLine(squareX, squareY, lastMouseX, lastMouseY, pixel);
                        }
                        break;
                    case MouseEvent.BUTTON2:
                        panel.slimeGoalX = squareX;
                        panel.slimeGoalY = squareY;
                        break;
                    case MouseEvent.BUTTON3:
                        grid.drawLine(squareX, squareY, lastMouseX, lastMouseY, new Air());
                        break;
                }
            } else {
                int newGridOffsetX = (panel.getGridStartOffsetX() + lastMouseX - squareX - lastGridOffsetX + gridOffsetX);
                int newGridOffsetY = (panel.getGridStartOffsetY() + lastMouseY - squareY - lastGridOffsetY + gridOffsetY);
                if (newGridOffsetX > (grid.getWidth() - panel.getRenderWidth())) {
                    newGridOffsetX = grid.getWidth() - panel.getRenderWidth();
                }
                if (newGridOffsetX < 0) {
                    newGridOffsetX = 0;
                }
                if (newGridOffsetY > (grid.getHeight() - panel.getRenderHeight())) {
                    newGridOffsetY = grid.getHeight() - panel.getRenderHeight();
                }
                if (newGridOffsetY < 0) {
                    newGridOffsetY = 0;
                }
                panel.setGridStartOffsetX(newGridOffsetX);
                panel.setGridStartOffsetY(newGridOffsetY);
            }
        }

        lastMouseX = squareX;
        lastMouseY = squareY;

        lastGridOffsetX = gridOffsetX;
        lastGridOffsetY = gridOffsetY;

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
        lastGridOffsetX = lastGridOffsetY = 0;
        if(selectedPerson!=-1){
            peopleManager.getPerson(selectedPerson).setDragged(false);
        }
        selectedPerson = -1;
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

    private int getPerson(int x, int y){
        //loop through each person and see if its head was clicked
        for (int i = 0; i < peopleManager.getPopulation(); i++) {
            Person person = peopleManager.getPerson(i);
            int offX = x - person.getRoot()[0];
            int offY = y - person.getRoot()[1];
            if((offX*offX)+(offY*offY) < person.getHeadRadius()*person.getHeadRadius()){
                return i;
            }
        }
        return -1;
    }
}
