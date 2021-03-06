package sandbox;

import sandbox.pixels.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.File;



public class CreativeOptions extends JDialog implements ActionListener, FocusListener {

    private CreativeOptions options;

    private JTextField seedField;
    private JTextField energyField;
    private JCheckBox infiniteEnergy;
    private JComboBox<String> worldType;

    String seed = null;
    String energy = null;
    String world = null;
    String confirm = "n";

    private JButton create;
    private JButton cancel;

    private JPanel BuildPanel() {

        JPanel optionPanel = new JPanel(new GridBagLayout());

        GridBagConstraints c = new GridBagConstraints();
        c.weightx = .5;
        c.weighty = .5;
        c.insets = new Insets(5, 0, 5, 0);
        c.fill = GridBagConstraints.HORIZONTAL;

        c.gridy = 0;
        c.gridx = 0;
        optionPanel.add(Box.createHorizontalGlue(), c);
        c.gridx = 2;
        optionPanel.add(Box.createHorizontalGlue(), c);

        c.gridy = 2;
        c.gridx = 1;
        optionPanel.add(Box.createHorizontalGlue(), c);

        JLabel label = new JLabel("New Creative"); //Title
        label.setHorizontalAlignment(SwingConstants.CENTER);
        c.gridx = 1;
        c.gridy = 0;
        optionPanel.add(label,c);

        JPanel subPanel = new JPanel();
        JLabel sLabel = new JLabel("Set Seed");
        sLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        subPanel.add(sLabel);
        seedField = new JTextField("Random",7); //Not really now but it runs default
        seedField.addActionListener(this);
        seedField.addFocusListener(this);
        subPanel.add(seedField);
        subPanel.setLayout(new BoxLayout(subPanel, BoxLayout.PAGE_AXIS));
        c.gridx = 0;
        c.gridy = 1;
        optionPanel.add(subPanel,c);

        subPanel = new JPanel();
        JLabel wLabel = new JLabel("Select World Type");
        wLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        subPanel.add(wLabel);
        String[] options = new String[]{"Random", "Earth", "Alien"};
        worldType = new JComboBox<>(options);
        subPanel.add(worldType);
        subPanel.setLayout(new BoxLayout(subPanel, BoxLayout.PAGE_AXIS));
        c.gridx = 0;
        c.gridy = 2;
        optionPanel.add(subPanel, c);

        subPanel = new JPanel();
        JLabel eLabel = new JLabel("Max Energy");
        eLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        subPanel.add(eLabel);
        energyField = new JTextField("1000",7);
        energyField.addActionListener(this);
        energyField.addFocusListener(this);
        subPanel.add(energyField);
        subPanel.setLayout(new BoxLayout(subPanel, BoxLayout.PAGE_AXIS));
        c.gridx = 2;
        c.gridy = 1;
        optionPanel.add(subPanel,c);

        subPanel = new JPanel(); //Toggleable options
        infiniteEnergy = new JCheckBox("Infinite Energy");
        infiniteEnergy.setAlignmentX(Component.CENTER_ALIGNMENT);
        infiniteEnergy.addActionListener(this);
        subPanel.add(infiniteEnergy);
        subPanel.setLayout(new BoxLayout(subPanel, BoxLayout.PAGE_AXIS));
        c.gridx = 1;
        c.gridy = 1;
        optionPanel.add(subPanel,c);

        return optionPanel;
    }

    public String[] getOptions(){

        setVisible(true);

        return new String[] {seed, energy, world, confirm};
    }

    public CreativeOptions(Component frameUp, Component location) {
        super(JOptionPane.getFrameForComponent(frameUp),"New Creative Sandbox",true);

        JPanel  optionPanel = BuildPanel();


        create = new JButton("Create");
        create.addActionListener(this);


        cancel = new JButton("Cancel");
        cancel.addActionListener(this);

        JPanel controlPanel = new JPanel();
        controlPanel.setLayout(new BoxLayout(controlPanel, BoxLayout.LINE_AXIS));
        controlPanel.add(Box.createHorizontalGlue());
        controlPanel.add(create);
        controlPanel.add(Box.createHorizontalGlue());
        //controlPanel.add(Box.createRigidArea(new Dimension(10, 0)));
        controlPanel.add(cancel);
        controlPanel.add(Box.createHorizontalGlue());



        Container contentPane = getContentPane();
        contentPane.add(optionPanel, BorderLayout.CENTER);
        contentPane.add(controlPanel, BorderLayout.PAGE_END);

        pack();
        setLocationRelativeTo(location);

    }

    public void actionPerformed(ActionEvent a) {
        Object source = a.getSource();


        if (source == seedField) {
            String tSeed = seedField.getText();
            try {
                Integer.parseInt(tSeed);
                seed = tSeed;
            } catch (NumberFormatException e) {
                if(!tSeed.equals("Random")) { // Assume default is not attempted seed
                    seedField.setText("Invalid Seed");
                }
                seed = null;
            }
        } else if (source == energyField) {
            String tEnergy = energyField.getText();
            try {
                int e = Integer.parseInt(tEnergy);
                if(e < 0) { Integer.parseInt("A");} //Throws exception to trigger catch
                energy = tEnergy;
            } catch (NumberFormatException e) {
                energyField.setText("Invalid Value");
                energy = null;
            }

        } else if (source == infiniteEnergy) {
            if(infiniteEnergy.isSelected()) {
                energyField.setEditable(false);
                energy = "-1";
            } else {
                energyField.setEditable(true);

                String tEnergy = energyField.getText();
                try {
                    int e = Integer.parseInt(tEnergy);
                    if(e < 0) { Integer.parseInt("A");} //Throws exception to trigger catch
                    energy = tEnergy;
                } catch (NumberFormatException e) {
                    seedField.setText("Invalid Value");
                    energy = null;
                }
            }
        } else if (source == cancel) {
            setVisible(false);
        } else if (source == create) {

            String tSeed = seedField.getText(); //Get values on create
            world = (String) worldType.getSelectedItem();
            try {
                Integer.parseInt(tSeed);
                seed = tSeed;
            } catch (NumberFormatException e) {
                seed = null;
            }
            if(infiniteEnergy.isSelected()) {
                energy = "-1";
            } else {
                String tEnergy = energyField.getText();
                try {
                    int e = Integer.parseInt(tEnergy);
                    if(e < 0) { Integer.parseInt("A");} //Throws exception to trigger catch
                    energy = tEnergy;
                } catch (NumberFormatException e) {
                    energy = null;
                }
            }

            confirm = "y";
            setVisible(false);
        }
    }

    public void focusGained(FocusEvent f) {}

    public void focusLost(FocusEvent f) { // Runs action code when deselected
        Object source = f.getSource();
        if (source == seedField) {
            String tSeed = seedField.getText();
            try {
                Integer.parseInt(tSeed);
                seed = tSeed;
            } catch (NumberFormatException e) {
                if(!tSeed.equals("Random")) { // Assume default is not attempted seed
                    seedField.setText("Invalid Seed");
                }
                seed = null;
            }
        } else if (source == energyField) {
            String tEnergy = energyField.getText();
            try {
                int e = Integer.parseInt(tEnergy);
                if(e < 0) { Integer.parseInt("A");} //Throws exception to trigger catch
                energy = tEnergy;
            } catch (NumberFormatException e) {
                energyField.setText("Invalid Value");
                energy = null;
            }

        }
    }
}
