package sandbox.people;

import sandbox.Grid;
import sandbox.pixels.Air;

import java.util.HashMap;

public class Person {
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
    private final double legLen = 2*Math.sqrt(13);
    private int direction = -1; //-1 left 1 right

    //currently looks for nutrients, tools, and building properties
    HashMap<String, Integer> inventory = new HashMap<>();

    public Person(int x, int y) {
        rootX = (double) x;
        rootY = (double) y;
        foot1X = x - 1;
        foot1Y = y + 2;
        foot2X = x + 1;
        foot2Y = y + 2;
        this
                .setResource("nutrients", 25)
                .setResource("stone", 0)
                .setResource("wood", 0)
                .setResource("tool", 0)
                .setResource("energy", 100);
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
            if (x+direction >= 0 && x+direction < grid.getWidth() && grid.getPixel(x+direction, y).getPropOrDefault(("density"), 100) < 20){//DON'T FORGET TO ADD OTHER CONDITIONS LATER
                x += direction;
                blocked = true;
                for (int v = 1; v <= maxStepHeight; v++) {
                    if (y < grid.getHeight() - 1 && grid.getPixel(x, y + 1).getPropOrDefault(("density"), 100) < 20) {
                        y += 1;
                    }
                    else{
                        blocked = false;
                    }
                }
            }
            else if (x+direction >= 0 && x+direction < grid.getWidth()){
                blocked = true;
                for (int v = 1; v <= maxStepHeight; v++){
                    if (x+direction >= 0 && x+direction < grid.getWidth() && grid.getPixel(x+direction, y-v).getPropOrDefault(("density"), 100) < 20){
                        blocked = false;
                        x += direction;
                        y -= v;
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
        if (!isStanding(grid)) {
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
        if (craft()){
            this.changeResource("energy", this.getResource("energy")-10);
        } else if (gather(grid)){
            this.changeResource("energy", this.getResource("energy")-5);
        } else if (eatFood()){
        } else if (move(grid)){
            this.changeResource("energy", this.getResource("energy")-1);
        }

        rootX = (foot1X+foot2X)/2;
        rootY = (foot1Y+foot2Y)/2-5;
        /*double feetCenterX = (foot1X+foot2X)/2;
        double feetCenterY = (foot1Y+foot2Y)/2;

        double spread = Math.sqrt(Math.pow(feetCenterY, 2)+Math.pow(feetCenterX, 2));
        double angle = Math.acos((spread/2)/legLen);
        System.out.println(angle);

        double displacement = legLen*Math.sin(angle);

        double slope1 = 0;
        if (foot1X != foot2X)
            slope1 = (foot1Y-foot2Y)/(foot1X-foot2X);

        double slope2 = -1/slope1;

        double deltaY = -(1/slope2)+Math.sqrt(Math.pow(1/slope2, 2) + 4*Math.pow(displacement, 2));

        if (deltaY > 0){
            deltaY = -(1/slope2)+Math.sqrt(Math.pow(1/slope2, 2) - 4*Math.pow(displacement, 2));
        }

        deltaY /= 2;

        double deltaX = deltaY/slope2;

        rootX = feetCenterX+deltaX;
        rootY = feetCenterY+deltaY;*/
    }
    public boolean gather(Grid grid){
        String lookingFor = "";
        if (this.getResource("nutrients") < 100){
            lookingFor = "nutrients";
        }
        else if (this.getResource("wood") < 20){
            lookingFor = "wood";
        }
        else if (this.getResource("stone") < 20){
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
                if (rootAndX >= 0 && rootAndX < grid.getWidth() && rootAndY>= 0 && rootAndY < grid.getHeight() && grid.getPixel(rootAndX, rootAndY).getPropOrDefault(lookingFor, 0) > 0 && maxGather > 0){
                    maxGather--;
                    this.changeResource(lookingFor, this.getResource(lookingFor)+grid.getPixel(rootAndX, rootAndY).getProperty(lookingFor));
                    grid.setPixel(rootAndX, rootAndY, new Air());
                    hasGathered = true;
                }
            }
        }
        return hasGathered;
    }
    public boolean craft(){
        if (this.getResource("wood") >= 10 && this.getResource("stone") > 10 && this.getResourceOrDefault("tool", 0) < 2){
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
        if (foot1Y + 1 == grid.getHeight() || grid.getPixel(foot1X, foot1Y + 1).getPropOrDefault("density", 100) > 20){
            return true;
        }
        if (foot2Y + 1 == grid.getHeight() || grid.getPixel(foot2X, foot2Y + 1).getPropOrDefault("density", 100) > 20){
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
        foot1X -= rootX;
        foot1X += x;
        foot1Y -= rootY;
        foot1Y += y;
        foot2X -= rootX;
        foot2X += x;
        foot2Y -= rootY;
        foot2Y += y;
        rootX = x;
        rootY = y;
    }
    public void changeResource(String resource, int amount){
        inventory.replace(resource, amount);
    }
    public int getResource(String resource){
        return inventory.get(resource);
    }
    private Person setResource(String resource, int amount){
        inventory.put(resource, amount);

        //for chaining method calls
        return this;
    }
    private int getResourceOrDefault(String resource, int other){
        return inventory.getOrDefault(resource, other);
    }
}
