package sandbox.goals;

import sandbox.*;
import sandbox.people.*;

public class AllHouse extends AllGoal { // Whether all houses  of the people are at a set level or better

    private static final long serialVersionUID = 8087987;

    private TestHouse testHouse;
    private String houseName;

    public AllHouse(Blueprint goalHouse) {
        this.houseName = goalHouse.getName();
        testHouse = new TestHouse(goalHouse);
    }

    protected boolean idividualTest(Person person) {
        return testHouse.test(person);
    }

    public String getInfo() {
        return "All houses must be a " + houseName + " or better: " + ((valid) ? "Complete" : "Incomplete");
    }
}
