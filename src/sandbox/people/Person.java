package sandbox.people;

import sandbox.Grid;
import sandbox.pixels.Air;
import sandbox.PeopleManager;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map.Entry;
import java.util.Set;

public class Person implements Serializable {

    private static final long serialVersionUID = 9999321236742L;

    //all positions are in units of grid

    private double rootX, rootY;
    private int foot1X, foot1Y, foot2X, foot2Y;
    private double knee1X, knee1Y, knee2X, knee2Y;
    private int foot1Xgoal = -1;
    private int foot1Ygoal = -1;
    private int foot2Xgoal = -1;
    private int foot2Ygoal = -1;
    private final double maxStep = 4;
    private final double maxStepHeight = 3;
    private final double headRadius = 2;
    private final double legLen;
    private final double height = 5;
    private boolean isWalking = true;
    private int direction = -1; //-1 left 1 right
    private Blueprint[] houses = new Blueprint[]{new WoodShack(), new WoodAFrame(), new WoodHouse()};
    private Blueprint house = null;
    private Blueprint[] structures = new Blueprint[]{new FirePit(), new Garden(), new Well()};
    private boolean[] structureBuilt = new boolean[]{false, false, false}; //correlates to structures to know if they're built
    private boolean showInventory = false;
    private boolean dragged = false;
    private String currentActivity = "doing nothing";
    public String[] job = {};

    //currently looks for nutrients, tools, and building properties
    HashMap<String, Integer> inventory = new HashMap<>();
    HashMap<String, Integer> desiredResources = new HashMap<>();
    HashMap<String, Integer> structureNumbers = new HashMap<>();

    public Person(int x, int y, String[] job) {
        rootX = x;
        rootY = y;
        foot1X = x - 1;
        foot1Y = y + 2;
        foot2X = x + 1;
        foot2Y = y + 2;
        legLen = Math.sqrt(Math.pow(height, 2)+Math.pow(maxStep/2, 2));
        direction = -1;
        if (Math.random() < 0.5){
            direction = 1;
        }
        this
                .setResource("nutrients", 25)
                .setResource("stone", 0)
                .setResource("wood", 0)
                .setResource("tool", 0)
                .setResource("energy", 100)
                .setDesire("nutrients", 100)
                .setDesire("energy", 100)
                .setDesire("wood", 20)
                .setDesire("stone", 0);

        this.job = job;
        if(job == null || job.length == 0){
            job = new String[]{"none"};
        }
        if (job[0].equals("gatherer") && desiredResources.containsKey(job[1])) {
            setDesire(job[1], 200);
        }
        structureNumbers.put("Well", 0);
        structureNumbers.put("Fire Pit", 0);
        structureNumbers.put("Garden", 0);
    }

    //valid pixel cannot have fluidity property nor temp above 80
    public void takeNextStep(Grid grid) {
        boolean blocked = false;

        int x = -1;
        int y = -1;

        int difference = 0;

        if (direction == -1) {
            if (foot1X > foot2X) {
                x = foot1X;
                y = foot1Y;
                difference = (foot1X - foot2X);
            } else {
                x = foot2X;
                y = foot2Y;
                difference = (foot2X - foot1X);
            }
        } else {
            if (foot1X < foot2X) {
                x = foot1X;
                y = foot1Y;
                difference = (foot2X - foot1X);
            } else {
                x = foot2X;
                y = foot2Y;
                difference = (foot1X - foot2X);
            }
        }

        for (int i = 0; i < maxStep + difference; i++) {
            //check if next pixel is background(0)
            if (x + direction >= 0 && x + direction < grid.getWidth() && y < grid.getHeight() && grid.getPixel(x + direction, y).getPropOrDefault(("walkable"), 0) == 0) {//DON'T FORGET TO ADD OTHER CONDITIONS LATER
                x += direction;
                //scan upwards for large enough space to walk, also check for water so the humans don't drown
                for (int v = 1; v <= height+3; v++) {
                    if (y-v > 0 && (grid.getPixel(x, y-v).getPropOrDefault(("walkable"), 0) != 0 || v >= height && grid.getPixel(x, y-v).getType().equals("water"))){
                        blocked = true;
                        break;
                    }
                }
                if (!blocked) {
                    blocked = true;
                    //scan downwards for a ground(1) pixel
                    for (int v = 1; v <= maxStepHeight; v++) {
                        if (y < grid.getHeight() - 1 && grid.getPixel(x, y + 1).getPropOrDefault(("walkable"), 0) == 0) {
                            y += 1;
                        } else if (y < grid.getHeight() - 1 && (grid.getPixel(x, y + 1).getPropOrDefault(("walkable"), 0) == -1 || grid.getPixel(x, y + 1).getPropOrDefault(("temperature"), 50) > 90)) {
                            //too dangerous to walk here(-1 or temp too high)
                            break;
                        } else {
                            blocked = false;
                        }
                    }
                }
            }
            //next pixel is ground(1)
            else if (x + direction >= 0 && x + direction < grid.getWidth() && y < grid.getHeight() && grid.getPixel(x + direction, y).getPropOrDefault(("walkable"), 0) == 1) {
                blocked = true;
                //scan upwards for a background(0) pixel
                for (int v = 1; v <= maxStepHeight; v++) {
                    if (x + direction >= 0 && x + direction < grid.getWidth() && y >= v && grid.getPixel(x + direction, y - v).getPropOrDefault(("walkable"), 0) == 0) {
                        blocked = false;
                        x += direction;
                        y -= v;
                        break;
                    } else if (x + direction >= 0 && x + direction < grid.getWidth() && y >= v && (grid.getPixel(x + direction, y - v).getPropOrDefault(("walkable"), 0) == -1 || grid.getPixel(x, y - v).getPropOrDefault(("temperature"), 50) > 90)) {
                        break;
                    }
                }
            } else {
                blocked = true;
            }
        }
        if (blocked)
            direction *= -1;
        else {
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
        if (dragged) {
            return false;
        } else if (!isStanding(grid)) {
            return false;
        } else if (foot1Xgoal != -1 || foot2Xgoal != -1) {
            if (foot1X != foot1Xgoal || foot1Y != foot1Ygoal) {
                if (foot1X != foot1Xgoal) {
                    foot1Y = foot1Ygoal - 1;
                    direction = (foot1Xgoal-foot1X)/Math.abs(foot1Xgoal-foot1X);
                    foot1X += direction;
                } else {
                    foot1Y = foot1Ygoal;
                }
                return true;
            } else if (foot2X != foot2Xgoal || foot2Y != foot2Ygoal) {
                if (foot2X != foot2Xgoal) {
                    foot2Y = foot2Ygoal - 1;
                    direction = (foot2Xgoal-foot2X)/Math.abs(foot2Xgoal-foot2X);
                    foot2X += direction;
                } else {
                    foot2Y = foot2Ygoal;
                }
                return true;
            } else {
                takeNextStep(grid);
                return true;
            }
        } else {
            foot2Xgoal = foot2X;
            foot1Xgoal = foot1X;
            foot1Ygoal = foot1Y;
            foot2Ygoal = foot2Y;
            return false;
        }
    }

    public void update(Grid grid, PeopleManager peopleManager) {
        this.checkDesires(peopleManager);

        if (craft(grid, peopleManager)) {
            this.changeResource("energy", this.getResource("energy") - 10);
            currentActivity = "crafting";  //move inside craft and describe what it's crafting
        } else if (gather(grid)) {
            this.changeResource("energy", this.getResource("energy") - 5);
        } else if (eatFood()) {
            currentActivity = "eating";
        } else if (isWalking && move(grid)) {
            this.changeResource("energy", this.getResource("energy") - 1);
            currentActivity = "wandering";
        } else {
            currentActivity = "doing nothing";
        }

        if (!isStanding(grid) && !dragged) {
            foot1Y += 1;
            foot2Y += 1;
            foot1Xgoal = -1;
            foot2Xgoal = -1;
        }

        if (Math.random() < 0.01) {
            isWalking = !isWalking;
            if (Math.random() < 0.5){
                direction *= -1;
            }
        }

        rootX = (foot1X + foot2X) / 2;
        rootY = (foot1Y + foot2Y) / 2 - 5;

        moveHead();
    }
    public void checkDesires(PeopleManager peopleManager){
        setDesire("wood", 0).setDesire("stone", 0);
        if (house == null)
            setDesire("wood", 20);
        else if (house == houses[0])
            setDesire("wood", 40);
        else if (house == houses[1])
            this.setDesire("wood", 40).setDesire("stone", 30);
        if (house != null && !structureBuilt[0] && peopleManager.belowMaxStruct("Fire Pit"))
            this.setDesire("wood", getDesire("wood")+5).setDesire("stone", getDesire("stone")+5);
        else if (house != null && house != houses[0] && !structureBuilt[1] && peopleManager.belowMaxStruct("Garden"))
            this.setDesire("wood", getDesire("wood")+20);
        else if (house != null && house != houses[0] && house != houses[1] && !structureBuilt[2] && peopleManager.belowMaxStruct("Well"))
            this.setDesire("wood", getDesire("wood")+10).setDesire("stone", getDesire("stone")+25);
    }
    public void moveHead(){
        rootX = (foot1X+foot2X)/2;
        rootY = foot1Y - height;

        //if (foot1Y - rootY > legLen || foot2Y - rootY > legLen) {
        double l2 = Math.sqrt(Math.pow(rootX - foot1X, 2) + Math.pow(rootY - foot1Y, 2))/2;
        double l3 = Math.sqrt(Math.pow(legLen/2, 2)-Math.pow(l2, 2));
        double c = Math.sqrt(Math.pow(rootX - foot1X, 2) + Math.pow(foot1Y - rootY, 2));
        double deltaX = (l3/c)*(foot1Y - rootY);
        double deltaY = (l3/c)*(rootX - foot1X);
        if (direction == -1) {
            knee1X = (rootX + foot1X) / 2 - deltaX;
            knee1Y = (rootY + foot1Y) / 2 - deltaY;
        }
        else{
            knee1X = (rootX + foot1X) / 2 + deltaX;
            knee1Y = (rootY + foot1Y) / 2 + deltaY;
        }

        // if(Double.isNaN(knee1X) || Double.isNaN(knee1Y)){
        //     System.out.println("knee1 NaN");
        // }

        l2 = Math.sqrt(Math.pow(rootX - foot2X, 2) + Math.pow(rootY - foot2Y, 2))/2;
        l3 = Math.sqrt(Math.pow(legLen/2, 2)-Math.pow(l2, 2));
        if (legLen/2 > l2) {
            c = Math.sqrt(Math.pow(rootX - foot2X, 2) + Math.pow(foot2Y - rootY, 2));
            if(c == 0){
                c+=0.1;
            }
            deltaX = (l3 / c) * (foot2Y - rootY);
            deltaY = (l3 / c) * (rootX - foot2X);
        }
        if (direction == -1) {
            knee2X = (rootX + foot2X) / 2 - deltaX;
            knee2Y = (rootY + foot2Y) / 2 - deltaY;
        }
        else {
            knee2X = (rootX + foot2X) / 2 + deltaX;
            knee2Y = (rootY + foot2Y) / 2 + deltaY;
        }

        if(Double.isNaN(knee2X) || Double.isNaN(knee2Y)){
            System.out.println("knee2 NaN");
        }
    }

    public boolean gather(Grid grid) {
        String lookingFor = "";
        if (this.getResource("nutrients") < getDesire("nutrients")) {
            lookingFor = "nutrients";
        } else if (Math.random() < 0.5 && this.getResource("wood") < getDesire("wood")) {
            lookingFor = "wood";
        } else if (this.getResource("stone") < getDesire("stone")) {
            lookingFor = "stone";
        } else if (this.getResource("wood") < getDesire("wood")){
            lookingFor = "wood";
        }

        int maxGather = 1;
        maxGather += this.getResourceOrDefault("tool", 0);

        boolean hasGathered = false;
        int lookDist = 7;
        int rootAndX = 0;
        int rootAndY = 0;
        for (int x = -lookDist; x < lookDist; x++) {
            rootAndX = x + this.getRoot()[0];
            for (int y = -lookDist; y < lookDist; y++) {
                rootAndY = y + this.getRoot()[1];
                if (rootAndX >= 0 && rootAndX < grid.getWidth() && rootAndY >= 0 && rootAndY < grid.getHeight() && grid.getPixel(rootAndX, rootAndY).getPropOrDefault(lookingFor, 0) > 0 && maxGather > 0 && grid.getPixel(rootAndX, rootAndY).getPropOrDefault("structure", 0) == 0) {
                    maxGather--;
                    this.changeResource(lookingFor, this.getResource(lookingFor) + grid.getPixel(rootAndX, rootAndY).getProperty(lookingFor));
                    grid.setPixel(rootAndX, rootAndY, new Air());
                    currentActivity = "gathering " + lookingFor;
                    hasGathered = true;
                }
            }
        }
        return hasGathered;
    }

    public boolean craft(Grid grid, PeopleManager peopleManager) {
        if (house == null && this.getResource("wood") >= 20) {
            if(houses[0].tryBuild(grid, foot1X, foot1Y+1)){
                house = houses[0];
                this.changeResource("wood", this.getResource("wood")-20);
            }
        }
        else if (house != null && this.getResource("wood") >= 5 && this.getResource("stone") >= 5 && peopleManager.belowMaxStruct("Fire Pit")){
            if(structures[0].tryBuild(grid, foot1X, foot1Y+1)){
                this.changeResource("wood", this.getResource("wood")-5);
                this.changeResource("stone", this.getResource("stone")-5);
                addStructure("Fire Pit");
                structureBuilt[0] = true;
            }
        }
        else if (house == houses[0] && this.getResource("wood") >= 40){
            house.destroy(grid);
            houses[1].build(grid, house.getX(), house.getY());
            house = houses[1];
            this.changeResource("wood", this.getResource("wood")-40);
            
        }
        else if (house != null && house != houses[0] && this.getResource("wood") >= 20 && peopleManager.belowMaxStruct("Garden")){
            if(structures[1].tryBuild(grid, foot1X, foot1Y+1)){
                this.changeResource("wood", this.getResource("wood")-20);
                addStructure("Garden");
                structureBuilt[1] = true;
            }
        }
        else if (house == houses[1] && this.getResource("wood") >= 40 && this.getResource("stone") >= 30){
            house.destroy(grid);
            houses[2].build(grid, house.getX(), house.getY());
            house = houses[2];
            this.changeResource("wood", this.getResource("wood")-40);
            this.changeResource("stone", this.getResource("stone")-30);
            
        }
        else if (house != null && house != houses[0] && house != houses[1] && this.getResource("wood") >= 10 && this.getResource("stone") >= 25 && peopleManager.belowMaxStruct("Well")){
            if(structures[2].tryBuild(grid, foot1X, foot1Y+1)){
                this.changeResource("wood", this.getResource("wood")-10);
                this.changeResource("stone", this.getResource("stone")-25);
                addStructure("Well");
                structureBuilt[2] = true;
            }
        }
        if (this.getResource("wood") >= 10 && this.getResource("stone") > 10 && job[0].equals("crafter") && job[1].equals("tool")&& this.getResourceOrDefault("tool", 0) < 2){
            this.changeResource("tool", 2);
            return true;
        } else if (this.getResource("wood") >= 20 && this.getResourceOrDefault("tool", 0) < 1) {
            this.changeResource("tool", 1);
            return true;
        }
        return false;
    }

    public boolean eatFood() {
        if (this.getResource("energy") < 100 && this.getResource("nutrients") > 0) {
            changeResource("energy", this.getResource("energy") + this.getResource("nutrients"));
            changeResource("nutrients", 0);
            return true;
        } else {
            return false;
        }
    }

    public double getLeg1Slope() {
        return (rootY - foot1Y) / (rootX - foot1X);
    }

    public double getLeg2Slope() {
        return (rootY - foot2Y) / (rootX - foot2X);
    }

    public boolean isStanding(Grid grid) {
        if (foot1Y + 1 >= grid.getHeight() || (foot1X >= 0 && foot1X < grid.getWidth() && grid.getPixel(foot1X, foot1Y + 1).getPropOrDefault("walkable", 0) == 1)) {
            return true;
        }
        if (foot2Y + 1 == grid.getHeight() || foot2X >= 0 && foot2X < grid.getWidth() && grid.getPixel(foot2X, foot2Y + 1).getPropOrDefault("walkable", 0) == 1) {
            return true;
        }
        return false;
    }

    public int[] getFoot1() {
        return (new int[]{foot1X, foot1Y});
    }

    public int[] getFoot2() {
        return (new int[]{foot2X, foot2Y});
    }

    public int[] getRoot() {
        return (new int[]{(int) rootX, (int) rootY});
    }
    public int[] getKnee1(){
        return (new int[]{(int) knee1X, (int) knee1Y});
    }
    public int[] getKnee2(){
        return (new int[]{(int) knee2X, (int) knee2Y});
    }
    public double getHeadRadius(){
        return headRadius;
    }

    public void setRoot(double x, double y) {
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

    public void changeResource(String resource, int amount) {
        inventory.replace(resource, amount);
    }

    public int getResource(String resource) {
        return inventory.get(resource);
    }

    public Set<Entry<String, Integer>> getResources() {
        return inventory.entrySet();
    }

    private Person setResource(String resource, int amount) {
        inventory.put(resource, amount);

        //for chaining method calls
        return this;
    }

    private int getResourceOrDefault(String resource, int other) {
        return inventory.getOrDefault(resource, other);
    }

    public int getDesire(String resource) {
        return desiredResources.get(resource);
    }

    public Set<Entry<String, Integer>> getDesires() {
        return desiredResources.entrySet();
    }

    public int getDesireOrDefault(String resource, int other) {
        return desiredResources.getOrDefault(resource, other);
    }

    private Person setDesire(String resource, int amount) {
        desiredResources.put(resource, amount);

        //for chaining method calls
        return this;
    }
    private Person setDesireCheckJob(String resource, int amount){
        //this sets the desire only if it's not the current job
        if(job == null || !job[1].equals(resource))
            desiredResources.put(resource, amount);

        return this;
    }
    public double getHappiness(){
        double happiness = 0;
        int count = 0;
        for (String resource : desiredResources.keySet()) {

            if(desiredResources.get(resource) != 0){  //avoid dividing by 0
                //happiness = square root(have / desired)
                //this allows for a bit of extra happiness if there is surplus
                happiness += Math.sqrt(Math.max(inventory.get(resource) / (double)desiredResources.get(resource), 0));
                count++;
            }
        }
        happiness += Math.sqrt(Math.max((house == null ? 0 : house.comfort) / 100d, 0));
        count++;
        return happiness / count;  //may retun NaN if no desires
    }

    public void setShowInventory(boolean showInventory) {
        this.showInventory = showInventory;
    }

    public boolean getShowInventory() {
        return showInventory;
    }

    public void setDragged(boolean dragged) {
        this.dragged = dragged;
    }

    public boolean getDragged() {
        return dragged;
    }

    public String getCurrentActivity() {
        return currentActivity;
    }

    public Blueprint getHouse() {
        return house;
    }

    public void ShareResources(Person other){
        double distX = rootX - other.getRoot()[0];
        double distY = rootY - other.getRoot()[1];
        //distance between the two people
        if(distX*distX+distY*distY<maxStep*maxStep && job != null) {
            if(job[0].equals("gatherer")){
                int surplus = getResource(job[1]) - getDesire(job[1])/2;  //extra resources collected from the job
                int needed = other.getDesire(job[1]) - other.getResource(job[1]);
                if(needed > 0 && surplus > 0){
                    int shared = Math.min(surplus, needed);
                    changeResource(job[1], getResource(job[1])-shared);
                    other.changeResource(job[1], other.getResource(job[1])+shared);
                }
            }
            else if(job[1].equals("crafter")){
                int tool = getResource("tool");
                int otherTool = other.getResource("tool");
                if(tool > otherTool){
                    setResource("tool", tool-1);
                    other.setResource("tool", otherTool+1);
                }
            }
        }
    }

    private void addStructure(String name){
        structureNumbers.replace(name, structureNumbers.get(name)+1);
    }
    public int getStructure(String name){
        return structureNumbers.get(name);
    }
}
