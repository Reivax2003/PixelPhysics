package sandbox.pixels;

import java.awt.*;
import java.util.HashMap;

public class Pixel {

    HashMap<String, Integer> properties = new HashMap<>();
    HashMap<String, Integer> state = new HashMap<>();

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

    public void setColor(Color color) {
        this.color = color;
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

    public Pixel changeProperty(String property, int value) {
        properties.replace(property, value);

        // for chaining method calls
        return this;
    }

    public int getState(String state) {
        return this.state.get(state);
    }

    public int getStateOrDefault(String state, int defaultValue) {
        return this.state.getOrDefault(state, defaultValue);
    }

    public void setState(String state, int value) {
        this.state.put(state, value);
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
}
