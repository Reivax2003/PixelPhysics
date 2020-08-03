package sandbox.goals;

import sandbox.*;
import sandbox.people.*;

public class AllEnergy extends AllGoal { // Whether all tool levels of the people are at a set level or better

    private static final long serialVersionUID = 43110;

    private int energyGoal;

    public AllEnergy(int energyGoal) {
        this.energyGoal = energyGoal;
    }

    protected boolean idividualTest(Person person) {
        return person.getResource("energy") >= energyGoal;
    }

    public String getInfo() {
        return "All people must have " + energyGoal + " energy or more: " + ((valid) ? "Complete" : "Incomplete");
    }
}
