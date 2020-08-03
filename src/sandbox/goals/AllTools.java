package sandbox.goals;

import sandbox.*;
import sandbox.people.*;

public class AllTools extends AllGoal { // Whether all tool levels of the people are at a set level or better

    private static final long serialVersionUID = 9919239;

    private int toolLevelGoal;

    public AllTools(int toolLevelGoal) {
        this.toolLevelGoal = toolLevelGoal;
    }

    protected boolean idividualTest(Person person) {
        return person.getResource("tool") >= toolLevelGoal;
    }

    public String getInfo() {
        return "All people must have " + toolLevelGoal + " level tools or better: " + ((valid) ? "Complete" : "Incomplete");
    }
}
