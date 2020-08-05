package sandbox.goals;

import sandbox.*;

public class BuildWell implements Goal { // Whether the average happiness of the people is above a set level

    private static final long serialVersionUID = 12323232;

    private int wellGoal;
    private boolean valid = false;


    public BuildWell(int wellGoal) {
        this.wellGoal = wellGoal;
    }

    public boolean validate(Grid grid, PeopleManager peopleManager) {

        if (peopleManager.getPopulation() > 0) { //If there are people
            if (peopleManager.getStructure("Well") >= wellGoal) {
                valid = true;
                return valid;
            }
        }
        valid = false;
        return valid;
    }

    public String getInfo() {
        return "There must be " + wellGoal + " well" + ((wellGoal > 1) ? "s" : "") + " or more: " + ((valid) ? "Complete" : "Incomplete");
    }
}
