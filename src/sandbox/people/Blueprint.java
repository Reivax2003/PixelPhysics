package sandbox.people;

import java.awt.*;
import sandbox.*;
import sandbox.pixels.*;

public class Blueprint {
    private Pixel[][] structure;
    private int x;
    private int y;

    public Blueprint(){ }
    public void setStructure(Pixel[][] structure){
        this.structure = structure;
    }

    public void build(Grid grid, int x, int y){
        this.x = x;
        this.y = y;
        for(int xmod = 0; xmod < structure[0].length; xmod++){
            for(int ymod = 0; ymod < structure.length; ymod++){
                grid.setPixel(x+xmod, y-ymod, structure[(structure.length-1)-ymod][xmod]);
            }
        }
    }

    public int getX(){
        return x;
    }
    public int getY(){
        return y;
    }
    public Pixel[][] getStructure(){
        return structure;
    }
}