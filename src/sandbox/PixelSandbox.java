package sandbox;

import sandbox.pixels.Air;

import javax.swing.*;
import java.awt.*;
import java.io.File;

public class PixelSandbox {
    private final JFrame frame = new JFrame();
    public Grid grid;
    private Renderer renderer;
    private MenuBar menuBar;
    private MouseHandler mouseHandler;
    private KeyHandler keyHandler;
    private GameLogic gameLogic;
    private PauseManager pauseManager;
    private PeopleManager peopleManager;

    private PixelSandbox(String[] args) {
        initializeGrid();
        if (args.length > 0) {
          File file = new File(args[0]);
          grid.loadGrid(file);
        }

        initializeComponents();
        initializeFrame();
        initializeLogic();
    }

    public static void main(String[] args) {
        new PixelSandbox(args);
    }

    private void initializeGrid() {

        // initialize a new grid
        grid = new Grid(200, 100);

        // set all spaces to air
        grid.fillGrid(new Air());

        //generate some simple terrain
        grid.worldGen(70268);
    }

    private void initializeFrame() {

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new GridLayout());
        frame.setSize(1000, 500);
        frame.setBackground(Color.lightGray);

        frame.setJMenuBar(menuBar);
        frame.add(renderer);

        frame.setVisible(true);
    }

    private void initializeComponents() {

        //menu bar
        menuBar = new MenuBar(grid);
        //calculations for people
        peopleManager = new PeopleManager(grid);
        //simulation
        renderer = new Renderer(grid, peopleManager, 100, 50);
    }

    private void initializeLogic() {

        //game mechanics
        gameLogic = new GameLogic(grid, renderer, peopleManager);
        pauseManager = new PauseManager(gameLogic);

        //handles user inputs
        keyHandler = new KeyHandler(pauseManager);
        mouseHandler = new MouseHandler(renderer, grid, menuBar);

        //add listeners for user inputs
        renderer.addMouseMotionListener(mouseHandler);
        renderer.addMouseListener(mouseHandler);
        renderer.addMouseWheelListener(mouseHandler);
        frame.addKeyListener(keyHandler);

        java.util.Timer timer = new java.util.Timer();

        //start game loop
        timer.scheduleAtFixedRate(gameLogic, 0, 100);
    }
}
