import java.awt.*;

public class Sand extends Pixel implements Granular {

    public Sand(int xpos, int ypos) {
        super("sand", xpos, ypos, Color.yellow);
    }
}
