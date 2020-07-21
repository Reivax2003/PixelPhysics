package sandbox;

import sandbox.pixels.Pixel;

import javax.swing.*;
import java.awt.*;

public class PixelSandbox {
    private final JFrame frame = new JFrame();
    private Renderer renderer;
    private MouseHandler mouseHandler;
    private GameLogic gameLogic;

    public Grid grid;

    public static void main(String[] args) {
        new PixelSandbox();
    }

    private PixelSandbox() {
        initializeGrid();
        initializeRenderer();
        initializeFrame();

        gameLogic = new GameLogic(grid, renderer);
        java.util.Timer timer = new java.util.Timer();

        timer.scheduleAtFixedRate(gameLogic, 0, 100);
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
}
