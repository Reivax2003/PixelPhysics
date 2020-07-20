import java.awt.*; 
import javax.swing.*; 
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

public class test extends JFrame{

    public static void main(String args[]){
        Frame f = new Frame("example");
        Canvas testCanvas = new Canvas();
        f.add(testCanvas);
        f.setSize(400, 400);
        f.setVisible(true);
        
    }
}