package sandbox;

import sandbox.goals.*;
import sandbox.people.*;
import java.util.ArrayList;

public class GoalManager {


    private final Grid grid;
    private final PeopleManager peopleManager;
    private final ArrayList<Goal> goals;

    public GoalManager(Grid grid, PeopleManager peopleManager) {
        this.grid = grid;
        this.peopleManager = peopleManager;

        this.goals = grid.getGoals();


    }

    public boolean validate() {
      boolean valid = true;
      for (Goal each : goals){
          valid = each.validate(grid, peopleManager) && valid  ;
      }
      return valid;
    }

}
