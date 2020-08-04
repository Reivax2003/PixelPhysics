package sandbox;

import sandbox.pixels.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.ArrayList;

public class MainMenu extends JPanel implements ActionListener {

    private Object[] saveSlots = {
      "slot 0",
      "slot 1",
      "slot 2",
      "slot 3",
      "slot 4",
      "slot 5",
      "slot 6",
      "slot 7",
      "slot 8",
      "slot 9",
      "load from file",
    };

    private Object[] campaignLevels = {
      "level 1",
      "level 2",
      "level 3",
      "level 4",
      "level 5",
      "level 6",
      "level 7",
      "level 8",
      "level 9",
    };

    private JFrame frame = new JFrame();
    private final JFileChooser fileChooser = new JFileChooser();

    private MainMenu() {
      fileChooser.setCurrentDirectory(new File(System.getProperty("user.dir"))); //Sets file viewer to program directory

      JLabel title = new JLabel("Pixel Physics",JLabel.CENTER); //Title
      title.setFont(new Font(title.getFont().getName(), Font.BOLD + Font.ITALIC, 100));
      title.setHorizontalAlignment(SwingConstants.CENTER);
      title.setVerticalAlignment(SwingConstants.CENTER);
      title.setAlignmentX(Component.CENTER_ALIGNMENT);
      title.setPreferredSize(new Dimension(500,200));
      title.setMaximumSize(new Dimension(Short.MAX_VALUE,Short.MAX_VALUE));

      add(title);

      add(Box.createRigidArea(new Dimension(0,20))); // Spacing

      JPanel buttonPanel = new JPanel(new GridBagLayout()); //Pannel for button organisation
      GridBagConstraints c = new GridBagConstraints();

      c.weightx = .5;
      c.weighty = .5;
      c.insets = new Insets(5, 0, 5, 0);
      c.fill = GridBagConstraints.HORIZONTAL;
      for(int y = 0; y < 3; y++){
          c.gridy = y;

          c.gridx = 0;
          buttonPanel.add(Box.createHorizontalGlue(), c);
          c.gridx = 2;
          buttonPanel.add(Box.createHorizontalGlue(), c);
      }

      JButton creative = new JButton("Creative"); //Freeplay option
      creative.setAlignmentX(Component.CENTER_ALIGNMENT);
      creative.setActionCommand("creative");
      creative.addActionListener(this);
      c.gridx = 1;
      c.gridy = 0;

      buttonPanel.add(creative,c);

      JButton campaign = new JButton("Campaign"); //Campaign option
      campaign.setAlignmentX(Component.CENTER_ALIGNMENT);
      campaign.setActionCommand("campaign");
      campaign.addActionListener(this);
      c.gridy += 1;

      buttonPanel.add(campaign,c);

      JButton load = new JButton("Load"); //Load file option
      load.setAlignmentX(Component.CENTER_ALIGNMENT);
      load.setActionCommand("load");
      load.addActionListener(this);
      c.gridy += 1;

      buttonPanel.add(load,c);

      JButton exit = new JButton("Exit"); //Exit option
      exit.setAlignmentX(Component.CENTER_ALIGNMENT);
      exit.setActionCommand("exit");
      exit.addActionListener(this);
      c.gridy += 1;

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

        switch (action) {
            case "exit": // Ends the process
                System.exit(0); 
            case "load": // Load a saved file
                args = loadChoice();
                if (args == null) {
                    return; // Return if no file is chosen
                }
                break;
            case "campaign": // Load a premade level with goals
                args = campaignChoice();
                if (args == null) {
                    return; // Return if no file is chosen
                }
                break;
            case "creative": // Create a freeplay world
                args = creativeDialog();
                if (args == null) {return;} // Cancelled
                if (args[0] == null && args[1] == null && args[2] == "Random") {args = new String[] {};} // No args if no inputs
                break;
        }
        frame.dispose(); // Closes menu
        PixelSandbox.main(args); // Most ways out of the menu will eventually require loading a sandbox

    }

    private String[] loadChoice() {
        ArrayList<Object> validChoices = new ArrayList<>();
        for (Object choice : saveSlots) { // Checks whether saves exist in slots
            try {
                Integer.parseInt(((String)choice).substring(5));
                if(new File("save/slot"+((String)choice).substring(5)+".lvl").exists()){
                    validChoices.add(choice);
                }
            } catch (NumberFormatException e) {
                validChoices.add(choice);
            }
        }
        Object[] validChoicesAr = validChoices.toArray();

        Object choiceObj = JOptionPane.showInputDialog(frame,"Choose save slot:","Load Saved Sandbox",JOptionPane.PLAIN_MESSAGE,null,validChoicesAr,validChoicesAr[validChoicesAr.length-1]);
        if (choiceObj == null) { // Cancelled or closed
            return null; // Returns null if no choice
        }

        String choice = (String) choiceObj;

        try {
            Integer.parseInt(choice.substring(5)); // Tests whether save slot is chosen
            return new String[] {"save/slot"+choice.substring(5)+".lvl"}; // Returns chosen file
        }
        catch (NumberFormatException e) {
            int option = fileChooser.showOpenDialog(this);
            if( option == JFileChooser.APPROVE_OPTION) {
                return new String[] {fileChooser.getSelectedFile().getPath()}; // Returns chosen file
            }
            else {
                return null; // Returns null if no choice
            }
        }

    }

    private String[] campaignChoice() {
        ArrayList<Object> validChoices = new ArrayList<>();
        for (Object choice : campaignLevels) { // Checks whether levels exist in slots
            Integer.parseInt(((String)choice).substring(6));
            if(new File("campaign/level"+((String)choice).substring(6)+".lvl").exists()){
                validChoices.add(choice);
            }
        }
        Object[] validChoicesAr = validChoices.toArray();

        Object choiceObj = JOptionPane.showInputDialog(frame,"Choose level:","Start Campaign Level",JOptionPane.PLAIN_MESSAGE,null,validChoicesAr,validChoicesAr[0]);
        if (choiceObj == null) { // Cancelled or closed
            return null; // Returns null if no choice
        }

        String choice = (String) choiceObj;
        return new String[] {"campaign/level"+choice.substring(6)+".lvl"}; // Returns chosen level
    }

    private String[] creativeDialog() { // Displays menu allowing options for a creative level

        CreativeOptions optionDialog = new CreativeOptions(frame, this);
        String[] options = optionDialog.getOptions();

        if(options[3] != "y") { // If the menu is closed and the create button is not pressed
          return null; // Returns null if cancelled
        }

        return new String[] {options[0],options[1], options[2]};

    }
}
