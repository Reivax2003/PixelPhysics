package sandbox.people;

import java.awt.*;
import java.io.Serializable;
import sandbox.*;
import sandbox.pixels.*;

public class Blueprint implements Serializable{

    private static final long serialVersionUID = 01334;

    private Pixel[][] structure;
    private Pixel[][] terrain;
    private int x;
    private int y;
    protected int comfort = 0; //comfort (0-100)
    protected String name;
    public boolean isBuilt;

    public Blueprint(){ }
    public void setStructure(Pixel[][] structure){
        this.structure = structure;
        this.terrain = new Pixel[structure.length][structure[0].length];
    }
    private void initTerrain(){
        for (int x = 0; x < terrain.length; x++){
            for (int y = 0; y < terrain[0].length; y++){
                terrain[x][y] = new Lava();
            }
        }
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
                    terrain[ymod][xmod] = grid.getPixel(x+xmod, y-ymod).duplicate();
                    grid.setPixel(x + xmod, y - ymod, pixel);
                }
            }
        }
        isBuilt = true;
    }

    public boolean tryBuild(Grid grid, int x, int y){
        this.x = x;
        this.y = y;

        //boundary check
        if (x < 0 || x + structure[0].length >= grid.getWidth() || y - structure.length < 0 || y >= grid.getHeight()) {
            return false;
        }

        //scan and check for other buildings
        for(int xmod = 0; xmod < structure[0].length; xmod++){
            for(int ymod = 0; ymod < structure.length; ymod++) {
                if(/*!structure[(structure.length - 1) - ymod][xmod].hasProperty("overwritable") &&*/ grid.getPixel(x + xmod, y - ymod).hasProperty("structure")){
                    return false;
                }
            }
        }

        for(int xmod = 0; xmod < structure[0].length; xmod++){
            for(int ymod = 0; ymod < structure.length; ymod++) {
                if (x + xmod >= 0 && x + xmod < grid.getWidth() && y - ymod >= 0 && y - ymod < grid.getHeight() && !structure[(structure.length - 1) - ymod][xmod].hasProperty("overwritable")) {
                    Pixel pixel = structure[(structure.length - 1) - ymod][xmod];
                    pixel.changeProperty("density", -10000);
                    pixel.addProperty("structure", 1);
                    pixel.setBuilding(this);
                    terrain[ymod][xmod] = grid.getPixel(x+xmod, y-ymod).duplicate();
                    grid.setPixel(x + xmod, y - ymod, pixel);
                }
            }
        }
        isBuilt = true;
        return true;
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
                if (x + xmod >= 0 && x + xmod < grid.getWidth() && y - ymod >= 0 && y - ymod < grid.getHeight() && grid.getPixel(x+xmod, y-ymod).getBuilding() != null) { // Only if this is part of the building
                    if (grid.getPixel(x+xmod, y-ymod).getBuilding() == this) { // If this is on top
                        if (terrain[ymod][xmod] != null) { // If there is terrain
                            grid.setPixel(x + xmod, y - ymod, terrain[ymod][xmod]);
                        } else{ // If there is no terrain, but it is coverved by the building
                            grid.setPixel(x + xmod, y - ymod, new Air());
                        }
                    } else { // Another building is on top and it's under terrain needs to be set to this's underterrain
                        // x + xmod - grid.getPixel(x+xmod, y-ymod).getBuilding().getX(); // Derive xmod and ymod of upper building
                        // - y + ymod + grid.getPixel(x+xmod, y-ymod).getBuilding().getY();
                        grid.getPixel(x+xmod, y-ymod).getBuilding().setTerrain(- y + ymod + grid.getPixel(x+xmod, y-ymod).getBuilding().getY(), x + xmod - grid.getPixel(x+xmod, y-ymod).getBuilding().getX(), terrain[ymod][xmod]);
                    }

                }
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
    public void setTerrain(int x, int y, Pixel pixel){
        terrain[x][y] = pixel;
    }

    public String getName() {
        return name;
    }
}
