package sandbox;
import sandbox.pixels.*;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.Arrays;

public class KeyHandler implements KeyListener{

    public int chosen = 0;
    public final Pixel[] pixels = {
        new Sand(0,0),
        new Water(0,0),
        new Soil(0,0),
        new Stone(0,0),
        new Wood(0,0),
        new Fire(0,0),
    };

    public void keyHandler(){}

    public void keyPressed(KeyEvent e){}
    public void keyReleased(KeyEvent e){}
    public void keyTyped(KeyEvent e){
        char[] nums = {'0','1','2','3','4','5','6','7','8','9'};
        char keyChar = e.getKeyChar();

        if (Arrays.asList(nums).contains(keyChar)) {
            chosen = Integer.parseInt(String.valueOf(keyChar));
        }
    }
}