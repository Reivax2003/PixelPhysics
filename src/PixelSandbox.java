package src;

import javax.swing.*;
import java.awt.*;

public class PixelSandbox {
    JFrame frame = new JFrame();
    Renderer renderer = new Renderer();

    public static void main(String[] args) {
        new PixelSandbox();
    }

    private PixelSandbox() {
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new GridLayout());
        frame.setSize(250, 250);

        frame.add(renderer);

        frame.setVisible(true);
    }
}
