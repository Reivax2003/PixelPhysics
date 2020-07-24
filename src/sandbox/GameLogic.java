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

                //check for any reactions with neighbor pixels
                boolean reacted = false;
                if (x > 0 && !reacted) {//left
                    Pixel[] products = reactions.getReaction(currentPixel, grid.getPixelLeft(x, y));
                    if (products != null) {
                        currentPixel = products[0];
                        grid.setPixel(x, y, currentPixel);
                        grid.setPixel(x - 1, y, products[1]);
                        reacted = true;
                    }
                }
                if (x < grid.getWidth() - 1 && !reacted) {//right
                    Pixel[] products = reactions.getReaction(currentPixel, grid.getPixelRight(x, y));
                    if (products != null) {
                        currentPixel = products[0];
                        grid.setPixel(x, y, currentPixel);
                        grid.setPixel(x + 1, y, products[1]);
                        reacted = true;
                    }
                }
                if (y > 0 && !reacted) {//up
                    Pixel[] products = reactions.getReaction(currentPixel, grid.getPixelUp(x, y));
                    if (products != null) {
                        currentPixel = products[0];
                        grid.setPixel(x, y, currentPixel);
                        grid.setPixel(x, y - 1, products[1]);
                        reacted = true;
                    }
                }
                if (y < grid.getHeight() - 1 && !reacted) {//down
                    Pixel[] products = reactions.getReaction(currentPixel, grid.getPixelDown(x, y));
                    if (products != null) {
                        currentPixel = products[0];
                        grid.setPixel(x, y, currentPixel);
                        grid.setPixel(x, y + 1, products[1]);
                        reacted = true;
                    }
                }

                int density = currentPixel.getPropOrDefault("density", Integer.MAX_VALUE);

                //transfer electricity pixel into conductor
                if (currentPixel.getType().equals("electricity")) {
                    if (y < grid.getHeight() - 1 && grid.getPixelDown(x, y).hasProperty("conductive")) {
                        currentPixel = new Air();
                        grid.setPixel(x, y, currentPixel);
                        grid.getPixelDown(x, y).setState("conducting", 1);
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
                                    surroundingElectricity = surroundingElectricity + grid.getPixel(gridX + x, gridY + y).getStateOrDefault("conducting", 0);
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

                    if (!currentPixel.hasMoved() && y < grid.getHeight() - steepness && grid.getPixelDown(x, y).getPropOrDefault("density", DEFAULT_DENSITY) >= density) {
                        double random = Math.random();

                        // down + left
                        if (x > 0 && !grid.getPixel(x - 1, y + steepness).hasMoved() && grid.getPixelLeft(x, y).getPropOrDefault("density", DEFAULT_DENSITY) <= density && grid.getPixel(x - 1, y + steepness).getPropOrDefault("density", DEFAULT_DENSITY) < density && random < 0.5) {
                            boolean fall = true;
                            for (int steepCheck = steepness - 1; steepCheck > 0; steepCheck--) { //Actual position and immediate side already checked
                                if (grid.getPixel(x - 1, y + steepCheck).getPropOrDefault("density", DEFAULT_DENSITY) >= density) {
                                    fall = false; //Block in way
                                }
                            }
                            if (fall) {
                                grid.swapPositions(x, y, x - 1, y + steepness);
                            }
                        }
                        // down + right
                        else if (x < grid.getWidth() - 1 && !grid.getPixel(x + 1, y + steepness).hasMoved() && grid.getPixelRight(x, y).getPropOrDefault("density", DEFAULT_DENSITY) <= density && grid.getPixel(x + 1, y + steepness).getPropOrDefault("density", DEFAULT_DENSITY) < density && random >= 0.5) {
                            // TODO: displacement instead of swapping
                            boolean fall = true;
                            for (int steepCheck = steepness - 1; steepCheck > 0; steepCheck--) { //Actual position and immediate side already checked
                                if (grid.getPixel(x + 1, y + steepCheck).getPropOrDefault("density", DEFAULT_DENSITY) >= density) {
                                    fall = false; //Block in way
                                }
                            }
                            if (fall) {
                                grid.swapPositions(x, y, x + 1, y + steepness);
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
                            gravity = (y + gravity < grid.getHeight()) ? gravity : (grid.getHeight() - 1 - y);
                        } else {
                            gravity = (y + gravity >= 0) ? gravity : -y;
                        }

                        //Fall to last air or swap with solid if touching
                        int sign = (gravity > 0) ? 1 : -1;
                        if (gravity * sign > 1) {
                            //Fall to the block above if a solid exists more than 1 away
                            for (int gravityCheck = gravity * sign; gravityCheck > 1; gravityCheck--) {
                                if (grid.getPixel(x, y + gravityCheck * sign).getPropOrDefault("density", DEFAULT_DENSITY) > 0) {
                                    gravity = gravityCheck * sign - sign;
                                }
                            }
                            //Check if touching solid
                            if (grid.getPixel(x, y + sign).getPropOrDefault("density", DEFAULT_DENSITY) > 0) {
                                gravity = sign;
                            }
                        }

                        if (grid.getPixel(x, y + gravity).getPropOrDefault("density", DEFAULT_DENSITY) < density && !grid.getPixel(x, y + gravity).hasMoved()) {
                            grid.swapPositions(x, y, x, y + gravity);
                        }
                    }
                }

                //liquids move left and right
                if (currentPixel.hasProperty("fluidity")) {
                    int fluidity = currentPixel.getProperty("fluidity");

                    if (Math.random() < fluidity / 100.0 && !currentPixel.hasMoved()) {
                        double random = Math.random();

                        // left
                        if (x > 0 && !grid.getPixelLeft(x, y).hasMoved() && grid.getPixelLeft(x, y).getPropOrDefault("density", DEFAULT_DENSITY) < density && random < 0.5) {
                            grid.swapPositions(x, y, x - 1, y);
                        }
                        // right
                        else if (x < grid.getWidth() - 1 && !grid.getPixelRight(x, y).hasMoved() && grid.getPixelRight(x, y).getPropOrDefault("density", DEFAULT_DENSITY) < density && random >= 0.5) {
                            grid.swapPositions(x, y, x + 1, y);
                        }
                    }
                }

                //fire calculations
                if (currentPixel.hasProperty("spreads")) {
                    if (currentPixel.getProperty("spreads") == 1) {
                        if (currentPixel.getType().equals("fire")) {
                            flicker(currentPixel, x, y);
                            if (currentPixel.getProperty("strength") == 100) {
                                spread(currentPixel, "flammable", true);
                            }
                        }
                        else if(currentPixel.getType().equals("lava"))
                            spread(new Fire(currentX, currentY), "flammable", false);
                    } else {
                        currentPixel.changeProperty("spreads", 1);
                    }
                }

                if(currentPixel.hasProperty("heating"))
                    currentPixel.changeProperty("temperature", Math.max(Math.min(currentPixel.getPropOrDefault("temperature", 50) + currentPixel.getProperty("heating"),100),0));
                if(currentPixel.hasProperty("temperature"))
                    if(currentPixel.getType().equals("stone") && currentPixel.getProperty("temperature") > 75)
                        grid.setPixel(currentX, currentY, currentPixel = new Lava(currentX, currentY));
                    else if(currentPixel.getType().equals("lava") && currentPixel.getProperty("temperature") < 75)
                        grid.setPixel(currentX, currentY, currentPixel = new Stone(currentX, currentY));

                //plants
                if (currentPixel.hasProperty("growing")) {
                    int growing = currentPixel.getProperty("growing");
                    //flower type
                    if (currentPixel.type.equals("plant")) {
                        grow1(currentPixel, x, y);
                    }
                    //tree type
                    else if (currentPixel.type.equals("alien plant") && growing == 1){
                        if (y > 0 && y < grid.getHeight()-1 && x > 0 && x < grid.getWidth()-1 && (currentPixel.getProperty("power") != 100 || grid.getPixel(x, y +1).hasProperty("fertile")) && currentPixel.getProperty("power") > 0){
                            grow2(currentPixel, x, y);
                            currentPixel.changeProperty("power", 0);
                        }
                    }
                    //tree type 2
                    else if(currentPixel.type.equals("plant3"))
                        grow3(currentPixel, x, y);
                }

                //certain substances will go away over time
                if (currentPixel.hasProperty("duration")) {
                    int duration = currentPixel.getProperty("duration");
                    if (Math.random() < 0.5)
                        duration--;
                    if (duration < 0)
                        grid.setPixel(x, y, new Air());
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
                Pixel newPlant = new AlienPlant();
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
            Pixel newPlant = new AlienPlant();
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
    public void spread(Pixel original, String fuel, int xpos, int ypos) {

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
            grid.setPixel(xpos, ypos, new Air(xpos, ypos));
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
        if (pixel.getPropOrDefault("fuel", 0) > 0) {
            pixel.changeProperty("fuel", pixel.getProperty("fuel") - amount);
        } else if (pixel.hasProperty("gravity")) {
            grid.setPixel(x, y, new Charcoal());
        }
    }
}
