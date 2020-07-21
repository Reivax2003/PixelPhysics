import java.awt.*;

public class Fire extends Pixel {

    public Sand(int xpos, int ypos) {
        super("sand", xpos, ypos, Color.yellow);
    }

    public int[] update(Grid grid) {
        int[] newPositions;

        List<int> positions = new ArrayList<int>();

        x = super.x;
        y = super.y;

        if (grid[x-1][y-1].getType() == "wood"){
            positions.add(x-1);
            positions.add(y);
            positions.add(x);
            positions.add(y-1);
        }
        if (grid[x+1][y-1].getType() == "wood"){
            positions.add(x+1);
            positions.add(y);
            positions.add(x);
            positions.add(y-1);
        }
        if (grid[x-1][y+1].getType() == "wood") {
            positions.add(x-1);
            positions.add(y);
            positions.add(x);
            positions.add(y+1);
        }
        if (grid[x+1][y+1].getType() == "wood") {
            positions.add(x);
            positions.add(y+1);
            positions.add(x+1);
            positions.add(y);
        }
        if (grid[x-1][y].getType() == "wood") {
            positions.add(x-1);
            positions.add(y+1);
            positions.add(x);
            positions.add(y);
        }
        if (grid[x+1][y].getType() == "wood") {
            positions.add(x+1);
            positions.add(y+1);
            positions.add(x);
            positions.add(y);
        }
        if (grid[x][y+1].getType() == "wood") {
            positions.add(x-1);
            positions.add(y+1);
            positions.add(x+1);
            positions.add(y+1);
        }
        if (grid[x1][y-1].getType() == "wood") {
            positions.add(x-1);
            positions.add(y-1);
            positions.add(x+1);
            positions.add(y-1);
            positions.add(x);
            positions.add(y);
        }
        if (math.random() > 0.5){
            positions.add(x)
            positions.add(y)
        }
        if (math.random() > 0.5){
            positions.add(x)
            positions.add(y+1)
        }

        boolean stays == false;
        int count = 0;
        for (int i = 0; i < positions.size(); i+= 2){
            if (check(grid, positions[i], positions[i+1])){
              count++;
            }
            else{
              positions.remove(i+1);
              positions.remove(i);
            }
        }
        newPositions = new int[count];

        for (int i = 0; i < positions.size(); i++){
            newPositions[i] = positions[i];
        }

        return newPositions;
    }

    public boolean check(Grid grid, int x, int y){
        return (grid[x][y].type == air)
    }
}
