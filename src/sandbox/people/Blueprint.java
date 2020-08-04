package sandbox.people;

import java.awt.*;
import java.io.Serializable;
import sandbox.*;
import sandbox.pixels.*;

public class Blueprint implements Serializable{

    private static final long serialVersionUID = 01334;

    private Pixel[][] structure;
    private int x;
    private int y;
    protected int comfort = 0; //comfort (0-100)
    protected String name;
    public boolean isBuilt;

    public Blueprint(){ }
    public void setStructure(Pixel[][] structure){
        this.structure = structure;
    }

    public void build(Grid grid, int x, int y){
        this.x = x;
        this.y = y;
        for(int xmod = 0; xmod < structure[0].length; xmod++){
            for(int ymod = 0; ymod < structure.length; ymod++) {
                if (x + xmod >= 0 && x + xmod < grid.getWidth() && y - ymod >= 0 && y - ymod < grid.getHeight() && !structure[(structure.length - 1) - ymod][xmod].hasProperty("overwritable")) {
                    Pixel pixel = structure[(structure.length - 1) - ymod][xmod];
                    pixel.changeProperty("density", -10000);
                    pixel.addProperty("structure", 1);
                    pixel.setBuilding(this);
                    grid.setPixel(x + xmod, y - ymod, pixel);
                }
            }
        }
        isBuilt = true;
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
    public void destroy(Grid grid){
        this.x = x;
        this.y = y;
        for(int xmod = 0; xmod < structure[0].length; xmod++){
            for(int ymod = 0; ymod < structure.length; ymod++){
                if (x+xmod>=0 && x+xmod<grid.getWidth() && y-ymod>=0 && y-ymod<grid.getHeight() && structure[(structure.length-1)-ymod][xmod].getType() == grid.getPixel(x+xmod, y-ymod).getType())
                    grid.setPixel(x+xmod, y-ymod, new Air());
            }
        }
        isBuilt = false;
    }
    public double getPercentRemaining(Grid grid){
        double original = 0;
        double remaining = 0;
        for(int xmod = 0; xmod < structure[0].length; xmod++){
            for(int ymod = 0; ymod < structure.length; ymod++){
                if (x+xmod>=0 && x+xmod<grid.getWidth() && y-ymod>=0 && y-ymod<grid.getHeight() && structure[(structure.length-1)-ymod][xmod].getType() == grid.getPixel(x+xmod, y-ymod).getType() && !structure[(structure.length - 1) - ymod][xmod].hasProperty("overwritable")){
                    original++;
                    remaining++;
                }
                else if (x+xmod>=0 && x+xmod<grid.getWidth() && y-ymod>=0 && y-ymod<grid.getHeight() && structure[(structure.length-1)-ymod][xmod].getType() != grid.getPixel(x+xmod, y-ymod).getType() && !structure[(structure.length - 1) - ymod][xmod].hasProperty("overwritable")){
                    original++;
                }
            }
        }
        return (remaining/original);
    }

    public String getName() {
        return name;
    }
}
