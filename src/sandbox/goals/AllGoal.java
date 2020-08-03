package sandbox.goals;

import sandbox.*;
import sandbox.people.*;

abstract class AllGoal implements Goal { // Whether all houses  of the people are at a set level or better

    protected boolean valid = false;

    public boolean validate(Grid grid, PeopleManager peopleManager) {
        valid = false;
        if (peopleManager.getPopulation() > 0) { //If there are people

            for (Person each : grid.getPeople()) {
                valid = false;
                valid = idividualTest(each);
                if(!valid) {break;} // If this person did not pass
            }
        }

        return valid; // If all people do not pass or there are no people
    }

    protected abstract boolean idividualTest(Person person);

}
