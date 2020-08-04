package sandbox;

import sandbox.people.*;
import java.util.ArrayList;
import java.util.HashMap;

public class PeopleManager {
    private final Grid grid;
    private ArrayList<Person> people;
    HashMap<String, Integer> popPerStructure = new HashMap<>();

    public PeopleManager(Grid grid){
        this.grid = grid;
        people = grid.getPeople();

        popPerStructure.put("Well", 10);
        popPerStructure.put("Fire Pit", 5);
        popPerStructure.put("Garden", 2);
    }

    public void updatePeople(){
        for (Person each : people){
            each.update(grid, this);
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

    public int getMaxStruct(String structure){return (int) Math.ceil(this.getPopulation()/popPerStructure.get(structure))+1;}
    public boolean belowMaxStruct(String structure) {
        return getStructure(structure) < getMaxStruct(structure);
    }
    public int getStructure(String structure){
        int structures = 0;
        for (Person person : people) {
            structures += person.getStructure(structure);

        }
        return structures;
    }
}
