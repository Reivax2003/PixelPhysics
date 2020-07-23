package sandbox;

import sandbox.pixels.*;

import javax.swing.*;
import java.awt.event.*;

public class MenuBar extends JMenuBar implements ActionListener {
  // private JMenu elementsMenu;

  public final Pixel[] pixels = { //List of elements in order, 0 and 10 are at ends of list
          new Sand(0, 0),
          new Sand(0, 0),
          new Water(0, 0),
          new Soil(0, 0),
          new Stone(0, 0),
          new Wood(0, 0),
          new Fire(0, 0),
          new Smoke(0, 0),
          new WetSand(0, 0),
          new Sand(0, 0),
          new Sand(0, 0),
          new Sand(0, 0),
          new Water(0, 0),
          new Soil(0, 0),
          new Stone(0, 0),
          new Wood(0, 0),
          new Fire(0, 0),
          new Smoke(0, 0),
          new WetSand(0, 0),
          new Sand(0, 0),
  };

  public int chosen = 0;

  public MenuBar() {
    JMenu elementsMenu = new JMenu("Elements");
    JMenu elementsMenu2 = new JMenu("Elem cont.");
    ButtonGroup elementButtons = new ButtonGroup();

    for(int p = 0; p < pixels.length; p ++) {
      JRadioButtonMenuItem button = new JRadioButtonMenuItem(pixels[p].getType());
      elementButtons.add(button);
      String index = String.valueOf(p);
      if(p == 0) { // 0 to end of first menu
        elementsMenu.add(button);
        button.setAccelerator(KeyStroke.getKeyStroke(index));
      }
      else if(index.length() == 1) { // 1-9
        elementsMenu.insert(button, p-1);
        button.setAccelerator(KeyStroke.getKeyStroke(index));
      }
      else if(p == 10) { // 10 to end of second menu
        elementsMenu2.add(button);
        button.setAccelerator(KeyStroke.getKeyStroke(index.charAt(1),ActionEvent.SHIFT_MASK));
      }
      else { //11-19
        elementsMenu2.insert(button, p-11);
        button.setAccelerator(KeyStroke.getKeyStroke(index.charAt(1),ActionEvent.SHIFT_MASK));
      }
      button.setActionCommand(index);
      button.addActionListener(this);
    }

    this.add(elementsMenu);
    this.add(elementsMenu2);
  }


  public void actionPerformed(ActionEvent p) {
    chosen = Integer.parseInt(p.getActionCommand());
  }
}
