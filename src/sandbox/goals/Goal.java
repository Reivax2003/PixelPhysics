package sandbox.goals;

import sandbox.*;

public interface Goal {

    public boolean validate(Grid grid, PeopleManager peopleManager); // Returns wether objective is complete

    public String getInfo(); // Returns Info as single line
}
