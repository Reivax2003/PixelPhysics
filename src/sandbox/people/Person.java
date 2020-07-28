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

    public Person(int x, int y) {
        rootX = (double) x;
        rootY = (double) y;
    }

    //valid pixel cannot have fluidity property nor temp above 80
    public int[] getNextStep(Grid grid, int direction){ //-1 = left, 1 = right
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
            if (grid.getPixel(x+direction, y).hasProperty("overwritable")){//DON'T FORGET TO ADD OTHER CONDITIONS LATER
                x += direction;
            }
            else{
                blocked = true;
                for (int v = 1; v <= maxStepHeight; v++){
                    if (grid.getPixel(x+direction, y+v).hasProperty("overwritable")){
                        blocked = false;
                        x += direction;
                        y += v;
                    }
                }
            }
        }
        if (blocked)
            return new int[]{-1, -1};
        else
            return new int[]{x, y};
    }
    public void update(){
        double feetXenterX = (foot1X+foot2X)/2;
        double feetCenterY = (foot1Y+foot2Y)/2;

        double spread = Math.sqrt(Math.pow(feetCenterY, 2)+Math.pow(feetXenterX, 2));
        double angle = Math.acos((spread/2)/legLen);

        double displacement = legLen*Math.sin(angle);

        double slope1 = (foot1Y-foot2Y)/(foot1X-foot2X);
        double slope2 = -1/slope1;

        double deltaY = -(1/slope2)+Math.sqrt(Math.pow(1/slope2, 2) - 4*Math.pow(displacement, 2));
        if (deltaY < 0){
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
}
