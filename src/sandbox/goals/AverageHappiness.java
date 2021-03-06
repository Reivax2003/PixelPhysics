package sandbox.goals;

import sandbox.*;

public class AverageHappiness implements Goal { // Whether the average happiness of the people is above a set level

    private static final long serialVersionUID = 12323232;

    private double happinessGoal;
    private boolean valid = false;


    public AverageHappiness(double happinessGoal) {
        this.happinessGoal = happinessGoal;
    }

    public boolean validate(Grid grid, PeopleManager peopleManager) {

        if (peopleManager.getPopulation() > 0) { //If there are people
            if (peopleManager.getAverageHappiness() >= happinessGoal) {
                valid = true;
                return valid;
            }
        }
        valid = false;
        return valid;
    }

    public String getInfo() {
        return "The average happiness must be " + happinessGoal + " or better: " + ((valid) ? "Complete" : "Incomplete");
    }
}
