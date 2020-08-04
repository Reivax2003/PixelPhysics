package sandbox.goals;

import sandbox.*;
import sandbox.people.*;

public class MaxEnergy extends MaxGoal { // Whether one energy amount is at a set amount or better

    private static final long serialVersionUID = 106653;

    private int energyGoal;

    public MaxEnergy(int energyGoal) {
        this.energyGoal = energyGoal;
    }

    protected boolean idividualTest(Person person) {
        return person.getResource("energy") >= energyGoal;
    }

    public String getInfo() {
        return "One person must have " + energyGoal + " energy or more: " + ((valid) ? "Complete" : "Incomplete");
    }
}
