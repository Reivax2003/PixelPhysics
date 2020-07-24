package sandbox;

import sandbox.pixels.*;

import javax.swing.*;
import java.awt.event.*;

public class MenuBar extends JMenuBar implements ActionListener {

  public final Pixel[] pixels = { //List of elements in order, 0 and 10 are at ends of lists
      new Electricity(), //Menu 1
      new Sand(),
      new Water(),
      new Soil(),
      new Stone(),
      new Wood(),
      new Fire(),
      new Plant(),
      new AlienPlant(),
      new Metal(),
      new Smoke(), // Menu 2
      new Charcoal(),
      new WetSand(),
      new Steam(),
      new Acid(),
      new Plant3(),
      new Lava(),
      new Slime()
  };

  public int chosen = 1; //currently selected substance

  public MenuBar() {
    JMenu elementsMenu = new JMenu("Elements");
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
    }

    this.add(elementsMenu);
    this.add(elementsMenu2);
  }


  public void actionPerformed(ActionEvent p) { // Detects interaction with menu
    chosen = Integer.parseInt(p.getActionCommand());
  }
}
