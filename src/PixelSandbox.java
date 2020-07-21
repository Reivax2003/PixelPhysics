import javax.swing.*;
import java.awt.*;
import java.util.concurrent.*;

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
        update();
        grid.setPixel(23, 23, new Water(23, 23));
    }

    private void initializeGrid() {

        grid = new Grid(100, 50);

        for (int x = 0; x < grid.getWidth(); x++){
            for (int y = 0; y < grid.getHeight(); y++){
                grid.setPixel(x, y, new Pixel("air", x, y, Color.black));
            }
        }
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

    private final ScheduledExecutorService scheduler =Executors.newScheduledThreadPool(1);
    private void update() {

      final Runnable updateFunction = new Runnable() {
        public void run() {
          for (int y = grid.getHeight() -1; y > -1; y--){
              for (int x = 0; x < grid.getWidth(); x++){
                  Pixel curPixel = grid.getPixel(x,y);
                  int[] newPos = curPixel.update(grid);
                  Pixel newPixel = grid.getPixel(newPos[0], newPos[1]);
                  newPixel.x = x;
                  newPixel.y = y;
                  grid.setPixel(x, y, newPixel); //If no movement it gets set to itself twice
                  curPixel.x = newPos[0];
                  curPixel.y = newPos[1];
                  grid.setPixel(newPos[0], newPos[1], curPixel);
                  if(curPixel.getType().equals("sand")){
                    System.out.println(newPos[1]+"NEW from" + y);
                  }
              }
              System.out.println(y);
          }
          renderer.repaint();
        }
      };
      final ScheduledFuture<?> updateTimer = scheduler.scheduleAtFixedRate(updateFunction, 1, 100, TimeUnit.MILLISECONDS);
      scheduler.schedule(new Runnable() { //Kills loop at a minutes
        public void run() { updateTimer.cancel(true); scheduler.shutdown(); }
      }, 5 * 100, TimeUnit.SECONDS);
    }
}
