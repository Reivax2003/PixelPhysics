package sandbox;

public class PauseManager {

    private final GameLogic gameLogic;
    private boolean paused = false;

    public PauseManager(GameLogic game) {
        gameLogic = game;
    }

    public boolean isPaused() {
        return paused;
    }

    public void setPaused(boolean paused) {
        gameLogic.setPaused(paused);
        this.paused = paused;
    }

    public void step() {
        gameLogic.setSteps(1);
    }

    public void step(int steps) {
        gameLogic.setSteps(steps);
    }
}
