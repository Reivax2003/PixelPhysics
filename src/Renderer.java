import javax.swing.*;
import java.awt.*;

public class Renderer extends JPanel {
    @Override
    protected void paintComponent(Graphics g) {
        g.setColor(Color.black);
        g.drawString("Hello, World!", 90, 100);
    }
}
