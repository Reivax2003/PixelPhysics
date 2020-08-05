package sandbox;

import sandbox.pixels.*;

import javax.swing.*;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.Timer;

public class MenuBar extends JMenuBar implements ActionListener {

    private final Grid grid;
    private boolean campaign;
    private Timer timerToKill;
    private JMenu loadMenu;

    public final Pixel[] pixels = { //List of elements in order
            new WetSand(),
            new Sand(),
            new Soil(),
            new Stone(),
            new Metal(),
            new Charcoal(),
            new Ice(),
            new Ghost(),
            new Duplicate(),
            new Fuse(),
            new FusePowder(),
            new Water(),
            new Lava(),
            new Acid(),
            new Gasoline(),
            new Smoke(),
            new Steam(),
            new Fire(),
            new Electricity(),
            new Wood(),
            new Plant(),
            new AlienPlant(true),
            new Plant3(),
            new Slime(),
            new Grass(),
    };
    private final Pixel[] solid = {
            new WetSand(),
            new Sand(),
            new Soil(),
            new Stone(),
            new Metal(),
            new Charcoal(),
            new Ice(),
            new Ghost(),
            new Duplicate(),
            new Fuse(),
            new FusePowder(),
    };
    private final Pixel[] liquid = {
            new Water(),
            new Lava(),
            new Acid(),
            new Gasoline(),
    };
    private final Pixel[] gas = {
            new Smoke(),
            new Steam(),
    };
    private final Pixel[] property = {
            new Fire(),
            new Electricity(),
    };
    private final Pixel[] living = {
            new Wood(),
            new Plant(),
            new AlienPlant(true),
            new Plant3(),
            new Slime(),
            new Grass(),
    };

    private ButtonGroup elementButtons;

    public int chosen = 1; //currently selected substance
    private JRadioButtonMenuItem chosenBtn;

    public MenuBar(Grid grid) {
        this.grid = grid;
        this.campaign = grid.campaign;

        JMenu solidsMenu = new JMenu("Solid");
        JMenu liquidsMenu = new JMenu("Liquid");
        JMenu gassesMenu = new JMenu("Gas");
        JMenu propertiesMenu = new JMenu("Property");
        JMenu livingMenu = new JMenu("Living");
        elementButtons = new ButtonGroup();

        int imod = 0;
        populateMenu(solidsMenu, solid, imod);
        imod += solid.length;
        populateMenu(liquidsMenu, liquid, imod);
        imod += liquid.length;
        populateMenu(gassesMenu, gas, imod);
        imod += gas.length;
        populateMenu(propertiesMenu, property, imod);
        imod += property.length;
        populateMenu(livingMenu, living, imod);

        // Add elments menus to bar
        this.add(solidsMenu);
        this.add(liquidsMenu);
        this.add(gassesMenu);
        this.add(propertiesMenu);
        this.add(livingMenu);

        // Whitespace
        this.add(Box.createHorizontalGlue());

        // Build view Menu (IF this gets big enough modify element list builder to do it)
        JMenu viewMenu = new JMenu("View");
        JMenuItem menuItem = new JMenuItem("Normal");
        menuItem.setActionCommand("normal"); //Possibly change these to radio buttons, but that is an asthetic choice
        menuItem.addActionListener(this); //Probably add hotkeys
        viewMenu.add(menuItem);
        menuItem = new JMenuItem("Heat");
        menuItem.setActionCommand("heat");
        menuItem.addActionListener(this);
        viewMenu.add(menuItem);
        menuItem = new JMenuItem("Color Changer");
        menuItem.setActionCommand("changeColor");
        menuItem.addActionListener(this);
        viewMenu.add(menuItem);

        // Add view menu
        this.add(viewMenu);

        // Build settings/control/options decide later menu
        JMenu controlMenu = new JMenu("Control");

        menuItem = new JMenuItem("Reload"); //Reloads world as it was intitialy
        menuItem.setActionCommand("reload");
        menuItem.addActionListener(this);
        controlMenu.add(menuItem);

        if (!campaign) {
            menuItem = new JMenuItem("Clear"); //Clears grid as air only
            menuItem.setActionCommand("clear");
            menuItem.addActionListener(this);
            controlMenu.add(menuItem);
        }

        JMenu saveMenu = new JMenu("Save");
        for (int i = 0; i < 10; i++) {  //add 10 save slots
            menuItem = new JMenuItem("slot " + i);
            menuItem.setActionCommand("save" + i);
            menuItem.addActionListener(this);
            saveMenu.add(menuItem);
        }
        controlMenu.add(saveMenu);

        // JMenu camSaveMenu = new JMenu("CampaignSave"); // Comment out on release, for creating campaigns only
        // for (int i = 1; i < 10; i++) {  //add 10 save slots
        //     menuItem = new JMenuItem("slot " + i);
        //     menuItem.setActionCommand("camp" + i);
        //     menuItem.addActionListener(this);
        //     camSaveMenu.add(menuItem);
        // }
        // controlMenu.add(camSaveMenu); // Commented out as we have campaign build which does job

        loadMenu = new JMenu("Load");
        for (int i = 0; i < 10; i++) {  //add 10 save slots
            menuItem = new JMenuItem("slot " + i);
            menuItem.setActionCommand("load" + i);
            menuItem.addActionListener(this);
            menuItem.setEnabled(new File("save/slot"+i+".lvl").exists());
            loadMenu.add(menuItem);
        }
        controlMenu.add(loadMenu);

        if (!campaign) {
            menuItem = new JMenuItem("Energy");
            menuItem.setActionCommand("energy");
            menuItem.addActionListener(this);
            controlMenu.add(menuItem);
        }

        menuItem = new JMenuItem("Info");
        menuItem.setActionCommand("info");
        menuItem.addActionListener(this);
        controlMenu.add(menuItem);

        menuItem = new JMenuItem("Main Menu");
        menuItem.setActionCommand("quit2menu");
        menuItem.addActionListener(this);
        controlMenu.add(menuItem);

        menuItem = new JMenuItem("Quit");
        menuItem.setActionCommand("quit");
        menuItem.addActionListener(this);
        controlMenu.add(menuItem);

        saveMenu = new JMenu("Save&Quit");
        for (int i = 0; i < 10; i++) {  //add 10 save slots
            menuItem = new JMenuItem("slot " + i);
            menuItem.setActionCommand("qsave" + i);
            menuItem.addActionListener(this);
            saveMenu.add(menuItem);
        }
        controlMenu.add(saveMenu);

        // Add control menu to bar
        this.add(controlMenu);
    }


    public void actionPerformed(ActionEvent a) { // Detects interaction with menu
        String action = a.getActionCommand();
        try { //Assumes all numeric action events are chosing type
            int newChoice = Integer.parseInt(action);
            chosen = newChoice;
            chosenBtn = (JRadioButtonMenuItem)a.getSource();
        } catch (NumberFormatException e) {

            if (action.startsWith("save")) { //Save and load special case
                grid.saveGrid(new File("save/slot"+action.substring(4)+".lvl"));
                checkSaveSlots();
            }else if (action.startsWith("camp")) { //Save as campaign
                grid.saveGrid(new File("campaign/level"+action.substring(4)+".lvl"), true); //Saves as a campaign file
            }else if (action.startsWith("load")) {
                grid.loadGrid(new File("save/slot"+action.substring(4)+".lvl"));
            }else if (action.startsWith("qsave")) { //Save and quit
                grid.saveGrid(new File("save/slot"+action.substring(5)+".lvl"));
                System.exit(0); // Ends the process
            } else {
                switch (action) {
                    //Control Menu
                    case "reload": // Restores grid to how it was from file or world generation
                        grid.reloadGrid();
                        break;
                    case "clear": // Clears grid, people, and goals and leaves and empty air
                        grid.clearGrid();
                        break;
                    case "energy": // Sets energy to infinite
                        grid.setInfEnergy();
                        break;
                    case "info": // Displays info and controls
                        displayInfo();
                        break;
                    case "quit2menu": // Exits sandbox and opens menu
                        quitToMenu();
                        break;
                    case "quit": // Ends the process
                        System.exit(0);
                    //View Menu
                    case "normal":
                        grid.setView(0);
                        break;
                    case "heat":
                        grid.setView(1);
                        break;
                    case "changeColor":
                        colorChanger();
                        break;
                }
            }
        }
    }

    public void displayInfo() { // Displays info about currently loaded sandbox and controls
        String[] info = grid.getInfo();
        int type = Integer.parseInt(info[0]);
        String message = "<html><center>Sandbox initially";

        switch(type) {
            case 0: // Empty grid
                message = message + " an empty grid.<br>";
                break;
            case 1: // Loaded from file
                message = message + " loaded from file " + info[1] + ".<br>";
                break;
            case 2: // Generated from seed
                message = message + " generated from seed " + info[1] + ".<br>";
                break;
            case 3: // Campaign level
                message = "<html><center>Mission: " + info[1] + "<br>";
                for (int i = 2; i < info.length; i++) {
                    message = message + info[i] + "<br>";
                }
        }

        message = message + "<br>Left Click to Place.<br>" + "Right Click to Delete.<br>"  +
                            "Space to Pause.<br>" + "Right Arrow to Advance Time.<small> (While Paused)</small><br>" +
                            "Scroll to Move Left and Right<br>" + "Control Click to Pan<br>" +
                            "Change Material in Menus.<br><br>" + "Version 1.0";
        JLabel messageLabel = (new JLabel(message));
        messageLabel.setHorizontalAlignment(SwingConstants.CENTER);
        messageLabel.setVerticalAlignment(SwingConstants.CENTER);

        Object[] options = {"Show People Tutorial" , "Close"};

        int c = JOptionPane.showOptionDialog(JOptionPane.getFrameForComponent(this), messageLabel, "Sandbox Info",JOptionPane.YES_NO_OPTION , JOptionPane.PLAIN_MESSAGE, null, options, options[1]);

        if (c == 0) { // If chosen display tutorial on people
            peopleTutorial();
        }
    }

    private void peopleTutorial() {
        String message = "<html><center> People's happiness is controlled by two things:<br>" +
                         "Their Desires and Their Homes<br><br>" +
                         "Their Desires are fufilled by gathering needed resources.<br>" +
                         "    Wood and Stone are used in crafting and contruction.<br>" +
                         "    Plants are converted into Nutrients to feed themselves.<br><br>" +
                         "Their Homes are improved by using resources to build them.<br>" ;
        JLabel messageLabel = (new JLabel(message));
        messageLabel.setHorizontalAlignment(SwingConstants.CENTER);
        messageLabel.setVerticalAlignment(SwingConstants.CENTER);

        JOptionPane.showMessageDialog(JOptionPane.getFrameForComponent(this), messageLabel, "People Tutorial", JOptionPane.PLAIN_MESSAGE);
    }

    private void colorChanger() {
        //use the current pixel's color and see if user wants to change it
        Color col = JColorChooser.showDialog(JOptionPane.getFrameForComponent(this), "Choose a color. Use color code 000001 for default", pixels[chosen].getColor());
        if(col != null){ //null means canceled
            if(col.getRed() == 0 && col.getGreen() == 0 && col.getBlue() == 1) {  //special number to reset to default color
                pixels[chosen].setColor(pixels[chosen].getOriginalColor());
                chosenBtn.setForeground(pixels[chosen].getOriginalColor().darker());
            }else{
                pixels[chosen].setColor(col);
                chosenBtn.setForeground(col.darker());
            }
        }
    }

    private void quitToMenu() {
        JOptionPane.getFrameForComponent(this).dispose(); // Closes frame
        timerToKill.cancel(); // Kills game loop
        MainMenu.main(new String[] {}); // Opens menu
    }

    public void setTimer(Timer timer) { // For main to pass down the timer to be killed
       timerToKill = timer;
    }

    public void populateMenu(JMenu menu, Pixel[] list, int indexMod) {
        for (int p = 0; p < list.length; p++) {
            JRadioButtonMenuItem button = new JRadioButtonMenuItem(list[p].getType());
            button.setForeground(list[p].getColor().darker()); // Color text for distinction
            elementButtons.add(button);
            menu.add(button);
            String index = String.valueOf(p + indexMod);

            if (p + indexMod == 1) { // Button one is by default selected as the first option
                button.setSelected(true);
                chosenBtn = button;
            }

            if (p + indexMod < 10)
                button.setAccelerator(KeyStroke.getKeyStroke(index)); // 0-9 by key
            else if (p + indexMod < 20) {
                button.setAccelerator(KeyStroke.getKeyStroke(index.charAt(1), ActionEvent.SHIFT_MASK)); // 10-19 by shift and first digit
            } else if (p + indexMod < 30) {
                button.setAccelerator(KeyStroke.getKeyStroke(index.charAt(1), ActionEvent.CTRL_MASK)); // 20-29 by control and first digit
            }


            button.setActionCommand(index);
            button.addActionListener(this);
        }
    }

    public void checkSaveSlots(){
        for (int i = 0; i < 10; i++) {  //add 10 save slots
            JMenuItem menuItem = loadMenu.getItem(i);
            menuItem.setEnabled(new File("save/slot"+i+".lvl").exists());
        }
    }
}
