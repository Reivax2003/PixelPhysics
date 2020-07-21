package sandbox.pixels;

import sandbox.Grid;

import java.awt.*;
import java.util.Random;
import java.util.ArrayList;

public class Fire extends Pixel {

    public Fire(int xpos, int ypos) {
        super("fire", xpos, ypos, Color.yellow);
    }

    public int[] update(Grid grid) {
        int[] newPositions;

        ArrayList<Integer> positions = new ArrayList<Integer>();

        if (grid.getPixel(x-1,y-1).getType().equals("wood")) {
            positions.add(x-1);
            positions.add(y);
            positions.add(x);
            positions.add(y-1);
        }
        if (grid.getPixel(x+1,y-1).getType().equals("wood")) {
            positions.add(x+1);
            positions.add(y);
            positions.add(x);
            positions.add(y-1);
        }
        if (grid.getPixel(x-1,y+1).getType().equals("wood")) {
            positions.add(x-1);
            positions.add(y);
            positions.add(x);
            positions.add(y+1);
        }
        if (grid.getPixel(x+1,y+1).getType().equals("wood")) {
            positions.add(x);
            positions.add(y+1);
            positions.add(x+1);
            positions.add(y);
        }
        if (grid.getPixel(x-1,y).getType().equals("wood")) {
            positions.add(x-1);
            positions.add(y+1);
            positions.add(x);
            positions.add(y);
        }
        if (grid.getPixel(x+1,y).getType().equals("wood")) {
            positions.add(x+1);
            positions.add(y+1);
            positions.add(x);
            positions.add(y);
        }
        if (grid.getPixel(x,y+1).getType().equals("wood")) {
            positions.add(x-1);
            positions.add(y+1);
            positions.add(x+1);
            positions.add(y+1);
        }
        if (grid.getPixel(x,y-1).getType().equals("wood")) {
            positions.add(x-1);
            positions.add(y-1);
            positions.add(x+1);
            positions.add(y-1);
            positions.add(x);
            positions.add(y);
        }

        Random rand = new Random();
        if (rand.nextDouble() > 0.5){
            positions.add(x);
            positions.add(y);
        }
        if (rand.nextDouble() > 0.5){
            positions.add(x);
            positions.add(y+1);
        }

        boolean stays = false;
        int count = 0;
        for (int i = 0; i < positions.size(); i+= 2){
            if (check(grid, positions.get(i), positions.get(i+1))){
              count++;
            }
            else{
              positions.remove(i+1);
              positions.remove(i);
              i -= 2 //Goes back to positions now at this index
            }
        }
        newPositions = new int[count];

        for (int i = 0; i < positions.size(); i++){
            newPositions[i] = positions.get(i);
        }

        return newPositions;
    }

    public boolean check(Grid grid, int x, int y){
        return (grid.getPixel(x,y).type.equals("air"));
    }
}
