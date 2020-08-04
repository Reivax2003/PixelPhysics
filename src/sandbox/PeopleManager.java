package sandbox;

import sandbox.people.*;
import java.util.ArrayList;
import java.util.HashMap;

public class PeopleManager {
    private final Grid grid;
    private ArrayList<Person> people;
    HashMap<String, Integer> structures = new HashMap<>();

    public PeopleManager(Grid grid){
        this.grid = grid;
        people = grid.getPeople();

        structures.put("Well", 0);
        structures.put("Fire Pit", 0);
        structures.put("Garden", 0);
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
    public int getMaxWells(){return (int) Math.ceil(this.getPopulation()/10)+1;}
    public int getMaxFirePits(){return (int) Math.ceil(this.getPopulation()/5)+1;}
    public int getMaxGardens(){return (int) Math.ceil(this.getPopulation()/2)+1;}
    public int getStructure(String name){
        return structures.get(name);
    }
    public void addStructure(String name){
        structures.replace(name, structures.get(name)+1);
    }
}
