package sandbox;

import sandbox.pixels.Air;
import sandbox.pixels.Pixel;
import sandbox.pixels.Air;

import javax.swing.*;
import java.util.TimerTask;
import java.util.ArrayList;

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
                            spread(currentPixel, "flammable");
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
    public void spread(Pixel original, String fuel){
        int xpos = original.getX();
        int ypos = original.getY();

        boolean hasFuel = false;

        for (int x = -1; x <= 1; x++){
            for (int y = -1; y <= 1; y++){
                if (grid.getPixel(x+xpos, y+ypos).hasProperty("flammable")) {
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
            if (i == 0) {
                check = grid.getPixel(x, y - 1);
            }
            else{
                check = grid.getPixel(x+i, y);
            }

            if (check.hasProperty("density") && check.getProperty("density") == -1) {
                Pixel clone = original.duplicate();
                clone.setY(check.getY());
                clone.setX(check.getX());
                clone.changeProperty("spreads", 0);
                grid.setPixel(check.getX(), check.getY(), clone);
            }
        }
    }

    /*public void spread(Pixel original, String spreadable, boolean staysWithoutFuel){
        int[] newPositions;
        boolean stays = staysWithoutFuel;

        int x = original.getX();
        int y = original.getY();

        int h = grid.getHeight()-1;
        int w = grid.getWidth()-1;

        ArrayList<Integer> positions = new ArrayList<Integer>();

        if ((x != 0 && y != 0) && grid.getPixel(x-1,y-1).hasProperty(spreadable)) {
            positions.add(x-1);
            positions.add(y);
            positions.add(x);
            positions.add(y-1);
        }
        if ((x != w && y != 0) && grid.getPixel(x+1,y-1).hasProperty(spreadable)) {
            positions.add(x+1);
            positions.add(y);
            positions.add(x);
            positions.add(y-1);
        }
        if ((x != 0 && y != h) && grid.getPixel(x-1,y+1).hasProperty(spreadable)) {
            positions.add(x-1);
            positions.add(y);
            positions.add(x);
            positions.add(y+1);
        }
        if ((x != w && y != h) && grid.getPixel(x+1,y+1).hasProperty(spreadable)) {
            positions.add(x);
            positions.add(y+1);
            positions.add(x+1);
            positions.add(y);
        }
        if ((x != 0) && grid.getPixel(x-1,y).hasProperty(spreadable)) {
            positions.add(x-1);
            positions.add(y+1);
            positions.add(x);
            positions.add(y);
        }
        if ((x != w) && grid.getPixel(x+1,y).hasProperty(spreadable)) {
            positions.add(x+1);
            positions.add(y+1);
            positions.add(x);
            positions.add(y);
        }
        if ((y != h) && grid.getPixel(x,y+1).hasProperty(spreadable)) {
            positions.add(x-1);
            positions.add(y+1);
            positions.add(x+1);
            positions.add(y+1);
        }
        if ((y != 0) && grid.getPixel(x,y-1).hasProperty(spreadable)) {
            positions.add(x-1);
            positions.add(y-1);
            positions.add(x+1);
            positions.add(y-1);
            positions.add(x);
            positions.add(y);
        }

        for (int i = 0; i < positions.size(); i+= 2){
            if (!check(grid, positions.get(i), positions.get(i+1))){
              positions.remove(i+1);
              positions.remove(i);
            }
        }

        for (int i = 0; i < positions.size(); i += 2){
            Pixel newPixel = original.duplicate();
            newPixel.setX(positions.get(i));
            newPixel.setY(positions.get(i+1));
            grid.setPixel(positions.get(i), positions.get(i+1), newPixel);
        }
        5if (!stays){
            grid.setPixel(original.getX(), original.getY(), new Air(original.getX(), original.getY()));
        }
    }
    public boolean check(Grid grid, int x, int y){
        return (grid.getPixel(x,y).type.equals("air"));
    }*/
}
