package sandbox.people;

import java.awt.*;
import java.util.HashMap;
import java.util.Set;
import java.util.Map.Entry;
import java.io.Serializable;

import sandbox.*;
import sandbox.pixels.*;

public class Person implements Serializable{

    private static final long serialVersionUID = 9999321236742L;

    //all positions are in units of grid

    private double rootX, rootY;
    private int foot1X, foot1Y, foot2X, foot2Y;
    private int foot1Xgoal = -1;
    private int foot1Ygoal = -1;
    private int foot2Xgoal = -1;
    private int foot2Ygoal = -1;
    private final double maxStep = 4;
    private final double maxStepHeight = 3;
    private final double headR = 2;
    private final double legLen;
    private int direction = -1; //-1 left 1 right
    private Blueprint[] houses = new Blueprint[]{new WoodShack(), new WoodAFrame(), new WoodHouse()};
    private Blueprint house = null;
    private boolean showInv = false;
    private boolean dragged = false;
    private String curActivity = "doing nothing";

    //currently looks for nutrients, tools, and building properties
    HashMap<String, Integer> inventory = new HashMap<>();
    HashMap<String, Integer> desiredResources = new HashMap<>();

    public Person(int x, int y) {
        rootX = (double) x;
        rootY = (double) y;
        foot1X = x - 1;
        foot1Y = y + 2;
        foot2X = x + 1;
        foot2Y = y + 2;
        legLen = Math.sqrt(Math.pow(maxStep, 2)+Math.pow(maxStepHeight, 2));
        this
                .setResource("nutrients", 25)
                .setResource("stone", 0)
                .setResource("wood", 0)
                .setResource("tool", 0)
                .setResource("energy", 100);
        this
                .setDesire("nutrients", 100)
                .setDesire("energy", 100)
                .setDesire("wood", 50)
                .setDesire("stone", 50);
    }

    //valid pixel cannot have fluidity property nor temp above 80
    public void takeNextStep(Grid grid){
        boolean blocked = false;

        int x = -1;
        int y = -1;

        int difference = 0;

        if (direction == -1) {
            if (foot1X > foot2X) {
                x = foot1X;
                y = foot1Y;
                difference = (foot1X-foot2X);
            } else {
                x = foot2X;
                y = foot2Y;
                difference = (foot2X-foot1X);
            }
        } else {
            if (foot1X < foot2X) {
                x = foot1X;
                y = foot1Y;
                difference = (foot2X-foot1X);
            } else {
                x = foot2X;
                y = foot2Y;
                difference = (foot1X-foot2X);
            }
        }

        for (int i = 0; i < maxStep+difference; i++){
            //check if next pixel is background(0)
            if (x+direction >= 0 && x+direction < grid.getWidth() && y < grid.getHeight() && grid.getPixel(x+direction, y).getPropOrDefault(("walkable"), 0) == 0){//DON'T FORGET TO ADD OTHER CONDITIONS LATER
                x += direction;
                blocked = true;
                //scan downwards for a ground(1) pixel
                for (int v = 1; v <= maxStepHeight; v++) {
                    if (y < grid.getHeight() - 1 && grid.getPixel(x, y + 1).getPropOrDefault(("walkable"), 0) == 0) {
                        y += 1;
                    } else if(y < grid.getHeight() - 1 && (grid.getPixel(x, y + 1).getPropOrDefault(("walkable"), 0) == -1 || grid.getPixel(x, y + 1).getPropOrDefault(("temperature"), 50) > 90)) {
                        //too dangerous to walk here(-1 or temp too high)
                        break;
                    } else {
                        blocked = false;
                    }
                }
            }
            //next pixel is ground(1)
            else if (x+direction >= 0 && x+direction < grid.getWidth() && y < grid.getHeight() && grid.getPixel(x+direction, y).getPropOrDefault(("walkable"), 0) == 1){
                blocked = true;
                //scan upwards for a background(0) pixel         
                for (int v = 1; v <= maxStepHeight; v++){
                    if (x+direction >= 0 && x+direction < grid.getWidth() && y >= v && grid.getPixel(x+direction, y-v).getPropOrDefault(("walkable"), 0) == 0){
                        blocked = false;
                        x += direction;
                        y -= v;
                        break;
                    }else if(x+direction >= 0 && x+direction < grid.getWidth() && y >= v && (grid.getPixel(x+direction, y-v).getPropOrDefault(("walkable"), 0) == -1 || grid.getPixel(x, y-v).getPropOrDefault(("temperature"), 50) > 90)){
                        break;
                    }
                }
            }
            else{
                blocked = true;
            }
        }
        if (blocked)
            direction *= -1;
        else{
            if (direction == -1) {
                if (foot1X > foot2X) {
                    foot1Xgoal = x;
                    foot1Ygoal = y;
                } else {
                    foot2Xgoal = x;
                    foot2Ygoal = y;
                }
            } else {
                if (foot1X < foot2X) {
                    foot1Xgoal = x;
                    foot1Ygoal = y;
                } else {
                    foot2Xgoal = x;
                    foot2Ygoal = y;
                }
            }
        }
    }
    public boolean move(Grid grid) {
        if(dragged){
            return false;
        }
        else if (!isStanding(grid)) {
            foot1Y += 1;
            foot2Y += 1;
            foot1Xgoal = -1;
            foot2Xgoal = -1;
            return false;
        } else if (foot1Xgoal != -1 || foot2Xgoal != -1) {
            if (foot1X != foot1Xgoal || foot1Y != foot1Ygoal) {
                if (foot1X != foot1Xgoal) {
                    foot1Y = foot1Ygoal - 1;
                    foot1X += direction;
                } else {
                    foot1Y = foot1Ygoal;
                }
                return true;
            } else if (foot2X != foot2Xgoal || foot2Y != foot2Ygoal) {
                if (foot2X != foot2Xgoal) {
                    foot2Y = foot2Ygoal - 1;
                    foot2X += direction;
                } else {
                    foot2Y = foot2Ygoal;
                }
                return true;
            } else {
                takeNextStep(grid);
                return false;
            }
        } else {
            foot2Xgoal = foot2X;
            foot1Xgoal = foot1X;
            foot1Ygoal = foot1Y;
            foot2Ygoal = foot2Y;
            return false;
        }
    }
    public void update(Grid grid){
        if (craft(grid)){
            this.changeResource("energy", this.getResource("energy")-10);
            curActivity = "crafting";  //move inside craft and describe what it's crafting
        } else if (gather(grid)){
            this.changeResource("energy", this.getResource("energy")-5);
        } else if (eatFood()){
            curActivity = "eating";
        } else if (move(grid)){
            this.changeResource("energy", this.getResource("energy")-1);
            curActivity = "wandering";
        } else{
            curActivity = "doing nothing";
        }

        rootX = (foot1X+foot2X)/2;
        rootY = (foot1Y+foot2Y)/2-5;

        moveHead();
    }
    private void moveHead(){
        rootX = (foot1X+foot2X)/2;
        rootY = foot1Y - legLen;

        /*if (foot1Y - rootY > legLen || foot2Y - rootY > legLen) {
            double l2 = Math.sqrt(Math.pow(foot2X - foot1X, 2) + Math.pow(foot2Y - foot1Y, 2));
            double theta1 = Math.acos((l2 / 2) / legLen);
            double theta2 = Math.atan((foot2Y - foot1Y) / (foot2X - foot1X));
            double deltaX = legLen * Math.cos(theta1 + theta2);
            double deltaY = legLen * Math.sin(theta1 + theta2);
            rootX = foot1X - deltaX;
            rootY = foot1Y - deltaY;
        }*/
    }
    public boolean gather(Grid grid){
        String lookingFor = "";
        if (this.getResource("nutrients") < 100){
            lookingFor = "nutrients";
        }
        else if (this.getResource("wood") < 40){
            lookingFor = "wood";
        }
        else if (this.getResource("stone") < 30){
            lookingFor = "stone";
        }

        int maxGather = 1;
        maxGather += this.getResourceOrDefault("tool", 0);

        boolean hasGathered = false;
        int lookDist = 7;
        int rootAndX = 0;
        int rootAndY = 0;
        for (int x = -lookDist; x < lookDist; x++){
            rootAndX = x + this.getRoot()[0];
            for (int y = -lookDist; y < lookDist; y++){
                rootAndY = y + this.getRoot()[1];
                if (rootAndX >= 0 && rootAndX < grid.getWidth() && rootAndY>= 0 && rootAndY < grid.getHeight() && grid.getPixel(rootAndX, rootAndY).getPropOrDefault(lookingFor, 0) > 0 && maxGather > 0 && grid.getPixel(rootAndX, rootAndY).getPropOrDefault("structure", 0) == 0){
                    maxGather--;
                    this.changeResource(lookingFor, this.getResource(lookingFor)+grid.getPixel(rootAndX, rootAndY).getProperty(lookingFor));
                    grid.setPixel(rootAndX, rootAndY, new Air());
                    curActivity = "gathering "+lookingFor;
                    hasGathered = true;
                }
            }
        }
        return hasGathered;
    }
    public boolean craft(Grid grid){
        if (house == null && this.getResource("wood") >= 20){
            house = houses[0];
            house.build(grid, foot1X, foot1Y+1);
            this.setResource("wood", this.getResource("wood")-20);
        }
        else if (house == houses[0] && this.getResource("wood") >= 40){
            house.destroy(grid);
            houses[1].build(grid, house.getX(), house.getY());
            house = houses[1];
            this.setResource("wood", this.getResource("wood")-40);
        }
        else if (house == houses[1] && this.getResource("wood") >= 40 && this.getResource("stone") >= 30){
            house.destroy(grid);
            houses[2].build(grid, house.getX(), house.getY());
            house = houses[2];
            this.setResource("wood", this.getResource("wood")-40);
            this.setResource("stone", this.getResource("stone")-30);
        }
        else if (this.getResource("wood") >= 10 && this.getResource("stone") > 10 && this.getResourceOrDefault("tool", 0) < 2){
            this.changeResource("tool", 2);
            return true;
        }
        else if (this.getResource("wood") >= 20 && this.getResourceOrDefault("tool", 0) < 1){
            this.changeResource("tool", 1);
            return true;
        }
        return false;
    }
    public boolean eatFood(){
        if (this.getResource("energy") < 100 && this.getResource("nutrients") > 0){
            changeResource("energy", this.getResource("energy")+this.getResource("nutrients"));
            changeResource("nutrients", 0);
            return true;
        }
        else{
            return false;
        }
    }
    public double getLeg1Slope(){
        return (rootY-foot1Y)/(rootX-foot1X);
    }
    public double getLeg2Slope(){
        return (rootY-foot2Y)/(rootX-foot2X);
    }
    public boolean isStanding(Grid grid){
        if (foot1Y + 1 >= grid.getHeight() || (foot1X >= 0 && foot1X < grid.getWidth() && grid.getPixel(foot1X, foot1Y + 1).getPropOrDefault("walkable", 0) == 1)){
            return true;
        }
        if (foot2Y + 1 == grid.getHeight() || foot2X >= 0 && foot2X < grid.getWidth() && grid.getPixel(foot2X, foot2Y + 1).getPropOrDefault("walkable", 0) == 1){
            return true;
        }
        return false;
    }
    public int[] getFoot1(){
        return (new int[]{(int) foot1X, (int) foot1Y});
    }
    public int[] getFoot2(){
        return (new int[]{(int) foot2X, (int) foot2Y});
    }
    public int[] getRoot(){
        return (new int[]{(int) rootX, (int) rootY});
    }
    public double getR(){
        return headR;
    }
    public void setRoot(double x, double y){
        //translate all values
        foot1X += x - rootX;
        foot1Y += y - rootY;
        foot2X += x - rootX;
        foot2Y += y - rootY;
        foot1Xgoal = -1;
        foot1Ygoal = -1;
        foot2Xgoal = -1;
        foot2Ygoal = -1;
        rootX = x;
        rootY = y;
    }
    public void changeResource(String resource, int amount){
        inventory.replace(resource, amount);
    }
    public int getResource(String resource){
        return inventory.get(resource);
    }
    public Set<Entry<String, Integer>> getResources(){
        return inventory.entrySet();
    }
    private Person setResource(String resource, int amount){
        inventory.put(resource, amount);

        //for chaining method calls
        return this;
    }
    private int getResourceOrDefault(String resource, int other){
        return inventory.getOrDefault(resource, other);
    }
    public int getDesire(String resource){
        return desiredResources.get(resource);
    }
    public Set<Entry<String, Integer>> getDesires(){
        return desiredResources.entrySet();
    }
    public int getDesireOrDefault(String resource, int other){
        return desiredResources.getOrDefault(resource, other);
    }
    private Person setDesire(String resource, int amount){
        desiredResources.put(resource, amount);

        //for chaining method calls
        return this;
    }
    public double getHappiness(){
        double happiness = 0;
        int count = 0;
        for (String resource : desiredResources.keySet()) {
            //happiness = square root(have / desired)
            //this allows for a bit of extra happiness if there is surplus
            happiness += Math.sqrt(Math.max(inventory.get(resource) / (double)desiredResources.get(resource), 0));
            count++;
        }
        happiness += Math.sqrt(Math.max((house==null?0:house.comfort) / 100d, 0));
        count++;
        return happiness / count;  //may retun NaN if no desires
    }
    public void setShowInv(boolean showInv){
        this.showInv = showInv;
    }
    public boolean getShowInv(){
        return showInv;
    }
    public void setDragged(boolean dragged){
        this.dragged = dragged;
    }
    public boolean getDragged(){
        return dragged;
    }
    public String getCurActivity(){
        return curActivity;
    }

    public Blueprint getHouse(){
        return house;
    }
}
