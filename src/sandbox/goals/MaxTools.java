package sandbox.goals;

import sandbox.*;
import sandbox.people.*;

public class MaxTools extends MaxGoal { // Whether one tool level is above or at a set level

    private static final long serialVersionUID = 99139;

    private int toolLevelGoal;

    public MaxTools(int toolLevelGoal) {
        this.toolLevelGoal = toolLevelGoal;
    }

    protected boolean idividualTest(Person person) {
        return person.getResource("tool") >= toolLevelGoal;
    }

    public String getInfo() {
        return "One person must have " + toolLevelGoal + " level tools or better: " + ((valid) ? "Complete" : "Incomplete");
    }
}
