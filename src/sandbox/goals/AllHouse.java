package sandbox.goals;

import sandbox.*;
import sandbox.people.*;

public class AllHouse implements Goal { // Whether all houses  of the people are at a set level or better

    private static final long serialVersionUID = 8087987;

    private Class goalHouse;
    private Class[] houseOrder = new Class[]{WoodShack.class, WoodAFrame.class, WoodHouse.class};
    private int houseIndex;
    private String houseName;
    private boolean valid = false;

    public AllHouse(Blueprint goalHouse) {
        this.goalHouse = goalHouse.getClass();
        this.houseName = goalHouse.getName();
        for (int i = houseOrder.length-1; i >= 0; i--) { //Gets index of house in house ranking list
            if (houseOrder[i].equals(this.goalHouse)) {
              houseIndex = i;
            }
        }
    }

    public boolean validate(Grid grid, PeopleManager peopleManager) {
        valid = false;
        if (peopleManager.getPopulation() > 0) { //If there are people

            for (Person each : grid.getPeople()) {
                valid = false;
                if (each.getHouse() == null) {break;} // If there is no house, fails
                Class testHouse = each.getHouse().getClass();
                for (int i = houseOrder.length-1; i >= houseIndex; i--) { // Go backwards through house values
                    if (houseOrder[i].equals(testHouse)) { // If the house is at a higher or equal index to the goal, it passes
                        valid = true;
                        break;
                    }
                }
                if(!valid) {break;} // If this person did not pass
            }
        }

        if(valid) {
            return true;
        }
        return false; // If all people do not pass or there are no people
    }

    public String getInfo() {
        return "All houses must be a " + houseName + " or better: " + ((valid) ? "Complete" : "Incomplete");
    }
}
