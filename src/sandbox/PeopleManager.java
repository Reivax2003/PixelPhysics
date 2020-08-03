package sandbox;

import sandbox.people.*;
import java.util.ArrayList;

public class PeopleManager {
    private final Grid grid;
    private ArrayList<Person> people;

    public PeopleManager(Grid grid){
        this.grid = grid;
        people = grid.getPeople();
        //test
        
    }

    public void updatePeople(){
        for (Person each : people){
            each.update(grid);
            for (Person other : people) {
                each.ShareResources(other);
            }
        }
    }
    public int getPopulation(){
        return people.size();
    }
    public Person getPerson(int index){
        return people.get(index);
    }

    public double getAverageHappiness(){
        double happiness = 0;
        int count = 0;
        for (Person person : people) {
            happiness += person.getHappiness();
            count++;
        }
        return happiness/count;  //may return NaN if no people, or if people have no desires
    }
}
