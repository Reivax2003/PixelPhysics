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
      fileChooser.setCurrentDirectory(new File(System.getProperty("user.dir")));

      JLabel title = new JLabel("Pixel Physics",JLabel.CENTER);
      title.setFont(new Font(title.getFont().getName(), Font.PLAIN, 100));
      title.setHorizontalAlignment(SwingConstants.CENTER);
      title.setVerticalAlignment(SwingConstants.CENTER);
      title.setAlignmentX(Component.CENTER_ALIGNMENT);

      add(title);

      JButton creative = new JButton("Creative");
      creative.setAlignmentX(Component.CENTER_ALIGNMENT);
      creative.setActionCommand("creative");
      creative.addActionListener(this);

      add(creative);

      JButton load = new JButton("Load");
      load.setAlignmentX(Component.CENTER_ALIGNMENT);
      load.setActionCommand("load");
      load.addActionListener(this);

      add(load);

      JButton exit = new JButton("Exit");
      exit.setAlignmentX(Component.CENTER_ALIGNMENT);
      exit.setActionCommand("exit");
      exit.addActionListener(this);

      add(exit);

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
        String[] args = new String[] {};

        if (action.equals("exit")) {
            System.exit(0);
        }
        if (action.equals("load")) {
            int option = fileChooser.showOpenDialog(this);
            if(option == JFileChooser.APPROVE_OPTION){
                args = new String[] {fileChooser.getSelectedFile().getPath()};
            }
            else {
                return;
            }
        }
        else if (action.equals("creative")) {
            args = new String[] {};
        }

        PixelSandbox.main(args);
        frame.dispose();
    }
}
