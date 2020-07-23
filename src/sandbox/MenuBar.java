package sandbox;

import sandbox.pixels.*;

import javax.swing.*;

public class MenuBar extends JMenuBar {
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

  public MenuBar() {
    JMenu elementsMenu = new JMenu("Elements");
    ButtonGroup elementButtons = new ButtonGroup();

    for(int p = 0; p < pixels.length; p ++) {
      MenuElement button = new MenuElement(pixels[p]);
      elementButtons.add(button);
      elementsMenu.add(button);
    }

    this.add(elementsMenu);
  }

  private class MenuElement extends JRadioButtonMenuItem {
    public MenuElement(Pixel element) {
      this.setText(element.getType());
    }
  }
}
