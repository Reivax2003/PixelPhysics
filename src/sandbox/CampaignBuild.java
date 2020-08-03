package sandbox;

import sandbox.goals.AllHouse;
import sandbox.goals.Goal;
import sandbox.goals.MaxHouse;
import sandbox.people.Person;
import sandbox.people.WoodAFrame;
import sandbox.people.WoodShack;

import java.io.File;
import java.util.ArrayList;

public class CampaignBuild { //Hardwrite this to create campaign level

    private final Grid grid;

    public CampaignBuild(Grid grid) {
        this.grid = grid;

        grid.clearGrid();
        setUpWorld();
        setUpPeople();
        setUpGoals();
        grid.setName("Level 1"); //Set name of level
        grid.saveGrid(new File("campaign/level" + "1" + ".lvl"), true); //Saves as a campaign file numbered what is set
    }

    private void setUpWorld() {
        //grid.loadGrid("Placeholder"); // Load from pregened world
        grid.worldGen((long) 12321, "Earth"); // 1st arg seed (long) (Math.floor(Math.random() * 100000) + 1)2nd arg Earth Alien or any text for random type
    }

    private void setUpPeople() {
        ArrayList<Person> people = grid.getPeople();
        // Test person
        people.add(new Person(50, 25, new String[]{"gatherer","wood"}));
        people.add(new Person(50, 25, new String[]{"crafter","tool"}));
    }

    private void setUpGoals() {
        ArrayList<Goal> goals = grid.getGoals();
        // Test goal
        goals.add(new MaxHouse(new WoodAFrame()));
        goals.add(new AllHouse(new WoodShack()));
    }

}
