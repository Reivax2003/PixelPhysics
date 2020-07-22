package sandbox.pixels;

import sandbox.Grid;

import java.awt.*;
import java.util.HashMap;

public class Pixel {

    HashMap<String, Integer> properties = new HashMap<>();

    protected int x;
    protected int y;

    public String type;
    protected Color color;

    protected boolean moved;

    public Pixel(String type, int xpos, int ypos, Color color) {
        this.x = xpos;
        this.y = ypos;
        this.type = type;
        this.color = color;
        this.moved = false;
    }

    public Pixel duplicate() {
        Pixel copy = new Pixel(type, x, y, color);
        copy.properties = this.properties;
        return (copy);
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

    protected Pixel setProperty(String property, int value) {
        properties.put(property, value);

        // for chaining method calls
        return this;
    }

    public int getProperty(String property) {
        return properties.get(property);
    }

    public int getPropOrDefault(String property, int def) {
        return properties.getOrDefault(property, def);
    }

    public boolean hasProperty(String property) {
        return properties.containsKey(property);
    }

    public boolean hasMoved() {
        return moved;
    }

    public void setMoved(boolean truefalse) {
        this.moved = truefalse;
    }

    public void update(Grid grid) {

    }
}
