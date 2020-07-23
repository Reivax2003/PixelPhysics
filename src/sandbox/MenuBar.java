package sandbox;

import sandbox.pixels.*;

import javax.swing.*;
import java.awt.event.*;

public class MenuBar extends JMenuBar implements ActionListener {
  // private JMenu elementsMenu;

  public final Pixel[] pixels = {
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
    ButtonGroup elementButtons = new ButtonGroup();

    for(int p = 0; p < pixels.length; p ++) {
      JRadioButtonMenuItem button = new JRadioButtonMenuItem(pixels[p].getType());
      elementButtons.add(button);
      String index = String.valueOf(p);
      elementsMenu.add(button);
      button.setAccelerator(KeyStroke.getKeyStroke(index)); //Only works 0 - 9
      button.setActionCommand(index);
      button.addActionListener(this);
    }

    this.add(elementsMenu);
  }


  public void actionPerformed(ActionEvent p) {
    chosen = Integer.parseInt(p.getActionCommand());
  }
}
