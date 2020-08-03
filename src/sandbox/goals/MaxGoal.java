package sandbox.goals;

import sandbox.*;
import sandbox.people.*;

abstract class MaxGoal implements Goal { // Whether atleast one person fits the requirement

    protected boolean valid = false;

    public boolean validate(Grid grid, PeopleManager peopleManager) {
        valid = false;
        if (peopleManager.getPopulation() > 0) { //If there are people
            // people.Person testPerson;
            for (Person each : grid.getPeople()) {
                valid = idividualTest(each);
                if(valid) {return valid;}
            }
        }

        return valid; // If not goal house or equal
    }

    protected abstract boolean idividualTest(Person person); // Test determined by goal function
}
