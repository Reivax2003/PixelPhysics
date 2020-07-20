import javax.swing.*;
import java.awt.*;

public class Renderer extends JPanel {
    @Override
    protected void paintComponent(Graphics g) {
        //g.drawString("Hello, World!", 90, 100);
        for (int x = 0; x < PixelSandbox.grid.length; x++){
            for (int y = 0; y < PixelSandbox.grid.length; y++){
                if (PixelSandbox.grid[x][y].type == "sand"){
                    g.setColor(Color.black);
                    g.drawRect(x, y, PixelSandbox.pixelSize, PixelSandbox.pixelSize);
                }
            }
        }
    }
}
