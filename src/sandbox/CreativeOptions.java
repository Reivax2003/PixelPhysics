package sandbox;

import sandbox.pixels.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;



public class CreativeOptions extends JDialog implements ActionListener {

    private CreativeOptions options;

    private JTextField seedField;
    private JTextField energyField;
    private JCheckBox infiniteEnergy;

    String seed = null;
    String energy = null;
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
        label.setAlignmentX(Component.CENTER_ALIGNMENT);
        c.gridx = 1;
        c.gridy = 0;
        optionPanel.add(label,c);

        JPanel subPanel = new JPanel();
        JLabel sLabel = new JLabel("Set Seed");
        sLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        subPanel.add(sLabel);
        seedField = new JTextField("Random",7); //Not really now but it runs default
        seedField.addActionListener(this);
        subPanel.add(seedField);
        subPanel.setLayout(new BoxLayout(subPanel, BoxLayout.PAGE_AXIS));
        c.gridx = 0;
        c.gridy = 1;
        optionPanel.add(subPanel,c);

        subPanel = new JPanel();
        JLabel eLabel = new JLabel("Max Energy");
        eLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        subPanel.add(eLabel);
        energyField = new JTextField("1000",7);
        energyField.addActionListener(this);
        subPanel.add(energyField);
        subPanel.setLayout(new BoxLayout(subPanel, BoxLayout.PAGE_AXIS));
        c.gridx = 2;
        c.gridy = 1;
        optionPanel.add(subPanel,c);

        subPanel = new JPanel(); //Toggleable options
        subPanel.setLayout(new FlowLayout());
        infiniteEnergy = new JCheckBox("Infinite Energy");
        infiniteEnergy.addActionListener(this);
        subPanel.add(infiniteEnergy);
        c.gridx = 1;
        c.gridy = 1;
        optionPanel.add(subPanel,c);

        return optionPanel;
    }

    public String[] getOptions(){

        setVisible(true);

        return new String[] {seed,energy,confirm};
    }

    public CreativeOptions(Component frameUp, Component location) {
        super(JOptionPane.getFrameForComponent(frameUp),"New Creative Sandbox",true);

        JPanel  optionPanel = BuildPanel();


        create = new JButton("Create");
        create.addActionListener(this);
        getRootPane().setDefaultButton(create);

        cancel = new JButton("Cancel");
        cancel.addActionListener(this);

        JPanel controlPanel = new JPanel();
        controlPanel.setLayout(new BoxLayout(controlPanel, BoxLayout.LINE_AXIS));
        controlPanel.add(Box.createHorizontalGlue());
        controlPanel.add(create);
        controlPanel.add(Box.createRigidArea(new Dimension(0, 0)));
        controlPanel.add(cancel);


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
                seedField.setText("Invalid Seed");
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
            confirm = "y";
            setVisible(false);
        }
    }
}
