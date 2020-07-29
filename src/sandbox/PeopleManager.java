package sandbox;

import sandbox.people.*;
import sandbox.pixels.*;
import java.util.ArrayList;

public class PeopleManager {
    private final Grid grid;
    private ArrayList<Person> people = new ArrayList<Person>();

    public PeopleManager(Grid grid){
        this.grid = grid;

        //test
        people.add(new Person(50, 25));
    }

    public void updatePeople(){
        for (Person each : people){
            each.update(grid);
        }
    }
    public int getPopulation(){
        return people.size();
    }
    public Person getPerson(int index){
        return people.get(index);
    }

}