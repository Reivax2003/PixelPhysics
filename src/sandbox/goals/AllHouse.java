package sandbox.goals;

import sandbox.*;
import sandbox.people.*;

public class AllHouse extends AllGoal { // Whether all houses  of the people are at a set level or better

    private static final long serialVersionUID = 8087987;

    private Class goalHouse;
    private Class[] houseOrder = new Class[]{WoodShack.class, WoodAFrame.class, WoodHouse.class};
    private int houseIndex;
    private String houseName;

    public AllHouse(Blueprint goalHouse) {
        this.goalHouse = goalHouse.getClass();
        this.houseName = goalHouse.getName();
        for (int i = houseOrder.length-1; i >= 0; i--) { //Gets index of house in house ranking list
            if (houseOrder[i].equals(this.goalHouse)) {
              houseIndex = i;
            }
        }
    }

    protected boolean idividualTest(Person person) {
        if (person.getHouse() == null) {return false;} // If there is no house, fails
        Class testHouse = person.getHouse().getClass();
        for (int i = houseOrder.length-1; i >= houseIndex; i--) { // Go backwards through house values
            if (houseOrder[i].equals(testHouse)) { // If the house is at a higher or equal index to the goal, it passes
                return true;
            }
        }
        return false;
    }

    public String getInfo() {
        return "All houses must be a " + houseName + " or better: " + ((valid) ? "Complete" : "Incomplete");
    }
}
