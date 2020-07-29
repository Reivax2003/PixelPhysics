package sandbox.people;

import java.awt.*;
import java.util.HashMap;
import sandbox.*;
import sandbox.pixels.*;

public class Person {
    //all positions are in units of grid

    private double rootX, rootY;
    private int foot1X, foot1Y, foot2X, foot2Y;
    private final double maxStep = 3; //must be less than 2*legLen
    private final double maxStepHeight = 2;
    private final double headR = 2;
    private final double legLen = 2;
    private int direction = -1; //-1 left 1 right

    public Person(int x, int y) {
        rootX = (double) x;
        rootY = (double) y;
    }

    //valid pixel cannot have fluidity property nor temp above 80
    public void takeNextStep(Grid grid){
        boolean blocked = false;

        int x = -1;
        int y = -1;

        if (direction == -1) {
            if (foot1X < foot2X) {
                x = foot1X;
                y = foot1Y;
            } else {
                x = foot2X;
                y = foot2Y;
            }
        } else {
            if (foot1X > foot2X) {
                x = foot1X;
                y = foot1Y;
            } else {
                x = foot2X;
                y = foot2Y;
            }
        }

        for (int i = 0; i < maxStep; i++){
            if (x+direction >= 0 && x+direction < grid.getWidth() && grid.getPixel(x+direction, y).hasProperty("overwritable")){//DON'T FORGET TO ADD OTHER CONDITIONS LATER
                x += direction;
            }
            else if (x+direction >= 0 && x+direction < grid.getWidth()){
                blocked = true;
                for (int v = 1; v <= maxStepHeight; v++){
                    if (grid.getPixel(x+direction, y-v).hasProperty("overwritable")){
                        blocked = false;
                        x += direction;
                        y -= v;
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
                if (foot1X < foot2X) {
                    foot1X = x;
                    foot1Y = y;
                } else {
                    foot2X = x;
                    foot2Y = y;
                }
            } else {
                if (foot1X > foot2X) {
                    foot1X = x;
                    foot1Y = y;
                } else {
                    foot2X = x;
                    foot2Y = y;
                }
            }
        }
        if (!isStanding(grid)){
            foot1Y += 1;
            foot2Y += 1;
        }
    }
    public void update(){
        double feetXenterX = (foot1X+foot2X)/2;
        double feetCenterY = (foot1Y+foot2Y)/2;

        double spread = Math.sqrt(Math.pow(feetCenterY, 2)+Math.pow(feetXenterX, 2));
        double angle = Math.acos((spread/2)/legLen);

        double displacement = legLen*Math.sin(angle);

        double slope1 = 0;
        if (foot1X != foot2X)
            slope1 = (foot1Y-foot2Y)/(foot1X-foot2X);

        double slope2 = -1/slope1;

        double deltaY = -(1/slope2)+Math.sqrt(Math.pow(1/slope2, 2) - 4*Math.pow(displacement, 2));
        if (deltaY > 0){
            deltaY = -(1/slope2)+Math.sqrt(Math.pow(1/slope2, 2) + 4*Math.pow(displacement, 2));
        }
        deltaY /= 2;

        double deltaX = deltaY/slope2;

        rootX = feetXenterX+deltaX;
        rootY = feetCenterY+deltaY;
    }
    public double getLeg1Slope(){
        return (rootY-foot1Y)/(rootX-foot1X);
    }
    public double getLeg2Slope(){
        return (rootY-foot2Y)/(rootX-foot2X);
    }
    public boolean isStanding(Grid grid){
        if (foot1Y + 1 >= grid.getHeight() || grid.getPixel(foot1X, foot1Y+1).getPropOrDefault("density", -1) > 0){
            return true;
        }
        if (foot2Y + 1 >= grid.getHeight() || grid.getPixel(foot2X, foot2Y+1).getPropOrDefault("density", -1) > 0){
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
}
