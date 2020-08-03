package sandbox.goals;

import sandbox.*;
import sandbox.people.*;
import java.io.Serializable;

public class TestHouse implements Serializable { // Whether the house of the person is at a set level or better

    private static final long serialVersionUID = 987;

    private Class goalHouse;
    private Class[] houseOrder = new Class[]{WoodShack.class, WoodAFrame.class, WoodHouse.class};
    private int houseIndex;

    public TestHouse(Blueprint goalHouse) {
        this.goalHouse = goalHouse.getClass();
        for (int i = houseOrder.length-1; i >= 0; i--) { //Gets index of house in house ranking list
            if (houseOrder[i].equals(this.goalHouse)) {
              houseIndex = i;
            }
        }
    }

    public boolean test(Person person) {
        if (person.getHouse() == null) {return false;} // If there is no house, fails
        Class testHouse = person.getHouse().getClass();
        for (int i = houseOrder.length-1; i >= houseIndex; i--) { // Go backwards through house values
            if (houseOrder[i].equals(testHouse)) { // If the house is at a higher or equal index to the goal, it passes
                return true;
            }
        }
        return false;
    }
}
