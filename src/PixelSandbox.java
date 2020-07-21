import javax.swing.*;
import java.awt.*;

public class PixelSandbox {
    private final JFrame frame = new JFrame();
    private Renderer renderer;
    private MouseHandler mouseHandler;

    public Grid grid;

    public static void main(String[] args) {
        new PixelSandbox();
    }

    private PixelSandbox() {
        initializeGrid();
        initializeRenderer();
        initializeFrame();
    }

    private void initializeGrid() {

        grid = new Grid(100, 50);

        for (int x = 0; x < grid.getWidth(); x++){
            for (int y = 0; y < grid.getHeight(); y++){
                grid.setPixel(x, y, new Pixel("air", x, y, Color.black));
            }
        }

        // test
        grid.setPixel(25, 25, new Sand(25, 25));
    }

    private void initializeFrame() {

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new GridLayout());
        frame.setSize(1000, 500);
        frame.setBackground(Color.white);

        frame.add(renderer);

        frame.setVisible(true);

        renderer.addMouseMotionListener(mouseHandler);
        renderer.addMouseListener(mouseHandler);
    }

    private void initializeRenderer() {

        renderer = new Renderer(grid);
        mouseHandler = new MouseHandler(renderer, grid);
    }

    private void update() {
      for (int y = grid.getHeight() -1; y > -1; y--){
          for (int x = 0; y < grid.getWidth(); x++){
              curPixel = grid.getPixel(x,y);
              newPos = curPixel.update(grid)
              grid.setPixel(x, y, grid.getPixel(newPos[0], newPos[1])); //If no movement it gets set to itself twice
              grid.setPixel(newPos[0], newPos[1], curPixel);
          }
      }
    }
}
