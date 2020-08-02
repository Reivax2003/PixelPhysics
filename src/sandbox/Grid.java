package sandbox;

import java.awt.Color;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Random;
import java.util.ArrayList;

import sandbox.pixels.*;
import sandbox.people.*;
import sandbox.goals.*;

public class Grid {
    private final Pixel[][] grid;

    private Random r;

    private Long seed = null;
    private String type = "Random";
    private File loadFrom = null;
    private boolean loaded = false;

    private final int MAX_ENERGY = 1000; //Max energy for display purposes and Default
    private int curMaxEnergy = MAX_ENERGY;
    private int energy = MAX_ENERGY;
    private boolean infiniteEnergy;

    public boolean campaign = false;
    private ArrayList<Goal> goals = new ArrayList<Goal>();
    private String levelName = "Placeholder";

    private ArrayList<Person> people = new ArrayList<Person>();

    private int viewMode = 0;
    public boolean needsRedraw;
    public boolean saving = false;

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

    public void drawPixel(int x, int y, Pixel pixel){
        int cost = pixel.getPropOrDefault("cost", 1);
        energy = (infiniteEnergy) ? 100000000:energy; // If Inf set to a very high number
        if(cost <= energy){
            if(pixel.getType().equals("electricity") && this.grid[x][y].hasProperty("conductive")) {
                this.grid[x][y].setState("conducting", 1);
                energy -= cost;
            }
            else if(!this.grid[x][y].getType().equals(pixel.getType())){
                this.grid[x][y] = pixel;
                energy -= cost;
            }
        }
        energy = (infiniteEnergy) ? MAX_ENERGY:energy; // If Inf keep at max display energy
    }

    //draws a line of pixels on the grid
    public void drawLine(int x1, int y1, int x2, int y2, Pixel pixel) {
        double dirX = x2 - x1, dirY = y2 - y1;
        double length = Math.sqrt(dirX * dirX + dirY * dirY);
        dirX /= length;
        dirY /= length;
        int cost = pixel.getPropOrDefault("cost", 1);
        energy = (infiniteEnergy) ? 100000000:energy; // If Inf set to a very high number

        for (double i = 0; i < length && cost <= energy; i++) {
            int x = (int) (x1 + dirX * i);
            int y = (int) (y1 + dirY * i);
            if(pixel.getType().equals("electricity") && this.grid[x][y].hasProperty("conductive")) {
                this.grid[x][y].setState("conducting", 1);
                energy -= cost;
            }
            else if(!this.grid[x][y].getType().equals(pixel.getType())){
                Pixel p = pixel.duplicate();
                this.grid[x][y] = p;
                energy -= cost;
            }
        }
        energy = (infiniteEnergy) ? MAX_ENERGY:energy; // If Inf keep at max display energy
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

    public void clearGrid() { // This specific pair is used enough to have its own method
        fillGrid(new Air());
        energy = curMaxEnergy;
        people.clear();
        goals.clear();
        needsRedraw = true;
    }

    public void saveGrid(File file) {
        saveGrid(file, false);
    }

    public void saveGrid(File file, boolean toCampaign){
        saving = true;
        File directory = file.getParentFile();
        if(!directory.exists()){
            directory.mkdirs();
        }
        try(ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(file))){
            //iterate through each pixel
            for (int x = 0; x < getWidth(); x++) {
                for (int y = 0; y < getHeight(); y++) {
                    out.writeObject(grid[x][y]);
                }
            }
            out.writeInt(energy);  //save grid's energy
            out.writeInt((infiniteEnergy)? -1:curMaxEnergy); // Saves max energy or infinite (as -1)

            out.writeInt(people.size()); //Number of people for loader
            for (Person each : people){
                out.writeObject(each);
            }

            out.writeBoolean((toCampaign)? true : campaign); //Whether a campaign file

            if((toCampaign)? true : campaign) {
                out.writeInt(goals.size()); //Number of goals for loader
                for (Goal each : goals){
                    out.writeObject(each);
                }
            }

            out.close();
        } catch (Exception e){
            System.out.println("An error occured while saving grid.");
            e.printStackTrace();
        }
        saving = false;
    }

    public void loadGrid(File file){
        people.clear();
        goals.clear();
        try(ObjectInputStream in = new ObjectInputStream(new FileInputStream(file))){
            //iterate through each pixel
            for (int x = 0; x < getWidth(); x++) {
                for (int y = 0; y < getHeight(); y++) {
                    Pixel temp = (Pixel)in.readObject();
                    grid[x][y] = temp;
                }
            }
            energy = in.readInt();  //energy
            curMaxEnergy = in.readInt(); // Max energy
            if (curMaxEnergy == -1) { // If written value is -1 set infinite energy
                setInfEnergy();
            }

            int numPeople = in.readInt(); // Number of people to read
            //Needs changing if person types with differing methods are made probably by getting each type in sequence
            for (int p = 0; p < numPeople; p++) {
                people.add((Person)in.readObject());
            }

            campaign = in.readBoolean();

            if(campaign) {
                int numGoals = in.readInt(); // Number of goals to read
                for (int p = 0; p < numPeople; p++) {
                    goals.add((Goal)in.readObject());
                }
            }

            in.close();
            loadFrom = file;
            loaded = true;
            needsRedraw = true;
        } catch (Exception e){
            System.out.println("An error occured while loading grid.");
            e.printStackTrace();
        }
    }
    public void worldGen(long seed, String worldType){
        this.seed = seed;
        r = new Random(seed);
        clearGrid(); // Assumes world gen replaces old world if any and clears people
        r.nextDouble();
        double world = r.nextDouble();
        type = worldType;
        if (worldType == "Earth") {world = 0;}
        else if (worldType == "Alien") {world = 1;}
        if(world < 0.5){
            //earth-like terrain
            genLand(new Soil(), new Grass(), this.getHeight()/5, 1);
            genLake();
            genPlant(new Plant(), new Soil(), new Water(), 0.3);
            genPlant(new Plant3(), new Soil(), new Water(), 0.03);
        }
        else{
            //alien planet
            Pixel land = new Stone();
            land.setColor(new Color(186, 85, 211));
            Pixel cover = new Soil();
            cover.setColor(new Color(139,0,139));
            genLand(land, cover, this.getHeight()/4, 1);
            genMountain(land, r.nextInt(10) + this.getHeight()*3/4, r.nextInt(20)+10, r.nextInt(20)+10);
            genPlant(new AlienPlant(true), new Soil(), new Stone(), 0.03);
        }
        loaded = false;

        // Test person
        people.add(new Person(50, 25));
        // Test goal
        goals.add(new MaxHouse(new WoodAFrame()));
    }
    public void genLand(Pixel land, Pixel cover, int maxHeight, int minHeight){
        int plus = 15;
        int minus = 15;

        int yChange = (maxHeight-minHeight);

        int yCoord = (int) (r.nextDouble()*yChange+minHeight);


        for (int x = 0; x < grid.length; x++){

            this.setPixel(x, (this.getHeight()-1)-yCoord, cover.duplicate());
            for (int i = yCoord-1; i >= 0; i--){
                this.setPixel(x, (this.getHeight()-1)-i, land.duplicate());
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
    public void genLake() {
        double center = (r.nextDouble() * this.getWidth() / 1.2);
        double width = (r.nextDouble() * (this.getWidth() / 2.0));
        double startHeight = 0;

        for (int i = this.getHeight() - 1; i >= 0; i--) {
            if (this.getPixel((int) center, i).type.equals("grass")) {
                startHeight = i;
                break;
            }
        }
        double depth = (r.nextDouble() * (this.getHeight() - startHeight));

        for (int x = 0; x < this.getWidth(); x++) {
            for (int y = this.getHeight() - 1; y >= 0; y--) {
                if (x < this.getWidth() && y < this.getHeight()) {
                    double isLake = startHeight + Math.sqrt(Math.pow(width / 2, 2) - Math.pow(x - center, 2)) / ((width / 2) / depth);
                    if (y < isLake && y > startHeight) {
                        this.setPixel(x, y, new WetSand());
                    } else if (y < isLake) {
                        this.setPixel(x, y, new Air());
                    } else if (y > isLake) {
                        this.setPixel(x, y, new Soil());
                    }
                }
            }
        }
        width -= 2;
        depth -= 1;
        for (int x = 0; x < this.getWidth(); x++) {
            for (int y = this.getHeight() - 1; y >= 0; y--) {
                if (x < this.getWidth() && y < this.getHeight()) {
                    boolean isLake = y < startHeight + Math.sqrt(Math.pow(width / 2, 2) - Math.pow(x - center, 2)) / ((width / 2) / depth);
                    if (isLake && y > startHeight) {
                        this.setPixel(x, y, new Water());
                    }
                }
            }
        }
    }
    public void genPlant(Pixel plant, Pixel soilType, Pixel cantPlace, double frequency){
        //adds plants randomly at the frequency (0-1)
        for (int x = 0; x < grid.length; x++) {
            if(r.nextDouble() < frequency)
                for (int y = 0; y < grid[0].length-1; y++) {
                    if(this.getPixel(x, y+1).getType().equals(soilType.getType())){
                        setPixel(x, y, plant.duplicate());
                        break;
                    }
                    else if(this.getPixel(x, y+1).getType().equals(cantPlace.getType())){
                        break;  //only place plants if it isn't covered
                    }
                }
        }
    }
    public void genMountain(Pixel type, int height, double lengthR, double lengthL){
        double center = r.nextDouble() * this.getWidth();
        double slopeL = lengthL/height;
        double slopeR = lengthR/height;

        for (int y = 0; y < height && y < this.getHeight(); y++) {
            for (int x = (int)(center-lengthL); x < center+lengthR; x++) {
                if(x >= 0 && x < this.getWidth())
                this.setPixel(x, this.getHeight()-y-1, type.duplicate());
            }
            lengthL-=slopeL*r.nextDouble()*((double)y/height*4);
            lengthR-=slopeR*r.nextDouble()*((double)y/height*4);
        }
    }

    public int getView() {
        return viewMode;
    }

    public void setView(int viewMode) {
        this.viewMode = viewMode;
        needsRedraw = true;
    }

    public void reloadGrid() {
        if (loaded) { // Reload if loaded from save
            loadGrid(loadFrom);
        }
        else {
            if (seed != null) { // If a seed does exist use that
                worldGen(seed, type);
            }
            else { // Else reset grid
                clearGrid();
            }
        }
    }

    public String[] getInfo() {
        if (!campaign) { // If a creative world
            if (loaded) { // Return filepath
                return new String[] {"1",loadFrom.getPath()};
            }
            else {
                if (seed != null) { // Return seed
                      return new String[] {"2",String.valueOf(seed) + " (Type " + type +")"};
                }
                else { // Return code signaling it started clear
                    return new String[] {"0",""};
                }
            }
        }
        else { // Return level info
            String[] levelInfo = new String[goals.size() + 2];
            levelInfo[0] = "3"; //Code for mission
            levelInfo[1] = levelName;
            for(int i = 0; i < goals.size(); i ++) {
                levelInfo[i+2] = goals.get(i).getInfo();
            }
            return levelInfo;
        }
    }

    public void setInfEnergy() {
        infiniteEnergy = true;
        energy = MAX_ENERGY;
        curMaxEnergy = MAX_ENERGY;
        needsRedraw = true;
    }

    public boolean changeEnergy(int energyChange) { // Return success of change
        if(infiniteEnergy) { // Always successful if infinite
          energy = MAX_ENERGY;
          return true;
        }
        if((energy + energyChange) < 0){ // Checks if goes below 0, does not change
            return false;
        }
        if((energy += energyChange) > curMaxEnergy){ // Does energy change here
            energy = curMaxEnergy; // Prevents going over max energy
        }
        return true;
    }

    public int getEnergy() {
        return energy;
    }
    public void setEnergy(int energy) {
        if(energy > curMaxEnergy || infiniteEnergy) {
            this.energy = curMaxEnergy;
        }
        else {
            this.energy = energy;
        }
    }

    public int getMaxEnergy() {
        return curMaxEnergy;
    }

    public int getDisplayMaxEnergy() {
        return (curMaxEnergy > MAX_ENERGY)?  MAX_ENERGY : curMaxEnergy; // If curMax is greater than the display/default value send that instead
    }

    public void setMaxEnergy(int newMax) {
        curMaxEnergy = newMax;
    }

    public ArrayList<Person> getPeople() {
        return people;
    }

    public ArrayList<Goal> getGoals() {
        return goals;
    }
}
