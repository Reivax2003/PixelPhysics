package sandbox;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Random;

import sandbox.pixels.*;

public class Grid {
    private final Pixel[][] grid;
    private Random r;
    private int viewMode = 0;
    public final int MAX_ENERGY = 1000;
    public int energy = MAX_ENERGY;

    public Grid(int width, int height) {
        grid = new Pixel[width][height];
    }

    public Pixel getPixel(int x, int y) {
        return grid[x][y];
    }

    public void setPixel(int x, int y, Pixel pixel) {
        this.grid[x][y] = pixel;
    }

    public int getWidth() {
        return grid.length;
    }

    public int getHeight() {
        return grid[0].length;
    }

    public Pixel getPixelLeft(int x, int y) {
        return grid[x - 1][y];
    }

    public Pixel getPixelRight(int x, int y) {
        return grid[x + 1][y];
    }

    public Pixel getPixelUp(int x, int y) {
        return grid[x][y - 1];
    }

    public Pixel getPixelDown(int x, int y) {
        return grid[x][y + 1];
    }

    public void swapPositions(int x1, int y1, int x2, int y2) {
        if (x1 == x2 && y1 == y2) {
            return;
        }

        Pixel position1 = grid[x1][y1];
        Pixel position2 = grid[x2][y2];

        position1.setMoved(true);
        position2.setMoved(true);

        grid[x1][y1] = position2;
        grid[x2][y2] = position1;
    }

    public int drawPixel(int x, int y, Pixel pixel, int energy){
        int cost = pixel.getPropOrDefault("cost", 1);
        if(cost <= energy){
            if(pixel.getType().equals("electricity") && this.grid[x][y].hasProperty("conductive")) {
                this.grid[x][y].setState("conducting", 1);
                energy -= cost;
            }
            else if(cost <= energy && !this.grid[x][y].getType().equals(pixel.getType())){
                this.grid[x][y] = pixel;
                energy -= cost;
            }
        }
        return energy;
    }

    //draws a line of pixels on the grid
    public int drawLine(int x1, int y1, int x2, int y2, Pixel pixel, int energy) {
        double dirX = x2 - x1, dirY = y2 - y1;
        double length = Math.sqrt(dirX * dirX + dirY * dirY);
        dirX /= length;
        dirY /= length;
        int cost = pixel.getPropOrDefault("cost", 1);

        for (double i = 0; i < length && cost <= energy; i++) {
            int x = (int) (x1 + dirX * i);
            int y = (int) (y1 + dirY * i);
            if(pixel.getType().equals("electricity") && this.grid[x][y].hasProperty("conductive")) {
                this.grid[x][y].setState("conducting", 1);
                energy -= cost;
                continue;
            }
            else if(!this.grid[x][y].getType().equals(pixel.getType())){
                Pixel p = pixel.duplicate();
                this.grid[x][y] = p;
                energy -= cost;
            }
        }
        return energy;
    }

    // Fills the grid with a pixel type
    public void fillGrid(Pixel pixel){
        for (int x = 0; x < this.getWidth(); x++) {
            for (int y = 0; y < this.getHeight(); y++) {
                Pixel p = pixel.duplicate();
                this.grid[x][y] = p;
            }
        }
    }

    public void saveGrid(File file){
        try(ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(file))){
            //iterate through each pixel
            for (int x = 0; x < getWidth(); x++) {
                for (int y = 0; y < getHeight(); y++) {
                    out.writeObject(grid[x][y]);
                }
            }
            out.writeInt(energy);  //save grid's energy
            out.close();
        } catch (Exception e){
            System.out.println("An error occured while saving grid.");
        }
    }

    public void loadGrid(File file){
        try(ObjectInputStream in = new ObjectInputStream(new FileInputStream(file))){
            //iterate through each pixel
            for (int x = 0; x < getWidth(); x++) {
                for (int y = 0; y < getHeight(); y++) {
                    Pixel temp = (Pixel)in.readObject();
                    grid[x][y] = temp;
                }
            }
            energy = in.readInt();  //energy 
            in.close();
        } catch (Exception e){
            System.out.println("An error occured while loading grid.");
        }
    }
    public void worldGen(long seed){
        r = new Random(seed);
        genDirt(this.getHeight()/5, 1);
        genLake();
    }
    public void genDirt(int maxHeight, int minHeight){
        int plus = 15;
        int minus = 15;

        int yChange = (maxHeight-minHeight);

        int yCoord = (int) (r.nextDouble()*yChange+minHeight);


        for (int x = 0; x < grid.length; x++){

            this.setPixel(x, (this.getHeight()-1)-yCoord, new Grass());
            for (int i = yCoord-1; i >= 0; i--){
                this.setPixel(x, (this.getHeight()-1)-i, new Soil());
            }

            if (r.nextDouble()*100 < plus && yCoord < maxHeight){
                yCoord++;
                plus--;
                minus++;
            }
            else if (r.nextDouble()*(100-plus) < minus && yCoord > minHeight){
                yCoord--;
                plus++;
                minus--;
            }
        }
    }
    public void genLake(){
        double center = (r.nextDouble()*this.getWidth()/1.2)+(this.getWidth()*(11/12));
        double width = (r.nextDouble()*(this.getWidth()/2));
        double startHeight = 0;

        for (int i = this.getHeight()-1; i >= 0; i--){
            if (this.getPixel((int) center, i).type.equals("grass")){
                startHeight = i;
                break;
            }
        }
        double depth = (r.nextDouble()*(this.getHeight()-startHeight));

        for (int x = 0; x < this.getWidth(); x++){
            for (int y = this.getHeight()-1; y >= 0; y--){
                if (x >= 0 && x < this.getWidth() && y >= 0 && y < this.getHeight()){
                    double isLake = startHeight+Math.sqrt(Math.pow(width/2, 2)-Math.pow(x-center, 2))/((width/2)/depth);
                    if (y < isLake && y > startHeight){
                        this.setPixel(x, y, new WetSand());
                    }
                    else if (y < isLake){
                        this.setPixel(x, y, new Air());
                    }
                    else if (y > isLake){
                        this.setPixel(x, y, new Soil());
                    }
                }
            }
        }
        width -= 2;
        depth -= 1;
        for (int x = 0; x < this.getWidth(); x++){
            for (int y = this.getHeight()-1; y >= 0; y--){
                if (x >= 0 && x < this.getWidth() && y >= 0 && y < this.getHeight()){
                    boolean isLake = y < startHeight+Math.sqrt(Math.pow(width/2, 2)-Math.pow(x-center, 2))/((width/2)/depth);
                    if (isLake && y > startHeight){
                        this.setPixel(x, y, new Water());
                    }
                }
            }
        }

    public int getView() {
        return viewMode;
    }

    public void setView(int viewMode) {
        this.viewMode = viewMode;
    }
}
