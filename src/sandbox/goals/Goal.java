package sandbox.goals;

import sandbox.*;
import java.io.Serializable;

public interface Goal extends Serializable {

    public boolean validate(Grid grid, PeopleManager peopleManager); // Returns wether objective is complete

    public String getInfo(); // Returns Info as single line in the form "Requirement: Complete/Incomplete"
}
