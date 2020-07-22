package sandbox;

import sandbox.pixels.*;

import javax.swing.*;
import java.util.TimerTask;
import java.util.ArrayList;
import java.util.Random;
import java.awt.Color;

public class GameLogic extends TimerTask {

    private final int DEFAULT_DENSITY = 10000;

    private final Grid grid;
    private final JPanel panel;

    private boolean reverse;

    Reactions reactions = new Reactions();

    public GameLogic(Grid grid, JPanel panel) {
        this.grid = grid;
        this.panel = panel;
    }

    @Override
    public void run() {
        //Start left to right
        reverse = false;
        for (int y = grid.getHeight() - 1; y > -1; y--) {
            for (int x = (reverse? 1:0) * (grid.getWidth() -1); -1 < x && x < grid.getWidth(); x+= reverse? -1:1 ) {
                Pixel currentPixel = grid.getPixel(x, y);

                int currentX = currentPixel.getX();
                int currentY = currentPixel.getY();

                int density = currentPixel.getPropOrDefault("density", Integer.MAX_VALUE);

                if(currentPixel.hasProperty("support")) {
                    int support = currentPixel.getProperty("support");
                    int steepness = currentPixel.getPropOrDefault("steepness", 1);

                    if(!currentPixel.hasMoved() && currentY < grid.getHeight() - steepness && grid.getPixelDown(currentX, currentY).getPropOrDefault("density", DEFAULT_DENSITY) >= density) {
                        double random = Math.random();

                        // down + left
                        if(currentX > 0 && !grid.getPixel(currentX - 1, currentY + steepness).hasMoved() && grid.getPixelLeft(currentX, currentY).getPropOrDefault("density", DEFAULT_DENSITY) < density && grid.getPixel(currentX - 1, currentY + steepness).getPropOrDefault("density", DEFAULT_DENSITY) < density && random < 0.5) {
                            grid.swapPositions(currentX, currentY, currentX - 1, currentY + steepness);
                        }
                        // down + right
                        else if(currentX < grid.getWidth() - 1 && !grid.getPixel(currentX + 1, currentY + steepness).hasMoved() && grid.getPixelRight(currentX, currentY).getPropOrDefault("density", DEFAULT_DENSITY) < density && grid.getPixel(currentX + 1, currentY + steepness).getPropOrDefault("density", DEFAULT_DENSITY) < density && random >= 0.5) {
                            // TODO: displacement instead of swapping
                            grid.swapPositions(currentX, currentY, currentX + 1, currentY + steepness);
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
                        //Fall to block above if a solid exist more than 1 away
                        for (int gravityCheck = gravity * sign; gravityCheck > 1; gravityCheck --) {
                          if (grid.getPixel(currentX, currentY + gravityCheck * sign).getPropOrDefault("density", DEFAULT_DENSITY) > 0) {
                            gravity = gravityCheck * sign - sign;
                          }
                        }
                        //Check touching
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
                        if (currentPixel.getType() == "fire") {
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

                Boolean reacted = false;
                if(currentX > 0 && !reacted)
                {
                    Pixel product = reactions.getReactionOrDefault(currentPixel.getType(), grid.getPixelLeft(currentX, currentY).getType(), null);
                    if(product != null)
                    {
                        product = product.duplicate();
                        product.setX(currentX);
                        product.setY(currentY);
                        grid.setPixel(currentX, currentY, product);
                        grid.setPixel(currentX-1, currentY, new Air(currentX-1, currentY));
                        reacted = true;
                    }
                }
                if(currentX < grid.getWidth()-1 && !reacted)
                {
                    Pixel product = reactions.getReactionOrDefault(currentPixel.getType(), grid.getPixelRight(currentX, currentY).getType(), null);
                    if(product != null)
                    {
                        product = product.duplicate();
                        product.setX(currentX);
                        product.setY(currentY);
                        grid.setPixel(currentX, currentY, product);
                        grid.setPixel(currentX+1, currentY, new Air(currentX+1, currentY));
                        reacted = true;
                    }
                }
                if(currentY > 0 && !reacted)
                {
                    Pixel product = reactions.getReactionOrDefault(currentPixel.getType(), grid.getPixelUp(currentX, currentY).getType(), null);
                    if(product != null)
                    {
                        product = product.duplicate();
                        product.setX(currentX);
                        product.setY(currentY);
                        grid.setPixel(currentX, currentY, product);
                        grid.setPixel(currentX, currentY-1, new Air(currentX, currentY-1));
                        reacted = true;
                    }
                }
                if(currentY < grid.getHeight()-1 && !reacted)
                {
                    Pixel product = reactions.getReactionOrDefault(currentPixel.getType(), grid.getPixelDown(currentX, currentY).getType(), null);
                    if(product != null)
                    {
                        product = product.duplicate();
                        product.setX(currentX);
                        product.setY(currentY);
                        grid.setPixel(currentX, currentY, product);
                        grid.setPixel(currentX, currentY+1, new Air(currentX, currentY+1));
                        reacted = true;
                    }
                }
            }
            //Alternate direction of updating
            reverse = !reverse;
        }

        for(int x = 0; x < grid.getWidth(); x++) {
            for(int y = 0; y < grid.getHeight(); y++) {
                grid.getPixel(x, y).setMoved(false);
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
            if (r.nextDouble() <= strength * spreadChance && grid.getPixel(x, y - 1).type == "air") {
                Pixel newFlame = new Fire(x, y - 1);
                newFlame.changeProperty("strength", (int) ((strength - spreadDecrease) * 100));

                color = new Color(color.getRed() / 255.0f, (float) ((color.getGreen() / 255.0f) * (newFlame.getProperty("strength") / 100.0f)), color.getBlue() / 255.0f);
                newFlame.setColor(color);

                grid.setPixel(x, y - 1, newFlame);
            }
        } catch (Exception e) {}
        if (r.nextDouble() > strength){
            strength *= decreaseAmount;
            if (strength == 0){
                grid.setPixel(x, y, new Air(x, y));
            }
        }
    }
    public void spread(Pixel original, String fuel){
        int xpos = original.getX();
        int ypos = original.getY();

        boolean hasFuel = false;

        for (int x = -1; x <= 1; x++){
            for (int y = -1; y <= 1; y++){
                if (x+xpos >= 0 && x+xpos < grid.getWidth() && y+ypos >= 0 && y+ypos < grid.getHeight() && grid.getPixel(x+xpos, y+ypos).hasProperty("flammable")) {
                    light(grid.getPixel(x+xpos, y+ypos), original);
                    if (x == 0 || y == 0) {
                        hasFuel = true;
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

                if (check.hasProperty("density") && check.getProperty("density") < 0) {
                    Pixel clone = original.duplicate();
                    clone.setY(check.getY());
                    clone.setX(check.getX());
                    clone.changeProperty("spreads", 0);
                    grid.setPixel(check.getX(), check.getY(), clone);
                }
            } catch(Exception e){}
        }
    }
}
