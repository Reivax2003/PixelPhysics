package sandbox;

import sandbox.pixels.*;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class KeyHandler implements KeyListener {

    private final PauseManager pauseManager;
    public KeyHandler(PauseManager pauseManager) {
        this.pauseManager = pauseManager;
    }

    @Override
    public void keyPressed(KeyEvent e) {
        //right arrow steps forward
        if (e.getExtendedKeyCode() == KeyEvent.VK_RIGHT) {
            pauseManager.step();
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
    }

    @Override
    public void keyTyped(KeyEvent e) {
        char keyChar = e.getKeyChar();

        //space key to pause
        if (keyChar == ' ') {
            pauseManager.setPaused(!pauseManager.isPaused());
        }
    }
}
