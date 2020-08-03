package sandbox;

import sandbox.pixels.Pixel;
import sandbox.people.Person;

import javax.swing.*;
import java.awt.*;
import java.util.HashMap;
import java.util.Set;
import java.util.Map.Entry;

public class Renderer extends JPanel {

    private final Grid grid;
    private final PeopleManager peopleManager;
    public boolean slimeExists = false;
    public int slimeGoalX;
    public int slimeGoalY;

    public float hOffset = 0;
    public boolean bool = false;

    private boolean paused = false;

    // width and height of the renderer in grid pixels - i.e. how much of the grid to show
    private int renderWidth;
    private int renderHeight;

    // offset from the start of the grid
    private int gridStartOffsetX = 0;
    private int gridStartOffsetY = 0;

    public Renderer(Grid grid, PeopleManager peopleManager, int width, int height) {
        this.grid = grid;
        this.peopleManager = peopleManager;
        this.renderWidth = width;
        this.renderHeight = height;

        // start at bottom left instead of top left
        this.gridStartOffsetY = grid.getHeight() - renderHeight;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        // set sandbox.pixels per square to the smallest dimension, so
        int pixelsPerSquare = Math.min(getWidth() / renderWidth, (getHeight() + 1 /* energy bar */ ) / renderHeight);

        // get the size of the unused margins (getWidth - used space), then divide it by two to get the center
        int xOffset = (getWidth() - renderWidth * pixelsPerSquare) / 2;
        int yOffset = (getHeight() - renderHeight * pixelsPerSquare) / 2;

        for (int x = gridStartOffsetX; x < grid.getWidth() && x < gridStartOffsetX + renderWidth; x++) {
            for (int y = gridStartOffsetY; y < grid.getHeight() && y < gridStartOffsetY + renderHeight; y++) {
                Pixel pixel = grid.getPixel(x, y);

                Color color = pixel.getColor();
                if (pixel.getStateOrDefault("recovering", 0) != 0) {
                    color = Color.yellow.darker();
                } else if (pixel.getStateOrDefault("conducting", 0) != 0) {
                    color = Color.yellow;
                }
                if (pixel.getStateOrDefault("flower", 0) != 0) {
                    int flower = pixel.getState("flower");

                    Color[] pastels = new Color[]{
                            new Color(255, 107, 255),
                            new Color(255, 85, 85),
                            new Color(255, 165, 48),
                            new Color(225, 255, 54),
                            new Color(54, 175, 255)
                    };

                    //if flower color has not yet been set, set it
                    flower = flower != -1 ? flower : (int) (Math.random() * pastels.length) + 1;
                    pixel.setState("flower", flower);
                    color = pastels[flower - 1];
                }

                if (grid.getView() == 1 && !pixel.getType().equals("air")) { // Heat map
                    int temperature = pixel.getPropOrDefault("temperature", 50);
                    color = gradientColor(temperature, 200, Color.blue.darker(), Color.red.brighter());
                }

                g.setColor(bool ? nudgeColor(color) : color);
                g.fillRect((x - gridStartOffsetX) * pixelsPerSquare + xOffset, (y - gridStartOffsetY) * pixelsPerSquare + yOffset, pixelsPerSquare, pixelsPerSquare);
            }
        }

        if (slimeExists) {
            if(slimeGoalX < gridStartOffsetX+renderWidth && slimeGoalX >= gridStartOffsetX && slimeGoalY < gridStartOffsetY + renderHeight && slimeGoalY >= gridStartOffsetY) {
                g.setColor(Color.red);
                g.drawRect((slimeGoalX - gridStartOffsetX) * pixelsPerSquare + xOffset, (slimeGoalY - gridStartOffsetY) * pixelsPerSquare + yOffset, pixelsPerSquare, pixelsPerSquare);
            }
        }

        //energy bar
        g.setColor(Color.yellow);
        g.fillRect(xOffset, yOffset - pixelsPerSquare / 2, Math.min(grid.getEnergy(), grid.getDisplayMaxEnergy()) * pixelsPerSquare / 10, pixelsPerSquare / 2);

        //rendering people
        HashMap<String, Integer> needed = new HashMap<>();
        g.setColor(Color.gray.darker());
        g.setFont(g.getFont().deriveFont(1.5f * pixelsPerSquare));
        for (int i = 0; i < peopleManager.getPopulation(); i++) {
            Person person = peopleManager.getPerson(i);
            //render center
            if (person.getRoot()[0] >= gridStartOffsetX && person.getRoot()[0] < gridStartOffsetX+renderWidth && person.getRoot()[1] >= gridStartOffsetY && person.getRoot()[1] < gridStartOffsetY+renderHeight) {
                g.fillRect((person.getRoot()[0] - gridStartOffsetX) * pixelsPerSquare + xOffset, (person.getRoot()[1] - gridStartOffsetY) * pixelsPerSquare + yOffset, pixelsPerSquare, pixelsPerSquare);
            }
            //render head
            for (int x = (int) -person.getR(); x < person.getR(); x++){
                for (int y = (int) -person.getR(); y < person.getR(); y++){
                    if ((x*x)+(y*y) < person.getR()*person.getR() && x + person.getRoot()[0] >= gridStartOffsetX && x + person.getRoot()[0] < gridStartOffsetX + renderWidth && y + person.getRoot()[1] > gridStartOffsetY && y + person.getRoot()[1] <= gridStartOffsetY + renderHeight){
                        g.fillRect(((x+person.getRoot()[0]) - gridStartOffsetX) * pixelsPerSquare + xOffset, (int) ((y+person.getRoot()[1]-person.getR()+1) - gridStartOffsetY) * pixelsPerSquare + yOffset, pixelsPerSquare, pixelsPerSquare);
                    }
                }
            }

            double xChange = person.getRoot()[0]-person.getFoot1()[0];
            double yChange = person.getRoot()[1]-person.getFoot1()[1];

            //first leg
            double x = person.getFoot1()[0];
            for (int y = person.getFoot1()[1]; y > person.getRoot()[1]; y--) {
                if (yChange != 0 && xChange != 0){
                    x -= (xChange/yChange);
                }
                if (x > gridStartOffsetX && x < gridStartOffsetX + renderWidth && y >= gridStartOffsetY && y < gridStartOffsetY + renderHeight) {
                    g.fillRect(((int) x - gridStartOffsetX) * pixelsPerSquare + xOffset, (y - gridStartOffsetY) * pixelsPerSquare + yOffset, pixelsPerSquare, pixelsPerSquare);
                }
            }

            //second leg
            x = person.getFoot2()[0];
            g.setColor(Color.gray);
            xChange = person.getRoot()[0]-person.getFoot2()[0];
            yChange = person.getRoot()[1]-person.getFoot2()[1];
            for (int y = person.getFoot2()[1]; y > person.getRoot()[1]; y--) {
                if (yChange != 0 && xChange != 0){
                    x -= (xChange/yChange);
                }
                if (x > gridStartOffsetX && x < gridStartOffsetX + renderWidth && y >= gridStartOffsetY && y < gridStartOffsetY + renderHeight) {
                    g.fillRect(((int)x - gridStartOffsetX) * pixelsPerSquare + xOffset, (y - gridStartOffsetY) * pixelsPerSquare + yOffset, pixelsPerSquare, pixelsPerSquare);
                }
            }
            //inventory
            if(person.getShowInv()){
                Set<Entry<String, Integer>> invItems = person.getResources();
                int size = invItems.size();
                g.setColor(new Color(100,100,100,150));
                g.fillRoundRect((person.getRoot()[0] - gridStartOffsetX - 1) * pixelsPerSquare + xOffset, (person.getRoot()[1] - gridStartOffsetY - (int)person.getR() - size * 2 - 4) * pixelsPerSquare + yOffset, 15 * pixelsPerSquare, ( size * 2 + 4 ) * pixelsPerSquare, 5, 5);
                g.setColor(new Color(255,255,255));
               
                //current task
                g.drawString(person.getCurActivity(), (person.getRoot()[0] - gridStartOffsetX) * pixelsPerSquare + xOffset,  (person.getRoot()[1] - gridStartOffsetY - (int)person.getR() -  size * 2 - 2) * pixelsPerSquare + yOffset);
                //items
                int j = 0;
                for (Entry<String,Integer> entry : invItems) {
                    g.drawString(String.format("%s: %d", entry.getKey(), entry.getValue()), (person.getRoot()[0] - gridStartOffsetX) * pixelsPerSquare + xOffset, (person.getRoot()[1] - gridStartOffsetY - (int)person.getR() - size * 2 + j) * pixelsPerSquare + yOffset);
                    j += 2;
                }
            }
            //needed items
            Set<Entry<String, Integer>> invItems = person.getDesires();
                for (Entry<String,Integer> entry : invItems) {
                    needed.put(entry.getKey(), needed.getOrDefault(entry.getKey(), 0) + Math.max(entry.getValue() - person.getResource(entry.getKey()),0));
                }
        }
        //render population stats
        g.setColor(Color.black);
        //average happiness
        g.drawString(Math.round(peopleManager.getAverageHappiness()*100)+"% happiness", xOffset, (int) (pixelsPerSquare * 1.5 + yOffset));
        //needed resources
        g.drawString("needed resources:", xOffset, (int) (3.5 * pixelsPerSquare + yOffset));
        needed.remove("energy");  //don't display energy
        needed.values().remove(0);  //don't display needs that are satisfied
        int k = 5;
        for (Entry<String,Integer> entry : needed.entrySet()) {
            g.drawString(String.format("%s: %d", entry.getKey(), entry.getValue()), xOffset, (int) ((k + 0.5) * pixelsPerSquare + yOffset));
            k += 2;
        }

        g.setColor(new Color(255, 255, 255, 127));
        // render horizontal scrollbar
        if (grid.getWidth() > renderWidth) {
            // fraction of the grid renderable by the renderer
            double widthFraction = renderWidth / (double) grid.getWidth();

            // width necessary to cover fraction of the screen
            double width = (renderWidth * pixelsPerSquare) * widthFraction;
            g.fillRect((int) (xOffset + (gridStartOffsetX * widthFraction) * pixelsPerSquare), yOffset + renderHeight * pixelsPerSquare - pixelsPerSquare, (int) width, pixelsPerSquare);
        }

        // render vertical scrollbar
        if (grid.getHeight() > renderHeight) {
            // fraction of the grid renderable by the renderer
            double heightFraction = renderHeight / (double) grid.getHeight();

            // height necessary to cover fraction of the screen
            double height = (renderHeight * pixelsPerSquare) * heightFraction;
            g.fillRect(xOffset, (int) (yOffset + (gridStartOffsetY * heightFraction) * pixelsPerSquare), pixelsPerSquare, (int) height);
        }

        g.setColor(new Color(255, 255, 255, 200));
        if (paused) {
            // render pause symbol
            g.fillRect(xOffset + pixelsPerSquare * renderWidth - pixelsPerSquare * 2, yOffset + pixelsPerSquare * renderHeight - pixelsPerSquare * 4, pixelsPerSquare, pixelsPerSquare * 3);
            g.fillRect(xOffset + pixelsPerSquare * renderWidth - pixelsPerSquare * 4, yOffset + pixelsPerSquare * renderHeight - pixelsPerSquare * 4, pixelsPerSquare, pixelsPerSquare * 3);
        }
    }

    public void setGridStartOffsetX(int offset) {
        this.gridStartOffsetX = offset;
    }

    public void setGridStartOffsetY(int offset) {
        this.gridStartOffsetY = offset;
    }

    public int getGridStartOffsetX() {
        return gridStartOffsetX;
    }

    public int getGridStartOffsetY() {
        return gridStartOffsetY;
    }

    public int getRenderWidth() {
        return renderWidth;
    }

    public int getRenderHeight() {
        return renderHeight;
    }

    public void setPaused(boolean paused) {
        this.paused = paused;
    }

    private Color nudgeColor(Color color) {
        float[] hsb = Color.RGBtoHSB(color.getRed(), color.getGreen(), color.getBlue(), null);
        float h = hsb[0];
        h = (h + hOffset) % 1.0f;
        return Color.getHSBColor(h, hsb[1], hsb[2]);
    }

    private Color gradientColor(int level, int max, Color low, Color high) {
        double portionHigh = level / (double) max;

        double red = high.getRed() * portionHigh + low.getRed() * (1 - portionHigh);
        double green = high.getGreen() * portionHigh + low.getGreen() * (1 - portionHigh);
        double blue = high.getBlue() * portionHigh + low.getBlue() * (1 - portionHigh);

        return new Color((int) red, (int) green, (int) blue);
    }
}
