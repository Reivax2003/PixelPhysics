package sandbox;

import sandbox.pixels.*;

import javax.swing.*;
import java.util.TimerTask;
import java.util.Random;
import java.awt.Color;

public class GameLogic extends TimerTask {

    public static final int DEFAULT_DENSITY = 10000;

    private final Grid grid;
    private final JPanel panel;

    Reactions reactions = new Reactions();

    public GameLogic(Grid grid, JPanel panel) {
        this.grid = grid;
        this.panel = panel;
    }

    @Override
    public void run() {
        //Start left to right
        boolean reverse = false;
        for (int y = grid.getHeight() - 1; y > -1; y--) {
            for (int x = (reverse ? 1:0) * (grid.getWidth() -1); -1 < x && x < grid.getWidth(); x+= reverse ? -1:1 ) {
                Pixel currentPixel = grid.getPixel(x, y);

                int currentX = currentPixel.getX();
                int currentY = currentPixel.getY();

                //check for any reactions with neighbor pixels
                boolean reacted = false;
                if(currentX > 0 && !reacted)
                {
                    Pixel[] products = reactions.getReaction(currentPixel, grid.getPixelLeft(currentX, currentY));
                    if(products != null)
                    {
                        currentPixel = products[0];
                        currentPixel.setX(currentX); currentPixel.setY(currentY);
                        grid.setPixel(currentX, currentY, currentPixel);
                        products[1].setX(currentX-1); products[1].setY(currentY);
                        grid.setPixel(currentX-1, currentY, products[1]);
                        reacted = true;
                    }
                }
                if(currentX < grid.getWidth()-1 && !reacted)
                {
                    Pixel[] products = reactions.getReaction(currentPixel, grid.getPixelRight(currentX, currentY));
                    if(products != null)
                    {
                        currentPixel = products[0];
                        currentPixel.setX(currentX);
                        currentPixel.setY(currentY);
                        grid.setPixel(currentX, currentY, currentPixel);
                        products[1].setX(currentX+1); products[1].setY(currentY);
                        grid.setPixel(currentX+1, currentY, products[1]);
                        reacted = true;
                    }
                }
                if(currentY > 0 && !reacted)
                {
                    Pixel[] products = reactions.getReaction(currentPixel, grid.getPixelUp(currentX, currentY));
                    if(products != null)
                    {
                        currentPixel = products[0];
                        currentPixel.setX(currentX);
                        currentPixel.setY(currentY);
                        grid.setPixel(currentX, currentY, currentPixel);
                        products[1].setX(currentX); products[1].setY(currentY-1);
                        grid.setPixel(currentX, currentY-1, products[1]);
                        reacted = true;
                    }
                }
                if(currentY < grid.getHeight()-1 && !reacted)
                {
                    Pixel[] products = reactions.getReaction(currentPixel, grid.getPixelDown(currentX, currentY));
                    if(products != null)
                    {
                        currentPixel = products[0];
                        currentPixel.setX(currentX);
                        currentPixel.setY(currentY);
                        grid.setPixel(currentX, currentY, currentPixel);
                        products[1].setX(currentX); products[1].setY(currentY+1);
                        grid.setPixel(currentX, currentY+1, products[1]);
                        reacted = true;
                    }
                }

                int density = currentPixel.getPropOrDefault("density", Integer.MAX_VALUE);

                if(currentPixel.getType().equals("electricity")) {
                    if(currentY < grid.getHeight() - 1 && grid.getPixelDown(currentX, currentY).hasProperty("conductive")) {
                        currentPixel = new Air(currentX, currentY);
                        grid.setPixel(currentX, currentY, currentPixel);
                        grid.getPixelDown(currentX, currentY).setState("conducting", 1);
                    }
                }

                if(currentPixel.hasProperty("conductive")) {
                    boolean wasRecovering = false;
                    if(currentPixel.getStateOrDefault("recovering", 0) != 0) {
                        currentPixel.setState("recovering", 0);
                        wasRecovering = true;
                    }

                    if (currentPixel.getStateOrDefault("conducting", 0) != 0) {
                        currentPixel.setState("recovering", 1);
                    }

                    if(!wasRecovering) {
                        int surroundingElectricity = 0;

                        for (int gridX = -1; gridX <= 1; gridX++) {
                            for (int gridY = -1; gridY <= 1; gridY++) {
                                try {
                                    surroundingElectricity = surroundingElectricity + grid.getPixel(gridX + currentX, gridY + currentY).getStateOrDefault("conducting", 0);
                                } catch(Exception ignored) {}
                            }
                        }
                        if(surroundingElectricity == 1 || surroundingElectricity == 2) {
                            currentPixel.setState("willBeConducting", 1);
                        }
                    }
                }

                if(currentPixel.hasProperty("support")) {
                    int steepness = currentPixel.getPropOrDefault("steepness", 1);

                    if(!currentPixel.hasMoved() && currentY < grid.getHeight() - steepness && grid.getPixelDown(currentX, currentY).getPropOrDefault("density", DEFAULT_DENSITY) >= density) {
                        double random = Math.random();

                        // down + left
                        if(currentX > 0 && !grid.getPixel(currentX - 1, currentY + steepness).hasMoved() && grid.getPixelLeft(currentX, currentY).getPropOrDefault("density", DEFAULT_DENSITY) <= density && grid.getPixel(currentX - 1, currentY + steepness).getPropOrDefault("density", DEFAULT_DENSITY) < density && random < 0.5) {
                            boolean fall = true;
                            for (int steepCheck = steepness - 1; steepCheck > 0; steepCheck --) { //Actual position and immediate side already checked
                              if (grid.getPixel(currentX - 1, currentY + steepCheck).getPropOrDefault("density", DEFAULT_DENSITY) >= density) {
                                  fall = false; //Block in way
                              }
                            }
                            if(fall) {
                                grid.swapPositions(currentX, currentY, currentX - 1, currentY + steepness);
                            }
                        }
                        // down + right
                        else if(currentX < grid.getWidth() - 1 && !grid.getPixel(currentX + 1, currentY + steepness).hasMoved() && grid.getPixelRight(currentX, currentY).getPropOrDefault("density", DEFAULT_DENSITY) <= density && grid.getPixel(currentX + 1, currentY + steepness).getPropOrDefault("density", DEFAULT_DENSITY) < density && random >= 0.5) {
                            // TODO: displacement instead of swapping
                            boolean fall = true;
                            for (int steepCheck = steepness - 1; steepCheck > 0; steepCheck --) { //Actual position and immediate side already checked
                              if (grid.getPixel(currentX + 1, currentY + steepCheck).getPropOrDefault("density", DEFAULT_DENSITY) >= density) {
                                  fall = false; //Block in way
                              }
                            }
                            if(fall) {grid.swapPositions(currentX, currentY, currentX + 1, currentY + steepness);}
                        }
                    }
                }

                if(currentPixel.hasProperty("gravity")) {
                    int gravity = currentPixel.getProperty("gravity");

                    if(!currentPixel.hasMoved()){
                      //Binds gravity to grid
                      if (gravity > 0) {
                        gravity = (currentY + gravity < grid.getHeight())? gravity:(grid.getHeight() - 1 - currentY);
                      }
                      else {
                        gravity = (currentY + gravity >= 0)? gravity: -currentY;
                      }
                      //Fall to last air or swap with solid if touching
                      int sign = (gravity > 0)? 1:-1;
                      if(gravity * sign > 1) {
                        //Fall to the block above if a solid exists more than 1 away
                        for (int gravityCheck = gravity * sign; gravityCheck > 1; gravityCheck --) {
                          if (grid.getPixel(currentX, currentY + gravityCheck * sign).getPropOrDefault("density", DEFAULT_DENSITY) > 0) {
                            gravity = gravityCheck * sign - sign;
                          }
                        }
                        //Check if touching solid
                        if (grid.getPixel(currentX, currentY + sign).getPropOrDefault("density", DEFAULT_DENSITY) > 0) {
                          gravity = sign;
                        }
                      }

                      if(grid.getPixel(currentX, currentY + gravity).getPropOrDefault("density", DEFAULT_DENSITY) < density && !grid.getPixel(currentX, currentY + gravity).hasMoved()) {
                          grid.swapPositions(currentX, currentY, currentX, currentY + gravity);
                      }
                  }
                }

                if(currentPixel.hasProperty("fluidity")) {
                    int fluidity = currentPixel.getProperty("fluidity");

                    if(Math.random() < fluidity / 100.0 && !currentPixel.hasMoved()) {
                        double random = Math.random();

                        // left
                        if(currentX > 0 && !grid.getPixelLeft(currentX, currentY).hasMoved() && grid.getPixelLeft(currentX, currentY).getPropOrDefault("density", DEFAULT_DENSITY) < density && random < 0.5) {
                            grid.swapPositions(currentX, currentY, currentX - 1, currentY);
                        }
                        // right
                        else if(currentX < grid.getWidth() - 1 && !grid.getPixelRight(currentX, currentY).hasMoved() && grid.getPixelRight(currentX, currentY).getPropOrDefault("density", DEFAULT_DENSITY) < density && random >= 0.5) {
                            grid.swapPositions(currentX, currentY, currentX + 1, currentY);
                        }
                    }
                }
                if(currentPixel.hasProperty("spreads")) {
                    if (currentPixel.getProperty("spreads") == 1){
                        if (currentPixel.getType().equals("fire")) {
                            flicker(currentPixel);
                            if (currentPixel.getProperty("strength") == 100) {
                                spread(currentPixel, "flammable");
                            }
                        }
                    }
                    else{
                        currentPixel.changeProperty("spreads", 1);
                    }
                }
            }
            //Alternate direction of updating
            reverse = !reverse;
        }

        for(int x = 0; x < grid.getWidth(); x++) {
            for(int y = 0; y < grid.getHeight(); y++) {
                Pixel pixel = grid.getPixel(x, y);
                if(!pixel.hasMoved() && pixel.getType().equals("electricity")) {
                    grid.setPixel(x, y, new Air(x, y));
                }
                if(pixel.getStateOrDefault("willBeConducting", 0) != 0) {
                    int chance = pixel.getPropOrDefault("conductive", 0);
                    if(Math.random() < chance / 100.0) {
                        pixel.setState("conducting", 1);
                    }
                    pixel.setState("willBeConducting", 0);
                }
                if(pixel.getStateOrDefault("recovering", 0) != 0) {
                    pixel.setState("conducting", 0);
                }
                pixel.setMoved(false);
            }
        }

        panel.repaint();
    }
    public void flicker(Pixel pixel){
        int x = pixel.getX();
        int y = pixel.getY();

        Random r = new Random();

        double spreadChance = 0.65;
        double spreadDecrease = 0.05;
        double decreaseAmount = 0;

        double strength = pixel.getProperty("strength")/100.0;
        Color color = pixel.getColor();

        try {
            if (r.nextDouble() <= strength * spreadChance && grid.getPixel(x, y - 1).type.equals("air")) {
                Pixel newFlame = new Fire(x, y - 1);
                newFlame.changeProperty("strength", (int) ((strength - spreadDecrease) * 100));

                color = new Color(color.getRed() / 255.0f, (color.getGreen() / 255.0f) * (newFlame.getProperty("strength") / 100.0f), color.getBlue() / 255.0f);
                newFlame.setColor(color);

                grid.setPixel(x, y - 1, newFlame);
            }
        } catch (Exception ignored) {}
        if (r.nextDouble() > strength){
            strength *= decreaseAmount;
            if (strength == 0){
                if (r.nextDouble() < 0.01) {
                    grid.setPixel(x, y, new Smoke(x, y));
                }
                else{
                    grid.setPixel(x, y, new Air(x, y));
                }
            }
        }
    }
    public void spread(Pixel original, String fuel){
        int xpos = original.getX();
        int ypos = original.getY();

        boolean hasFuel = false;

        Random r = new Random();

        for (int x = -1; x <= 1; x++){
            for (int y = -1; y <= 1; y++){
                if (x+xpos >= 0 && x+xpos < grid.getWidth() && y+ypos >= 0 && y+ypos < grid.getHeight() && grid.getPixel(x+xpos, y+ypos).hasProperty("flammable")) {
                    light(grid.getPixel(x+xpos, y+ypos), original);
                    if (x == 0 || y == 0) {
                        hasFuel = true;
                        loseFuel(grid.getPixel(x+xpos, y+ypos), r.nextInt(3));
                    }
                }
            }
        }

        if (!hasFuel){
            grid.setPixel(xpos, ypos, new Air(xpos, ypos));
        }
    }
    public void light(Pixel pixel, Pixel original){
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
            } catch (Exception e) {}
        }
    }
    public void loseFuel(Pixel pixel, int amount){
        if (pixel.hasProperty("fuel")){
            if (pixel.getProperty("fuel") > 0){
                pixel.changeProperty("fuel", pixel.getProperty("fuel") - amount);
            }
            else if (pixel.hasProperty("gravity")){
                pixel.changeProperty("gravity", 1);
                pixel.addProperty("support", 1);
                pixel.setColor(Color.darkGray.darker());
            }
        }
    }
}
