package sandbox;

import sandbox.pixels.*;

import javax.swing.*;
import java.awt.*;
import java.util.Random;
import java.util.TimerTask;

public class GameLogic extends TimerTask {

    public static final int DEFAULT_DENSITY = 10000;

    private final Grid grid;
    private final JPanel panel;
    Reactions reactions = new Reactions();
    private boolean isPaused = false;
    private int steps = 0;

    public GameLogic(Grid grid, JPanel panel) {
        this.grid = grid;
        this.panel = panel;
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

        //Start left to right
        boolean reverse = false;
        for (int y = grid.getHeight() - 1; y > -1; y--) {
            for (int x = (reverse ? 1 : 0) * (grid.getWidth() - 1); -1 < x && x < grid.getWidth(); x += reverse ? -1 : 1) {
                Pixel currentPixel = grid.getPixel(x, y);

                int currentX = currentPixel.getX();
                int currentY = currentPixel.getY();

                //check for any reactions with neighbor pixels
                boolean reacted = false;
                if (currentX > 0 && !reacted) {//left
                    Pixel[] products = reactions.getReaction(currentPixel, grid.getPixelLeft(currentX, currentY));
                    if (products != null) {
                        currentPixel = products[0];
                        currentPixel.setX(currentX);
                        currentPixel.setY(currentY);
                        grid.setPixel(currentX, currentY, currentPixel);
                        products[1].setX(currentX - 1);
                        products[1].setY(currentY);
                        grid.setPixel(currentX - 1, currentY, products[1]);
                        reacted = true;
                    }
                }
                if (currentX < grid.getWidth() - 1 && !reacted) {//right
                    Pixel[] products = reactions.getReaction(currentPixel, grid.getPixelRight(currentX, currentY));
                    if (products != null) {
                        currentPixel = products[0];
                        currentPixel.setX(currentX);
                        currentPixel.setY(currentY);
                        grid.setPixel(currentX, currentY, currentPixel);
                        products[1].setX(currentX + 1);
                        products[1].setY(currentY);
                        grid.setPixel(currentX + 1, currentY, products[1]);
                        reacted = true;
                    }
                }
                if (currentY > 0 && !reacted) {//up
                    Pixel[] products = reactions.getReaction(currentPixel, grid.getPixelUp(currentX, currentY));
                    if (products != null) {
                        currentPixel = products[0];
                        currentPixel.setX(currentX);
                        currentPixel.setY(currentY);
                        grid.setPixel(currentX, currentY, currentPixel);
                        products[1].setX(currentX);
                        products[1].setY(currentY - 1);
                        grid.setPixel(currentX, currentY - 1, products[1]);
                        reacted = true;
                    }
                }
                if (currentY < grid.getHeight() - 1 && !reacted) {//down
                    Pixel[] products = reactions.getReaction(currentPixel, grid.getPixelDown(currentX, currentY));
                    if (products != null) {
                        currentPixel = products[0];
                        currentPixel.setX(currentX);
                        currentPixel.setY(currentY);
                        grid.setPixel(currentX, currentY, currentPixel);
                        products[1].setX(currentX);
                        products[1].setY(currentY + 1);
                        grid.setPixel(currentX, currentY + 1, products[1]);
                        reacted = true;
                    }
                }

                int density = currentPixel.getPropOrDefault("density", Integer.MAX_VALUE);

                //transfer electricity pixel into conductor
                if (currentPixel.getType().equals("electricity")) {
                    if (currentY < grid.getHeight() - 1 && grid.getPixelDown(currentX, currentY).hasProperty("conductive")) {
                        currentPixel = new Air(currentX, currentY);
                        grid.setPixel(currentX, currentY, currentPixel);
                        grid.getPixelDown(currentX, currentY).setState("conducting", 1);
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
                                    surroundingElectricity = surroundingElectricity + grid.getPixel(gridX + currentX, gridY + currentY).getStateOrDefault("conducting", 0);
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

                    if (!currentPixel.hasMoved() && currentY < grid.getHeight() - steepness && grid.getPixelDown(currentX, currentY).getPropOrDefault("density", DEFAULT_DENSITY) >= density) {
                        double random = Math.random();

                        // down + left
                        if (currentX > 0 && !grid.getPixel(currentX - 1, currentY + steepness).hasMoved() && grid.getPixelLeft(currentX, currentY).getPropOrDefault("density", DEFAULT_DENSITY) <= density && grid.getPixel(currentX - 1, currentY + steepness).getPropOrDefault("density", DEFAULT_DENSITY) < density && random < 0.5) {
                            boolean fall = true;
                            for (int steepCheck = steepness - 1; steepCheck > 0; steepCheck--) { //Actual position and immediate side already checked
                                if (grid.getPixel(currentX - 1, currentY + steepCheck).getPropOrDefault("density", DEFAULT_DENSITY) >= density) {
                                    fall = false; //Block in way
                                }
                            }
                            if (fall) {
                                grid.swapPositions(currentX, currentY, currentX - 1, currentY + steepness);
                            }
                        }
                        // down + right
                        else if (currentX < grid.getWidth() - 1 && !grid.getPixel(currentX + 1, currentY + steepness).hasMoved() && grid.getPixelRight(currentX, currentY).getPropOrDefault("density", DEFAULT_DENSITY) <= density && grid.getPixel(currentX + 1, currentY + steepness).getPropOrDefault("density", DEFAULT_DENSITY) < density && random >= 0.5) {
                            // TODO: displacement instead of swapping
                            boolean fall = true;
                            for (int steepCheck = steepness - 1; steepCheck > 0; steepCheck--) { //Actual position and immediate side already checked
                                if (grid.getPixel(currentX + 1, currentY + steepCheck).getPropOrDefault("density", DEFAULT_DENSITY) >= density) {
                                    fall = false; //Block in way
                                }
                            }
                            if (fall) {
                                grid.swapPositions(currentX, currentY, currentX + 1, currentY + steepness);
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
                            gravity = (currentY + gravity < grid.getHeight()) ? gravity : (grid.getHeight() - 1 - currentY);
                        } else {
                            gravity = (currentY + gravity >= 0) ? gravity : -currentY;
                        }
                        //Fall to last air or swap with solid if touching
                        int sign = (gravity > 0) ? 1 : -1;
                        if (gravity * sign > 1) {
                            //Fall to the block above if a solid exists more than 1 away
                            for (int gravityCheck = gravity * sign; gravityCheck > 1; gravityCheck--) {
                                if (grid.getPixel(currentX, currentY + gravityCheck * sign).getPropOrDefault("density", DEFAULT_DENSITY) > 0) {
                                    gravity = gravityCheck * sign - sign;
                                }
                            }
                            //Check if touching solid
                            if (grid.getPixel(currentX, currentY + sign).getPropOrDefault("density", DEFAULT_DENSITY) > 0) {
                                gravity = sign;
                            }
                        }

                        if (grid.getPixel(currentX, currentY + gravity).getPropOrDefault("density", DEFAULT_DENSITY) < density && !grid.getPixel(currentX, currentY + gravity).hasMoved()) {
                            grid.swapPositions(currentX, currentY, currentX, currentY + gravity);
                        }
                    }
                }

                //liquids move left and right
                if (currentPixel.hasProperty("fluidity")) {
                    int fluidity = currentPixel.getProperty("fluidity");

                    if (Math.random() < fluidity / 100.0 && !currentPixel.hasMoved()) {
                        double random = Math.random();

                        // left
                        if (currentX > 0 && !grid.getPixelLeft(currentX, currentY).hasMoved() && grid.getPixelLeft(currentX, currentY).getPropOrDefault("density", DEFAULT_DENSITY) < density && random < 0.5) {
                            grid.swapPositions(currentX, currentY, currentX - 1, currentY);
                        }
                        // right
                        else if (currentX < grid.getWidth() - 1 && !grid.getPixelRight(currentX, currentY).hasMoved() && grid.getPixelRight(currentX, currentY).getPropOrDefault("density", DEFAULT_DENSITY) < density && random >= 0.5) {
                            grid.swapPositions(currentX, currentY, currentX + 1, currentY);
                        }
                    }
                }

                //fire calculations
                if (currentPixel.hasProperty("spreads")) {
                    if (currentPixel.getProperty("spreads") == 1) {
                        if (currentPixel.getType().equals("fire")) {
                            flicker(currentPixel);
                            if (currentPixel.getProperty("strength") == 100) {
                                spread(currentPixel, "flammable");
                            }
                        }
                    } else {
                        currentPixel.changeProperty("spreads", 1);
                    }
                }
                //plants
                if (currentPixel.hasProperty("growing")) {
                    int growing = currentPixel.getProperty("growing");
                    //flower type
                    if (currentPixel.type.equals("plant")) {
                        // System.out.println("true");
                        if (growing == 0) {
                            if (currentY < grid.getHeight() - 1 && grid.getPixelDown(currentX, currentY).hasProperty("fertile") &&
                                    currentY > 0 && grid.getPixelUp(currentX, currentY).getType().equals("air")) {
                                currentPixel.changeProperty("growing", 1);
                                currentPixel.changeProperty("gravity", 0);
                                currentPixel.addProperty("height", (int) (Math.max(Math.random() * currentPixel.getPropOrDefault("maxheight", 0), currentPixel.getPropOrDefault("minheight", 0))));
                            }
                        } else if (growing == 1) {
                            int height = currentPixel.getProperty("height");
                            if (Math.random() < currentPixel.getProperty("speed") / 100.0) {
                                if (height <= 1) {
                                    currentPixel.changeProperty("growing", 2);
                                    currentPixel.setState("flower", -1); // for use in Renderer
                                }
                                currentPixel.changeProperty("height", height - 1);
                                if (grid.getPixel(currentX, currentY - 1).getPropOrDefault("density", DEFAULT_DENSITY) < density)
                                    grid.swapPositions(currentX, currentY, currentX, currentY - 1);
                                grid.setPixel(currentX, currentY, new Plant(currentX, currentY));
                            }
                        }
                    }
                    //tree type
                    else if (currentPixel.type.equals("plant2") && growing == 1){
                        if (currentY > 0 && currentY < grid.getHeight()-1 && currentX > 0 && currentX < grid.getWidth()-1 && (currentPixel.getProperty("power") != 100 || grid.getPixel(currentX, currentY+1).hasProperty("fertile")) && currentPixel.getProperty("power") > 0){
                            grow2(currentPixel);
                            currentPixel.changeProperty("power", 0);
                        }
                    }
                }
                
                //certain substances will go away over time
                if (currentPixel.hasProperty("duration")) {
                    int duration = currentPixel.getProperty("duration");
                    if (Math.random() < 0.5)
                        duration--;
                    if (duration < 0)
                        grid.setPixel(currentX, currentY, new Air(currentX, currentY));
                    else
                        currentPixel.changeProperty("duration", duration);
                }
            }
            //Alternate direction of updating
            reverse = !reverse;
        }

        //update metal and electricity states
        for (int x = 0; x < grid.getWidth(); x++) {
            for (int y = 0; y < grid.getHeight(); y++) {
                Pixel pixel = grid.getPixel(x, y);
                if (!pixel.hasMoved() && pixel.getType().equals("electricity")) {
                    grid.setPixel(x, y, new Air(x, y));
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
    //calculates the tree plant growth direction and amount
    public void grow2(Pixel pixel){
        int angle = pixel.getProperty("angle");
        double power = pixel.getProperty("power")/100.0;
        int direction = pixel.getProperty("direction");
        int split = pixel.getProperty("split");
        int turning = pixel.getProperty("turning");
        double loss = pixel.getProperty("loss")/100.0;
        int x = pixel.getX();
        int y = pixel.getY();

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
            Pixel newPlant;

            if (direction == 0 && turning == 0 && grid.getPixel(x+1, y-1).getProperty("density") < 0){
                newPlant = new Plant2(x+1, y-1);
            }
            else if (direction == 0 && turning == 1 && grid.getPixel(x-1, y-1).getProperty("density") < 0){
                newPlant = new Plant2(x-1, y-1);
            }
            else if (direction == 1 && turning == 0 && grid.getPixel(x+1, y+1).getProperty("density") < 0){
                newPlant = new Plant2(x+1, y+1);
            }
            else if (direction == 1 && turning == 1 && grid.getPixel(x+1, y-1).getProperty("density") < 0){
                newPlant = new Plant2(x+1, y-1);
            }
            else if (direction == 2 && turning == 0 && grid.getPixel(x-1, y+1).getProperty("density") < 0){
                newPlant = new Plant2(x-1, y+1);
            }
            else if (direction == 2 && turning == 1 && grid.getPixel(x+1, y+1).getProperty("density") < 0){
                newPlant = new Plant2(x+1, y+1);
            }
            else if (direction == 3 && turning == 0 && grid.getPixel(x-1, y+1).getProperty("density") < 0){
                newPlant = new Plant2(x-1, y+1);
            }
            else {//(direction == 3 && turning == 1 && grid.getPixel(x-1, y-1).getProperty("density") < 0)
                newPlant = new Plant2(x-1, y-1);
            }
            if (newPlant.getX() >= 0 && newPlant.getX() < grid.getWidth() && newPlant.getY() >= 0 && newPlant.getY() < grid.getWidth()) {
                newPlant.changeProperty("turning", Math.abs(turning - 1));
                newPlant.changeProperty("power", (int) ((power - loss)*100));
                pixel.changeProperty("angle", angle);
                grid.setPixel(newPlant.getX(), newPlant.getY(), newPlant);
            }
        }
        int newx = -1;
        int newy = -1;
        if (direction == 0 && grid.getPixel(x, y - 1).getProperty("density") < 0) {
            newx = x;
            newy = y - 1;
        } else if (direction == 1 && grid.getPixel(x + 1, y).getProperty("density") < 0) {
            newx = x + 1;
            newy = y;
        } else if (direction == 2 && grid.getPixel(x, y + 1).getProperty("density") < 0) {
            newx = x;
            newy = y + 1;
        } else if (direction == 3 && grid.getPixel(x - 1, y).getProperty("density") < 0) {
            newx = x - 1;
            newy = y;
        }
        if (newx >= 0 && newx < grid.getWidth() && newy >= 0 && newy < grid.getHeight()) {
            Pixel newPlant = new Plant2(newx, newy);
            newPlant.changeProperty("turning", turning);
            newPlant.changeProperty("direction", direction);
            newPlant.changeProperty("angle", angle);
            newPlant.changeProperty("power", (int) ((power - loss) * 100));
            grid.setPixel(newx, newy, newPlant);
        }

    }
    //adds fire pixels about the main fire pixel to give a special effect
    public void flicker(Pixel pixel) {
        int x = pixel.getX();
        int y = pixel.getY();

        Random r = new Random();

        double spreadChance = 0.65;
        double spreadDecrease = 0.05;
        double decreaseAmount = 0;

        double strength = pixel.getProperty("strength") / 100.0;
        Color color = pixel.getColor();

        try {
            if (r.nextDouble() <= strength * spreadChance && grid.getPixel(x, y - 1).type.equals("air")) {
                Pixel newFlame = new Fire(x, y - 1);
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
                    grid.setPixel(x, y, new Smoke(x, y));
                } else {
                    grid.setPixel(x, y, new Air(x, y));
                }
            }
        }
    }

    //spreads the fire to neighboring flammable pixels
    public void spread(Pixel original, String fuel) {
        int xpos = original.getX();
        int ypos = original.getY();

        boolean hasFuel = false;

        Random r = new Random();

        for (int x = -1; x <= 1; x++) {
            for (int y = -1; y <= 1; y++) {
                if (x + xpos >= 0 && x + xpos < grid.getWidth() && y + ypos >= 0 && y + ypos < grid.getHeight() && grid.getPixel(x + xpos, y + ypos).hasProperty("flammable")) {
                    light(grid.getPixel(x + xpos, y + ypos), original);
                    if (x == 0 || y == 0) {
                        hasFuel = true;
                        loseFuel(grid.getPixel(x + xpos, y + ypos), r.nextInt(3));
                    }
                }
            }
        }

        if (!hasFuel) {
            grid.setPixel(xpos, ypos, new Air(xpos, ypos));
        }
    }

    //lights flammables by putting fire pixels around them
    public void light(Pixel pixel, Pixel original) {
        int x = pixel.getX();
        int y = pixel.getY();
        Pixel check;

        for (int i = -1; i <= 1; i++) {
            try {
                if (i == 0) {
                    check = grid.getPixel(x, y - 1);
                } else {
                    check = grid.getPixel(x + i, y);
                }

                if (check.hasProperty("density") && check.getProperty("density") == -1000) {
                    Pixel clone = original.duplicate();
                    clone.setY(check.getY());
                    clone.setX(check.getX());
                    clone.changeProperty("spreads", 0);
                    grid.setPixel(check.getX(), check.getY(), clone);
                }
            } catch (Exception e) {
            }
        }
    }

    //decreases the fuel value, eventually turning pixels into charcoal
    public void loseFuel(Pixel pixel, int amount) {
        if (pixel.hasProperty("fuel")) {
            if (pixel.getProperty("fuel") > 0) {
                pixel.changeProperty("fuel", pixel.getProperty("fuel") - amount);
            } else if (pixel.hasProperty("gravity")) {
                grid.setPixel(pixel.getX(), pixel.getY(), new Charcoal(pixel.getX(), pixel.getY()));
            }
        }
    }
}
