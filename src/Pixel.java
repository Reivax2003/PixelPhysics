public class Pixel{
  public boolean isStable;
  public int x, y;
  public String type;
  public Pixel(String t, int xpos, int ypos){
    isStable = false;
    x = xpos;
    y = ypos;
    type = t;
  }
}
