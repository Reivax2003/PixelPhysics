package sandbox.pixels;

import sandbox.Grid;

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

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public void setX(int x) {
        this.x = x;
    }

    public void setY(int y) {
        this.y = y;
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

    public int[] update(Grid grid) {
        return new int[] {x, y};
    }
}
