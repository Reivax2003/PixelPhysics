package sandbox.goals;

import sandbox.*;
import sandbox.people.*;

public class MaxHouse implements Goal { // Whether the best house the people is at a set level or better

    private Class goalHouse;
    private Class[] houseOrder = new Class[]{WoodShack.class, WoodAFrame.class, WoodHouse.class};
    private int houseIndex;
    private String houseName;
    private boolean valid = false;

    public MaxHouse(Blueprint goalHouse) {
        this.goalHouse = goalHouse.getClass();
        this.houseName = goalHouse.getName();
        for (int i = houseOrder.length-1; i >= 0; i--) { //Gets index of house in house ranking list
            if (houseOrder[i].equals(this.goalHouse)) {
              houseIndex = i;
            }
        }
    }

    public boolean validate(Grid grid, PeopleManager peopleManager) {

        if (peopleManager.getPopulation() > 0) { //If there are people
            // people.Person testPerson;
            for (Person each : grid.getPeople()) {
                if (each.getHouse() == null) {continue;} // If there is no house
                Class testHouse = each.getHouse().getClass();
                for (int i = houseOrder.length-1; i >= houseIndex; i--) { // Go backwards through house values
                    if (houseOrder[i].equals(testHouse)) { // If the house is at a higher or equal index to the goal, it passes
                        valid = true;
                        return true;
                    }
                }
            }
        }
        valid = false;
        return false; // If not goal house or equal
    }

    public String getInfo() {
        return "The best house must be a " + houseName + " or better: " + ((valid) ? "Complete" : "Incomplete");
    }
}
