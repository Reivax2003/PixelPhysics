package sandbox;

import sandbox.people.Person;

import java.util.ArrayList;

public class PeopleManager {
    private final Grid grid;
    private ArrayList<Person> people = new ArrayList<Person>();

    public PeopleManager(Grid grid){
        this.grid = grid;
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