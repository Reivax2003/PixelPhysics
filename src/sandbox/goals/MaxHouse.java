package sandbox.goals;

import sandbox.*;
import sandbox.people.*;

public class MaxHouse extends MaxGoal { // Whether the best house of the people is at a set level or better

    private static final long serialVersionUID = 8087987;

    private TestHouse testHouse;
    private String houseName;

    public MaxHouse(Blueprint goalHouse) {
        this.houseName = goalHouse.getName();
        testHouse = new TestHouse(goalHouse);
    }

    protected boolean idividualTest(Person person) {
        return testHouse.test(person);
    }

    public String getInfo() {
        return "The best house must be a " + houseName + " or better: " + ((valid) ? "Complete" : "Incomplete");
    }
}
