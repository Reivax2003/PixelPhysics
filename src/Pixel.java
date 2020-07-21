import java.awt.*;

public class Pixel {

  protected boolean isStable;

  protected int x;
  protected int y;

  public String type;
  protected Color color;

  public Pixel(String type, int xpos, int ypos, Color color){
    this.isStable = false;
    this.x = xpos;
    this.y = ypos;
    this.type = type;
    this.color = color;
  }

  public Color getColor() {
    return color;
  }

  public String getType() {
    return type;
  }

  public void setType(String type) {
    this.type = type;
  }
}
