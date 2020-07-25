package sandbox;

import sandbox.pixels.*;

import javax.lang.model.util.ElementScanner6;
import javax.swing.*;
import java.awt.*;
import java.util.Random;
import java.util.TimerTask;
import java.util.HashMap;
import java.util.ArrayList;
import java.util.Arrays;

public class GameLogic extends TimerTask {

    public static final int DEFAULT_DENSITY = 10000;

    private final Grid grid;
    private final JPanel panel;
    Reactions reactions = new Reactions();
    private boolean isPaused = false;
    private int steps = 0;
    private ArrayList<ArrayList<Integer>> slimeEdges = new ArrayList<ArrayList<Integer>>();
    private ArrayList<ArrayList<Integer>> slimeEdgesEmpty = new ArrayList<ArrayList<Integer>>();
    private boolean slimeExists = false;
    int slimeGoalX;
    int slimeGoalY;
    int slimeSupports = 0;
    // int frameNum = 0;

    public GameLogic(Grid grid, JPanel panel) {
        this.grid = grid;
        this.panel = panel;

        slimeGoalX = (int) (Math.random()*grid.getWidth());
        slimeGoalY = (int) (Math.random()*grid.getHeight());
    }

    public void setPaused(boolean paused) {
        isPaused = paused;
    }

    public void setSteps(int steps) {
        this.steps = steps;
    }

    @Override
    public void run() {
        if (isPaused && !(steps > 0)) {
            return;
        }

        // System.out.println("next frame "+frameNum++);

        //Start left to right
        boolean reverse = false;
        slimeEdges.clear();
        if (Math.random() < 0.01){
            slimeGoalX = (int) (Math.random()*grid.getWidth());
            slimeGoalY = (int) (Math.random()*grid.getHeight());
        }
        for (int y = grid.getHeight() - 1; y > -1; y--) {
            for (int x = (reverse ? 1 : 0) * (grid.getWidth() - 1); -1 < x && x < grid.getWidth(); x += reverse ? -1 : 1) {
                Pixel currentPixel = grid.getPixel(x, y);
                int pixelX = x;
                int pixelY = y;

                //check for any reactions with neighbor pixels
                if (pixelX > 0) {//left
                    Pixel[] products = reactions.getReaction(currentPixel, grid.getPixelLeft(pixelX, pixelY));
                    if (products != null) {
                        currentPixel = products[0];
                        grid.setPixel(pixelX, pixelY, currentPixel);
                        grid.setPixel(pixelX - 1, pixelY, products[1]);
                    }
                }
                if (pixelX < grid.getWidth() - 1) {//right
                    Pixel[] products = reactions.getReaction(currentPixel, grid.getPixelRight(pixelX, pixelY));
                    if (products != null) {
                        currentPixel = products[0];
                        grid.setPixel(pixelX, pixelY, currentPixel);
                        grid.setPixel(pixelX + 1, pixelY, products[1]);
                    }
                }
                if (pixelY > 0) {//up
                    Pixel[] products = reactions.getReaction(currentPixel, grid.getPixelUp(pixelX, pixelY));
                    if (products != null) {
                        currentPixel = products[0];
                        grid.setPixel(pixelX, pixelY, currentPixel);
                        grid.setPixel(pixelX, pixelY - 1, products[1]);
                    }
                }
                if (pixelY < grid.getHeight() - 1) {//down
                    Pixel[] products = reactions.getReaction(currentPixel, grid.getPixelDown(pixelX, pixelY));
                    if (products != null) {
                        currentPixel = products[0];
                        grid.setPixel(pixelX, pixelY, currentPixel);
                        grid.setPixel(pixelX, pixelY + 1, products[1]);
                    }
                }
                
                if (currentPixel.hasProperty("temperature")) {
                    //Temp Change
                    if (pixelX > 0) {//left
                        Pixel[] products = reactions.getTempChange(currentPixel, grid.getPixelLeft(pixelX, pixelY));
                        if (products != null) {
                            currentPixel = products[0];
                            grid.setPixel(pixelX, pixelY, currentPixel);
                            grid.setPixel(pixelX - 1, pixelY, products[1]);
                        }
                    }
                    if (pixelX < grid.getWidth() - 1) {//right
                        Pixel[] products = reactions.getTempChange(currentPixel, grid.getPixelRight(pixelX, pixelY));
                        if (products != null) {
                            currentPixel = products[0];
                            grid.setPixel(pixelX, pixelY, currentPixel);
                            grid.setPixel(pixelX + 1, pixelY, products[1]);
                        }
                    }
                    if (pixelY > 0) {//up
                        Pixel[] products = reactions.getTempChange(currentPixel, grid.getPixelUp(pixelX, pixelY));
                        if (products != null) {
                            currentPixel = products[0];
                            grid.setPixel(pixelX, pixelY, currentPixel);
                            grid.setPixel(pixelX, pixelY - 1, products[1]);
                        }
                    }
                    if (pixelY < grid.getHeight() - 1) {//down
                        Pixel[] products = reactions.getTempChange(currentPixel, grid.getPixelDown(pixelX, pixelY));
                        if (products != null) {
                            currentPixel = products[0];
                            grid.setPixel(pixelX, pixelY, currentPixel);
                            grid.setPixel(pixelX, pixelY + 1, products[1]);
                        }
                    }
                }

                int density = currentPixel.getPropOrDefault("density", Integer.MAX_VALUE);

                //transfer electricity pixel into conductor
                if (currentPixel.getType().equals("electricity")) {
                    if (pixelY < grid.getHeight() - 1 && grid.getPixelDown(pixelX, pixelY).hasProperty("conductive")) {
                        currentPixel = new Air();
                        grid.setPixel(pixelX, pixelY, currentPixel);
                        grid.getPixelDown(pixelX, pixelY).setState("conducting", 1);
                    }
                }

                //next state
                if (currentPixel.hasProperty("conductive")) {
                    boolean wasRecovering = false;
                    if (currentPixel.getStateOrDefault("recovering", 0) != 0) {
                        currentPixel.setState("recovering", 0);
                        wasRecovering = true;
                    }

                    if (currentPixel.getStateOrDefault("conducting", 0) != 0) {
                        currentPixel.setState("recovering", 1);
                    }

                    if (!wasRecovering) {
                        int surroundingElectricity = 0;

                        for (int gridX = -1; gridX <= 1; gridX++) {
                            for (int gridY = -1; gridY <= 1; gridY++) {
                                try {
                                    surroundingElectricity = surroundingElectricity + grid.getPixel(gridX + pixelX, gridY + pixelY).getStateOrDefault("conducting", 0);
                                } catch (Exception ignored) {
                                }
                            }
                        }
                        //transfer electricity
                        if (surroundingElectricity == 1 || surroundingElectricity == 2) {
                            currentPixel.setState("willBeConducting", 1);
                        }
                    }
                }

                //check for diagonal falling
                if (currentPixel.getPropOrDefault("support", 0) != 0) {
                    int steepness = currentPixel.getPropOrDefault("steepness", 1);

                    if (!currentPixel.hasMoved() && pixelY < grid.getHeight() - steepness && grid.getPixelDown(pixelX, pixelY).getPropOrDefault("density", DEFAULT_DENSITY) >= density) {
                        double random = Math.random();

                        // down + left
                        if (pixelX > 0 && !grid.getPixel(pixelX - 1, pixelY + steepness).hasMoved() && grid.getPixelLeft(pixelX, pixelY).getPropOrDefault("density", DEFAULT_DENSITY) <= density && grid.getPixel(pixelX - 1, pixelY + steepness).getPropOrDefault("density", DEFAULT_DENSITY) < density && random < 0.5) {
                            boolean fall = true;
                            for (int steepCheck = steepness - 1; steepCheck > 0; steepCheck--) { //Actual position and immediate side already checked
                                if (grid.getPixel(pixelX - 1, pixelY + steepCheck).getPropOrDefault("density", DEFAULT_DENSITY) >= density) {
                                    fall = false; //Block in way
                                }
                            }
                            if (fall) {
                                grid.swapPositions(pixelX, pixelY, pixelX -= 1, pixelY += steepness);
                            }
                        }
                        // down + right
                        else if (pixelX < grid.getWidth() - 1 && !grid.getPixel(pixelX + 1, pixelY + steepness).hasMoved() && grid.getPixelRight(pixelX, pixelY).getPropOrDefault("density", DEFAULT_DENSITY) <= density && grid.getPixel(pixelX + 1, pixelY + steepness).getPropOrDefault("density", DEFAULT_DENSITY) < density && random >= 0.5) {
                            // TODO: displacement instead of swapping
                            boolean fall = true;
                            for (int steepCheck = steepness - 1; steepCheck > 0; steepCheck--) { //Actual position and immediate side already checked
                                if (grid.getPixel(pixelX + 1, pixelY + steepCheck).getPropOrDefault("density", DEFAULT_DENSITY) >= density) {
                                    fall = false; //Block in way
                                }
                            }
                            if (fall) {
                                grid.swapPositions(pixelX, pixelY, pixelX += 1, pixelY += steepness);
                            }
                        }
                    }
                }

                //check for vertical falling
                if (currentPixel.hasProperty("gravity")) {
                    int gravity = currentPixel.getProperty("gravity");

                    if (!currentPixel.hasMoved()) {

                        //Binds gravity to grid
                        if (gravity > 0) {
                            gravity = (pixelY + gravity < grid.getHeight()) ? gravity : (grid.getHeight() - 1 - pixelY);
                        } else {
                            gravity = (pixelY + gravity >= 0) ? gravity : -pixelY;
                        }

                        //Fall to last air or swap with solid if touching
                        int sign = (gravity > 0) ? 1 : -1;
                        if (gravity * sign > 1) {
                            //Fall to the block above if a solid exists more than 1 away
                            for (int gravityCheck = gravity * sign; gravityCheck > 1; gravityCheck--) {
                                if (grid.getPixel(pixelX, pixelY + gravityCheck * sign).getPropOrDefault("density", DEFAULT_DENSITY) > 0) {
                                    gravity = gravityCheck * sign - sign;
                                }
                            }
                            //Check if touching solid
                            if (grid.getPixel(pixelX, pixelY + sign).getPropOrDefault("density", DEFAULT_DENSITY) > 0) {
                                gravity = sign;
                            }
                        }

                        if (grid.getPixel(pixelX, pixelY + gravity).getPropOrDefault("density", DEFAULT_DENSITY) < density && !grid.getPixel(pixelX, pixelY + gravity).hasMoved()) {
                            grid.swapPositions(pixelX, pixelY, pixelX, pixelY += gravity);
                        }
                    }
                }

                //liquids move left and right
                if (currentPixel.hasProperty("fluidity")) {
                    int fluidity = currentPixel.getProperty("fluidity");

                    if (Math.random() < fluidity / 100.0 && !currentPixel.hasMoved()) {
                        double random = Math.random();

                        // left
                        if (pixelX > 0 && !grid.getPixelLeft(pixelX, pixelY).hasMoved() && grid.getPixelLeft(pixelX, pixelY).getPropOrDefault("density", DEFAULT_DENSITY) < density && random < 0.5) {
                            grid.swapPositions(pixelX, pixelY, pixelX -= 1, pixelY);
                        }
                        // right
                        else if (pixelX < grid.getWidth() - 1 && !grid.getPixelRight(pixelX, pixelY).hasMoved() && grid.getPixelRight(pixelX, pixelY).getPropOrDefault("density", DEFAULT_DENSITY) < density && random >= 0.5) {
                            grid.swapPositions(pixelX, pixelY, pixelX += 1, pixelY);
                        }
                    }
                }

                //fire calculations
                if (currentPixel.hasProperty("spreads")) {
                    if (currentPixel.getProperty("spreads") == 1) {
                        if (currentPixel.getType().equals("fire")) {
                            flicker(currentPixel, pixelX, pixelY);
                            if (currentPixel.getProperty("strength") == 100) {
                                spread(currentPixel, "flammable", true, pixelX, pixelY);
                            }
                        }
                        else if(currentPixel.getType().equals("lava"))
                            spread(new Fire(), "flammable", false, pixelX, pixelY);
                    } else {
                        currentPixel.changeProperty("spreads", 1);
                    }
                }

                //temperature change system
                if(currentPixel.hasProperty("heating"))
                    currentPixel.changeProperty("temperature", Math.max(Math.min(currentPixel.getPropOrDefault("temperature", 50) + currentPixel.getProperty("heating"),200),0));
                if(currentPixel.hasProperty("temperature")) {
                    if(currentPixel.getType().equals("stone") && currentPixel.getProperty("temperature") > 175)
                        grid.setPixel(pixelX, pixelY, new Lava());
                    else if(currentPixel.getType().equals("lava") && currentPixel.getProperty("temperature") < 150)
                        grid.setPixel(pixelX, pixelY, new Stone());
                    else if(currentPixel.getType().equals("ice") && currentPixel.getProperty("temperature") > 30)
                        grid.setPixel(pixelX, pixelY, new Water());
                    else if(currentPixel.getType().equals("water") && currentPixel.getProperty("temperature") < 20)
                        grid.setPixel(pixelX, pixelY, new Ice());
                    else if(currentPixel.getType().equals("water") && currentPixel.getProperty("temperature") > 110)
                        grid.setPixel(pixelX, pixelY, new Steam());
                    else if(currentPixel.getType().equals("steam") && currentPixel.getProperty("temperature") < 90)
                        grid.setPixel(pixelX, pixelY, new Water());
                    grid.getPixel(pixelX,pixelY).changeProperty("temperature",currentPixel.getProperty("temperature")); //Keep old temp
                }

                //plants
                if (currentPixel.hasProperty("growing")) {
                    int growing = currentPixel.getProperty("growing");
                    //flower type
                    if (currentPixel.type.equals("plant")) {
                        grow1(currentPixel, pixelX, pixelY);
                    }
                    //tree type
                    else if (currentPixel.type.equals("alien plant") && growing == 1){
                        if (pixelY > 0 && pixelY < grid.getHeight()-1 && pixelX > 0 && pixelX < grid.getWidth()-1 && (currentPixel.getProperty("power") != 100 || grid.getPixel(pixelX, pixelY +1).hasProperty("fertile")) && currentPixel.getProperty("power") > 0){
                            grow2(currentPixel, pixelX, pixelY);
                            currentPixel.changeProperty("power", 0).changeProperty("gravity", 0).changeProperty("support", 0);
                        }
                    }
                    //tree type 2
                    else if(currentPixel.type.equals("plant3"))
                        grow3(currentPixel, pixelX, pixelY);
                }
                //slime that wanders around
                if (currentPixel.type.equals("slime")){
                    if (!slimeExists){
                        refreshEdges();
                        slimeExists = true;
                    }
                    int neighbors = checkSurroundingsFor(currentPixel, pixelX, pixelY, "group", 1, 1, 1, false);
                    int supports = checkSurroundingsFor(currentPixel, pixelX, pixelY, "support", 1, Integer.MAX_VALUE, 1, true);
                    if (currentPixel.getProperty("group") == 1 && neighbors < 8){
                        slimeEdges.add(new ArrayList<Integer>(Arrays.asList(pixelX, pixelY)));
                    }
                    if (supports > 0){
                        slimeSupports += 1;
                        currentPixel.changeProperty("stable", 1);
                    }
                    else{
                        if (currentPixel.getProperty("stable") == 1){
                            currentPixel.changeProperty("stable", 0);
                            slimeSupports -= 1;
                        }
                    }
                    if (supports == 0){
                        currentPixel.changeProperty("gravity", 2);
                    }
                    else{
                        currentPixel.changeProperty("gravity", 0);
                    }
                }

                //certain substances will go away over time
                if (currentPixel.hasProperty("duration")) {
                    int duration = currentPixel.getProperty("duration");
                    if (Math.random() < 0.5)
                        duration--;
                    if (duration < 0)
                        grid.setPixel(pixelX, pixelY, new Air());
                    else
                        currentPixel.changeProperty("duration", duration);
                }
            }
            //Alternate direction of updating
            reverse = !reverse;
        }

        if (slimeSupports > 0) {
            //get slime pixel furthest from goal
            int farX = 0;
            int farY = 0;
            double farDist = -1;
            for (int i = 0; i < slimeEdges.size(); i++) {
                double dist = DistBetween(slimeEdges.get(i).get(0), slimeEdges.get(i).get(1), slimeGoalX, slimeGoalY);
                if (dist > farDist) {
                    farDist = dist;
                    farX = slimeEdges.get(i).get(0);
                    farY = slimeEdges.get(i).get(1);
                }
            }

            //get closest possible position for slime pixel
            int closeX = 0;
            int closeY = 0;
            double closeDist = Integer.MAX_VALUE;
            for (int x = 0; x < slimeEdgesEmpty.size(); x++) {
                double dist = DistBetween(slimeEdgesEmpty.get(x).get(0), slimeEdgesEmpty.get(x).get(1), slimeGoalX, slimeGoalY);
                if (dist < closeDist && grid.getPixel(slimeEdgesEmpty.get(x).get(0), slimeEdgesEmpty.get(x).get(1)).getPropOrDefault("density", DEFAULT_DENSITY) <= 0) {
                    closeDist = dist;
                    closeX = slimeEdgesEmpty.get(x).get(0);
                    closeY = slimeEdgesEmpty.get(x).get(1);
                }
            }

            //move pixel
            //System.out.println(farDist+" "+closeDist);
            if (farDist != 0 && closeDist != Integer.MAX_VALUE && farDist > closeDist) {
                grid.swapPositions(closeX, closeY, farX, farY);
            } //else if (farDist <= closeDist) {
            refreshEdges();
            //}
        }

        //update metal and electricity states
        for (int x = 0; x < grid.getWidth(); x++) {
            for (int y = 0; y < grid.getHeight(); y++) {
                Pixel pixel = grid.getPixel(x, y);
                if (!pixel.hasMoved() && pixel.hasProperty("fragile")) {
                    grid.setPixel(x, y, new Air());
                }
                if (pixel.getStateOrDefault("willBeConducting", 0) != 0) {
                    int chance = pixel.getPropOrDefault("conductive", 0);
                    if (Math.random() < chance / 100.0) {
                        pixel.setState("conducting", 1);
                    }
                    pixel.setState("willBeConducting", 0);
                }
                if (pixel.getStateOrDefault("recovering", 0) != 0) {
                    pixel.setState("conducting", 0);
                }
                pixel.setMoved(false);
            }
        }

        panel.repaint();
        steps--;
    }
    // refreshes the list of air pixels around the slime
    public void refreshEdges(){
        slimeEdgesEmpty.clear();
        for (int i = 0; i < slimeEdges.size(); i++){
            addEdges(grid.getPixel(slimeEdges.get(i).get(0), slimeEdges.get(i).get(1)), slimeEdges.get(i).get(0), slimeEdges.get(i).get(1));
        }
    }
    // adds pixels around a pixel that are air to the list of air pixels around the slime
    public void addEdges(Pixel original, int origx, int origy){
        for (int x = -1; x <= 1; x++){
            for (int y = -1; y <= 1; y++){
                try {
                    if ((x != 0 || y != 0) && grid.getPixel(origx + x, origy + y).getPropOrDefault("density", DEFAULT_DENSITY) <= 0) {
                        boolean contains = false;
                        for (ArrayList<Integer> each : slimeEdgesEmpty){
                            if (each.get(0) == origx+x && each.get(1) == origy+y){
                                contains = true;
                                break;
                            }
                        }
                        if (!contains) {
                            slimeEdgesEmpty.add(new ArrayList<Integer>(Arrays.asList(origx + x, origy + y)));
                        }
                    }
                }catch(Exception e){}
            }
        }
    }
    // checks the surroundings of a pixel in radius r for pixels with a given
    // property between min and max (inclusive) and returns the number of them
    public int checkSurroundingsFor(Pixel original, int origx, int origy, String property, int min, int max, int r, boolean edgeCounts){
        int num = 0;
        for (int x = -r; x <= r; x++){
            for (int y = -r; y <= r; y++){
                try {
                    if ((x != 0 || y != 0) && grid.getPixel(origx + x, origy + y).hasProperty(property) && grid.getPixel(origx + x, origy + y).getProperty(property) <= max && grid.getPixel(origx + x, origy + y).getProperty(property) >= min) {
                        num++;
                    }
                }catch(Exception e){
                    if (edgeCounts){
                        num++;
                    }
                }
            }
        }
        return num;
    }
    //calculates the distance between two points and returns it as a double
    public double DistBetween(double x1, double y1, double x2, double y2){
        double x = x2-x1;
        double y = y2-y1;
        double dist = Math.sqrt((x*x)+(y*y));
        return dist;
    }

    public void grow1(Pixel pixel, int x, int y) {
        int growing = pixel.getProperty("growing");
        int density = pixel.getProperty("density");
        if (growing == 0) {
            if (y < grid.getHeight() - 1 && grid.getPixelDown(x, y).hasProperty("fertile") &&
                    y > 0 && grid.getPixelUp(x, y).hasProperty("overwritable")) {
                pixel.changeProperty("growing", 1);
                pixel.changeProperty("gravity", 0);
                pixel.changeProperty("support", 0);
                pixel.addProperty("height", (int) (Math.max(Math.random() * pixel.getPropOrDefault("maxheight", 0), pixel.getPropOrDefault("minheight", 0))));
            }
        } else if (growing == 1) {
            int height = pixel.getProperty("height");
            if (Math.random() < pixel.getProperty("speed") / 100.0) {
                if (height <= 1) {
                    pixel.changeProperty("growing", 2);
                    pixel.setState("flower", -1); // for use in Renderer
                }
                pixel.changeProperty("height", height - 1);
                if (grid.getPixel(x, y - 1).getPropOrDefault("density", DEFAULT_DENSITY) < density)
                    grid.swapPositions(x, y, x, y - 1);
                Pixel p = new Plant();
                p.changeProperty("support", 0);
                grid.setPixel(x, y, p);
            }
        }
    }
    //calculates the tree plant growth direction and amount
    public void grow2(Pixel pixel, int x, int y){
        int angle = pixel.getProperty("angle");
        double power = pixel.getProperty("power")/100.0;
        int direction = pixel.getProperty("direction");
        int split = pixel.getProperty("split");
        int turning = pixel.getProperty("turning");
        double loss = pixel.getProperty("loss")/100.0;

        if (Math.random() < angle/180.0){
            int newDir;
            if (turning == 0) {
                newDir = direction - 1;
            } else {
                newDir = direction + 1;
            }
            if (newDir == 4) {
                newDir = 0;
            } else if (newDir == -1) {
                newDir = 3;
            }
            pixel.changeProperty("direction", newDir);
            direction = newDir;
            pixel.changeProperty("angle", angle-((int) (loss*100)));
        }
        if (Math.random() < split/100.0){
            int newX;
            int newY;

            if (direction == 0 && turning == 0 && grid.getPixel(x+1, y-1).getPropOrDefault("density", DEFAULT_DENSITY) < 0){
                newX = x+1;
                newY = y-1;
            }
            else if (direction == 0 && turning == 1 && grid.getPixel(x-1, y-1).getPropOrDefault("density", DEFAULT_DENSITY) < 0){
                newX = x-1;
                newY = y-1;
            }
            else if (direction == 1 && turning == 0 && grid.getPixel(x+1, y+1).getPropOrDefault("density", DEFAULT_DENSITY) < 0){
                newX = x+1;
                newY = y+1;
            }
            else if (direction == 1 && turning == 1 && grid.getPixel(x+1, y-1).getPropOrDefault("density", DEFAULT_DENSITY) < 0){
                newX = x+1;
                newY = y-1;
            }
            else if (direction == 2 && turning == 0 && grid.getPixel(x-1, y+1).getPropOrDefault("density", DEFAULT_DENSITY) < 0){
                newX = x-1;
                newY = y+1;
            }
            else if (direction == 2 && turning == 1 && grid.getPixel(x+1, y+1).getPropOrDefault("density", DEFAULT_DENSITY) < 0){
                newX = x+1;
                newY = y+1;
            }
            else if (direction == 3 && turning == 0 && grid.getPixel(x-1, y+1).getPropOrDefault("density", DEFAULT_DENSITY) < 0){
                newX = x-1;
                newY = y+1;
            }
            else {//(direction == 3 && turning == 1 && grid.getPixel(x-1, y-1).getProperty("density") < 0)
                newX = x-1;
                newY = y-1;
            }
            if (newX >= 0 && newX < grid.getWidth() && newY >= 0 && newY < grid.getWidth()) {
                Pixel newPlant = new AlienPlant(false);
                newPlant.changeProperty("turning", Math.abs(turning - 1));
                newPlant.changeProperty("power", (int) ((power - loss)*100));
                pixel.changeProperty("angle", angle);
                grid.setPixel(newX, newY, newPlant);
            }
        }
        int newx = -1;
        int newy = -1;
        if (direction == 0 && grid.getPixel(x, y - 1).getPropOrDefault("density", DEFAULT_DENSITY) < 0) {
            newx = x;
            newy = y - 1;
        } else if (direction == 1 && grid.getPixel(x + 1, y).getPropOrDefault("density", DEFAULT_DENSITY) < 0) {
            newx = x + 1;
            newy = y;
        } else if (direction == 2 && grid.getPixel(x, y + 1).getPropOrDefault("density", DEFAULT_DENSITY) < 0) {
            newx = x;
            newy = y + 1;
        } else if (direction == 3 && grid.getPixel(x - 1, y).getPropOrDefault("density", DEFAULT_DENSITY) < 0) {
            newx = x - 1;
            newy = y;
        }
        if (newx >= 0 && newx < grid.getWidth() && newy >= 0 && newy < grid.getHeight()) {
            Pixel newPlant = new AlienPlant(false);
            newPlant.changeProperty("turning", turning);
            newPlant.changeProperty("direction", direction);
            newPlant.changeProperty("angle", angle);
            newPlant.changeProperty("power", (int) ((power - loss) * 100));
            grid.setPixel(newx, newy, newPlant);
            Pixel trunk = new Wood();
            trunk.setColor(Color.magenta.darker());
            grid.setPixel(x, y, trunk);
        }

    }

    //grows based on int displacement
    public void grow3(Pixel pixel, int x, int y){
        int growing = pixel.getProperty("growing");
        int density = pixel.getProperty("density");

        if (growing == 0) {
            if (y < grid.getHeight() - 1 && grid.getPixelDown(x, y).hasProperty("fertile") &&
                    y > 0 && grid.getPixelUp(x, y).getPropOrDefault("density", DEFAULT_DENSITY) < density) {
                pixel.changeProperty("growing", 1)
                    .changeProperty("gravity", 0)
                    .changeProperty("support", 0);
            }
        } else if (growing == 1) {
            if (Math.random() < pixel.getProperty("speed") / 100.0) {
                if (Math.random() > pixel.getProperty("power") / 100.0) {  //check if it will stop growing
                    pixel.changeProperty("growing", 2);
                    pixel.setState("flower", -1); // for use in Renderer
                }
                //check if it changes direction
                int direction = pixel.getProperty("direction");
                if(Math.random() < pixel.getProperty("turning") / 100.0)
                    direction += Math.random() < 0.5 ? -1 : 1;
                if(direction > 1) direction = 1;
                else if(direction < -1) direction = -1;
                pixel.changeProperty("direction", direction);
                int newX = Math.max(Math.min(x + direction, grid.getWidth()-1), 0);

                //grow
                if (y > 0 && grid.getPixel(newX, y - 1).getPropOrDefault("density", DEFAULT_DENSITY) < density)
                    grid.swapPositions(x, y, newX, y - 1);
                Pixel p = new Plant3()
                    .changeProperty("growing", 2)
                    .changeProperty("gravity", 0)
                    .changeProperty("support", 0);
                grid.setPixel(x, y, p);

                //check if it will split
                if(Math.random() < pixel.getProperty("split") / 100.0)
                {
                    double random = Math.random();
                    newX = Math.max(Math.min(x + (random < 0.3 ? -1 : random < 0.6 ? 0 : 1), grid.getWidth()-1), 0);
                    if (y > 0 && grid.getPixel(newX, y - 1).getPropOrDefault("density", DEFAULT_DENSITY) < density)
                    {
                        p = new Plant3()
                            .changeProperty("growing", 1)
                            .changeProperty("gravity", 0)
                            .changeProperty("support", 0);
                        grid.setPixel(newX, y - 1, p);
                    }
                }
            }
        }
    }

    //adds fire pixels about the main fire pixel to give a special effect
    public void flicker(Pixel pixel, int x, int y) {

        Random r = new Random();

        double spreadChance = 0.65;
        double spreadDecrease = 0.05;
        double decreaseAmount = 0;

        double strength = pixel.getProperty("strength") / 100.0;
        Color color = pixel.getColor();

        try {
            if (r.nextDouble() <= strength * spreadChance && grid.getPixel(x, y - 1).hasProperty("overwritable")) {
                Pixel newFlame = new Fire();
                newFlame.changeProperty("strength", (int) ((strength - spreadDecrease) * 100));

                color = new Color(color.getRed() / 255.0f, (color.getGreen() / 255.0f) * (newFlame.getProperty("strength") / 100.0f), color.getBlue() / 255.0f);
                newFlame.setColor(color);

                grid.setPixel(x, y - 1, newFlame);
            }
        } catch (Exception ignored) {
        }
        if (r.nextDouble() > strength) {
            strength *= decreaseAmount;
            if (strength == 0) {
                if (r.nextDouble() < 0.01) {
                    grid.setPixel(x, y, new Smoke());
                } else {
                    grid.setPixel(x, y, new Air());
                }
            }
        }
    }

    //spreads the fire to neighboring flammable pixels
    public void spread(Pixel original, String fuel, boolean requiresFuel, int xpos, int ypos) {

        boolean hasFuel = false;

        Random r = new Random();

        for (int x = -1; x <= 1; x++) {
            for (int y = -1; y <= 1; y++) {
                if (x + xpos >= 0 && x + xpos < grid.getWidth() && y + ypos >= 0 && y + ypos < grid.getHeight() && grid.getPixel(x + xpos, y + ypos).hasProperty("flammable")) {
                    light(original, x + xpos, y + ypos);
                    if (x == 0 || y == 0) {
                        hasFuel = true;
                        loseFuel(grid.getPixel(x + xpos, y + ypos), r.nextInt(3), x + xpos, y + ypos);
                    }
                }
            }
        }

        if (!hasFuel && requiresFuel) {
            grid.setPixel(xpos, ypos, new Air());
        }
    }

    //lights flammables by putting fire pixels around them
    public void light(Pixel original, int x, int y) {
        for (int i = -1; i <= 1; i++) {
            try {
                int checkX = x;
                int checkY = y;
                if (i == 0) {
                    checkY = y - 1;
                } else {
                    checkX = x + i;
                }

                if (grid.getPixel(checkX, checkY).hasProperty("overwritable")) {
                    Pixel clone = original.duplicate();
                    clone.changeProperty("spreads", 0);
                    grid.setPixel(checkX, checkY, clone);
                }
            } catch (Exception ignored) {
            }
        }
    }

    //decreases the fuel value, eventually turning pixels into charcoal
    public void loseFuel(Pixel pixel, int amount, int x, int y) {
        if (pixel.hasProperty("fuel")) {
            if (pixel.getProperty("fuel") > 0) {
                pixel.changeProperty("fuel", pixel.getProperty("fuel") - amount);
            } else if (Math.random() < pixel.getPropOrDefault("charcoal", 0)/100.0) {
                grid.setPixel(x, y, new Charcoal());
            } else if (Math.random() < pixel.getPropOrDefault("ash", 0)/100.0) {
                grid.setPixel(x, y, new Ash());
            } else {
                grid.setPixel(x, y, new Air());
            }
        }
    }
}
