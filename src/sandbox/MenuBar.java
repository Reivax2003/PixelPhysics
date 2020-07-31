package sandbox;

import sandbox.pixels.*;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

public class MenuBar extends JMenuBar implements ActionListener {

    private final Grid grid;
    private boolean campaign;

    public final Pixel[] pixels = { //List of elements in order, 0 and 10 are at ends of lists
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

        // Build Elements Menus
    /*JMenu elementsMenu = new JMenu("Elements");
    JMenu elementsMenu2 = new JMenu("Elem. cont.");
    ButtonGroup elementButtons = new ButtonGroup();

    for(int p = 0; p < pixels.length; p ++) { //Can only handle up to 20 elements
      JRadioButtonMenuItem button = new JRadioButtonMenuItem(pixels[p].getType());
      button.setForeground(pixels[p].getColor().darker()); // Color text for distinction
      elementButtons.add(button);
      String index = String.valueOf(p);
      if (index.length() == 1) {
        if(p == 0) { // 0 to end of first menu
          elementsMenu.add(button);
        }
        else if(p == 1) { // Button one is by default selected as the first option
          elementsMenu.insert(button, p-1);
          button.setSelected(true);
        }
        else { // 2-9
          elementsMenu.insert(button, p-1);
        }
        button.setAccelerator(KeyStroke.getKeyStroke(index)); // 0-9 by key
      }
      else {
        if(p == 10) { // 10 to end of second menu
          elementsMenu2.add(button);
        }
        else { //11-19
          elementsMenu2.insert(button, p-11);
        }
        button.setAccelerator(KeyStroke.getKeyStroke(index.charAt(1),ActionEvent.SHIFT_MASK)); // 10-19 by shift and first digit
      }
      button.setActionCommand(index);
      button.addActionListener(this);
    }*/

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

        JMenu camSaveMenu = new JMenu("CampaignSave"); // Comment out on release, for creating campaigns only
        for (int i = 1; i < 10; i++) {  //add 10 save slots
            menuItem = new JMenuItem("slot " + i);
            menuItem.setActionCommand("camp" + i);
            menuItem.addActionListener(this);
            camSaveMenu.add(menuItem);
        }
        controlMenu.add(camSaveMenu);

        saveMenu = new JMenu("Load");
        for (int i = 0; i < 10; i++) {  //add 10 save slots
            menuItem = new JMenuItem("slot " + i);
            menuItem.setActionCommand("load" + i);
            menuItem.addActionListener(this);
            saveMenu.add(menuItem);
        }
        controlMenu.add(saveMenu);

        if (!campaign) {
            menuItem = new JMenuItem("Energy");
            menuItem.setActionCommand("energy");
            menuItem.addActionListener(this);
            controlMenu.add(menuItem);
        }

        menuItem = new JMenuItem("Info"); // Modify this for campaign (Goals?)
        menuItem.setActionCommand("info");
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
            chosen = Integer.parseInt(action);
        } catch (NumberFormatException e) {

            if (action.startsWith("save")) { //Save and load special case
                grid.saveGrid(new File("save/slot"+action.substring(4)+".lvl"));
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
                    case "reload":
                        grid.reloadGrid();
                        break;
                    case "clear":
                        grid.clearGrid();
                        break;
                    case "energy":
                        grid.setInfEnergy();
                        break;
                    case "info":
                        displayInfo();
                        break;
                    case "quit":
                        System.exit(0); // Ends the process
                    //View Menu
                    case "normal":
                        grid.setView(0);
                        break;
                    case "heat":
                        grid.setView(1);
                        break;
                }
            }
        }
    }

    private void displayInfo() {
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
        }

        message = message + "<br>Left Click to Place.<br>" + "Right Click to Delete.<br>"  +
                            "Space to Pause.<br>" + "Right Arrow to Advance Time.<small> (While Paused)</small><br>" +
                            "Scroll to Move Left and Right<br>" + "Control Click to Pan<br>" +
                            "Change Material in Menus.<br><br>" + "Version 1.0";
        JLabel messageLabel = (new JLabel(message));
        messageLabel.setHorizontalAlignment(SwingConstants.CENTER);
        messageLabel.setVerticalAlignment(SwingConstants.CENTER);

        JOptionPane.showMessageDialog(JOptionPane.getFrameForComponent(this), messageLabel, "Sandbox Info", JOptionPane.PLAIN_MESSAGE);
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
}
