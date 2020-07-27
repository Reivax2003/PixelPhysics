package sandbox;

import sandbox.pixels.*;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MenuBar extends JMenuBar implements ActionListener {

    private final Grid grid;

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
            new Slime()
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
    };

    private ButtonGroup elementButtons;

    public int chosen = 1; //currently selected substance

    public MenuBar(Grid grid) {
        this.grid = grid;

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

        // Build settings/control/options decide later menu
        JMenu controlMenu = new JMenu("Control");
        JMenuItem reset = new JMenuItem("Reset");
        reset.setActionCommand("reset");
        reset.addActionListener(this);
        controlMenu.add(reset);

        // Add control menu to bar
        this.add(controlMenu);
    }


    public void actionPerformed(ActionEvent a) { // Detects interaction with menu
        String action = a.getActionCommand();
        try { //Assumes all numeric action events are chosing type
            chosen = Integer.parseInt(action);
        } catch (NumberFormatException e) {
            if (action == "reset") {
                grid.fillGrid(new Air());
            }
        }
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
