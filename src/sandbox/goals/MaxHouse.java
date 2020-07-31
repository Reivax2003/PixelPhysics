package sandbox.goals;

import sandbox.*;
import sandbox.people.*;

public class MaxHouse implements Goal {

    private Class goalHouse;
    private Class[] houseOrder = new Class[]{WoodShack.class, WoodAFrame.class, WoodHouse.class};
    private int houseIndex;


    public MaxHouse(Blueprint goalHouse) {
        this.goalHouse = goalHouse.getClass();
        for (int i = houseOrder.length-1; i >= 0; i--) {
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
                    if (houseOrder[i].equals(testHouse)) {
                        return true;
                    }
                }
            }
        }

        return false; // If not goal house or equal
    }
}
