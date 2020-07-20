public class Sand extends Pixel{
  public Sand(int xpos, int ypos){
    super("sand", xpos, ypos);
  }
  public int[] update(Pixel[][] grid){
    int[] newPosition = new int[]{super.x, super.y};

    if (grid[super.x][super.y-1].type == "air"){
      newPosition[0] = super.x;
      newPosition[1] = super.y-1;
    }
    return(newPosition);
  }
}
