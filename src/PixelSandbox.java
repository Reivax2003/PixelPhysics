import javax.swing.*;
import java.awt.*;

public class PixelSandbox {
    JFrame frame = new JFrame();
    Renderer renderer = new Renderer(100, 50);
    MouseHandler mouseHandler = new MouseHandler(renderer, 100, 50);
    public static Pixel[][] grid;
    static int w = 500;
    static int h = 500;
    static int pixelSize = 10;

    public static void main(String[] args) {

        grid = new Pixel[w/pixelSize][h/pixelSize];
        for (int x = 0; x < w/pixelSize; x++){
          for (int y = 0; y < h/pixelSize; y++){
            grid[x][y] = new Pixel("air", x, y);
          }
        }
        grid[25][25] = new Sand(25, 25);

        new PixelSandbox();
    }

    private PixelSandbox() {
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new GridLayout());
        frame.setSize(w, h);
        frame.setBackground(Color.black);

        frame.add(renderer);

        frame.setVisible(true);

        renderer.addMouseMotionListener(mouseHandler);
        renderer.addMouseListener(mouseHandler);
    }
}
