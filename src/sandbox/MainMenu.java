package sandbox;

import sandbox.pixels.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

public class MainMenu extends JPanel implements ActionListener {

    JFrame frame = new JFrame();
    private final JFileChooser fileChooser = new JFileChooser();

    private MainMenu() {
      fileChooser.setCurrentDirectory(new File(System.getProperty("user.dir"))); //Sets file viewer to program directory

      JLabel title = new JLabel("Pixel Physics",JLabel.CENTER); //Title
      title.setFont(new Font(title.getFont().getName(), Font.PLAIN, 100));
      title.setHorizontalAlignment(SwingConstants.CENTER);
      title.setVerticalAlignment(SwingConstants.CENTER);
      title.setAlignmentX(Component.CENTER_ALIGNMENT);
      title.setPreferredSize(new Dimension(500,200));
      title.setMaximumSize(new Dimension(Short.MAX_VALUE,Short.MAX_VALUE));

      add(title);

      JPanel buttonPanel = new JPanel(new GridBagLayout()); //Pannel for button organisation
      GridBagConstraints c = new GridBagConstraints();

      c.weightx = .5;
      c.weighty = .5;
      c.insets = new Insets(5, 20, 5, 20);

      JButton creative = new JButton("Creative"); //Freeplay option
      creative.setAlignmentX(Component.CENTER_ALIGNMENT);
      creative.setActionCommand("creative");
      creative.addActionListener(this);
      c.gridx = 0;
      c.gridy = 0;

      buttonPanel.add(creative,c);

      add(Box.createRigidArea(new Dimension(0,10))); // Spacing

      JButton load = new JButton("Load"); //Load file option
      load.setAlignmentX(Component.CENTER_ALIGNMENT);
      load.setActionCommand("load");
      load.addActionListener(this);
      c.gridy = 1;

      buttonPanel.add(load,c);

      add(Box.createRigidArea(new Dimension(0,10))); // Spacing

      JButton exit = new JButton("Exit"); //Exit option
      exit.setAlignmentX(Component.CENTER_ALIGNMENT);
      exit.setActionCommand("exit");
      exit.addActionListener(this);
      c.gridy = 2;

      buttonPanel.add(exit,c);

      add(buttonPanel); //Add buttons

      add(Box.createRigidArea(new Dimension(0,10))); // Spacing

      //Sets vertical box layout
      setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));

      displayFrame(this);
    }

    public static void main(String[] args) {
        new MainMenu();

    }

    private void displayFrame(MainMenu menu) {
      frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
      frame.setLayout(new GridLayout());
      frame.setSize(1000, 500);
      frame.setBackground(Color.lightGray);

      frame.add(menu);

      frame.setVisible(true);
    }

    public void actionPerformed(ActionEvent a) { // Detects interaction with menu
        String action = a.getActionCommand();
        String[] args = new String[] {}; // By default has grid create its new file

        if (action.equals("exit")) {
            System.exit(0); // Ends the process
        }
        if (action.equals("load")) {
            int option = fileChooser.showOpenDialog(this);
            if(option == JFileChooser.APPROVE_OPTION){
                args = new String[] {fileChooser.getSelectedFile().getPath()}; // Sets arguments to chosen file
            }
            else {
                return; //Stops path if no file is selected
            }
        }
        else if (action.equals("creative")) {
            args = new String[] {}; //Sets default arguments, does nothing
        }

        PixelSandbox.main(args); // Most ways out of the menu will eventually require loading a sandbox
        frame.dispose(); // Closes menu
    }
}
