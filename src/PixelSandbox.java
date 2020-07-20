import javax.swing.*;
import java.awt.*;

public class PixelSandbox {
    JFrame frame = new JFrame();
    Renderer renderer = new Renderer(100, 50);
    MouseHandler mouseHandler = new MouseHandler(renderer, 100, 50);

    public static void main(String[] args) {
        new PixelSandbox();
    }

    private PixelSandbox() {
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new GridLayout());
        frame.setSize(1500, 750);
        frame.setBackground(Color.black);

        frame.add(renderer);

        frame.setVisible(true);

        renderer.addMouseMotionListener(mouseHandler);
    }
}
