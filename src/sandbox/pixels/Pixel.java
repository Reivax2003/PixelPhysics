package sandbox.pixels;

import java.awt.*;
import java.io.Serializable;
import java.util.HashMap;

public class Pixel implements Serializable {

    /**
     *
     */
    //not sure what this is, but visual studio code said something about a serialization id
    private static final long serialVersionUID = -812730393170643872L;
    public String type;
    protected Color color;
    protected boolean moved;
    HashMap<String, Integer> properties = new HashMap<>();
    HashMap<String, Integer> state = new HashMap<>();
    public Pixel other = null;

    public Pixel(String type, Color color) {
        this.type = type;
        this.color = color;
        this.moved = false;
        this.setProperty("temperature", 50);
    }

    public Pixel duplicate() {
        Pixel copy = new Pixel(type, color);
        copy.properties = new HashMap<>(this.properties);
        return (copy);
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

    public Pixel addProperty(String property, int value) {
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

    public Pixel getOther(){
        return other;
    }
    public void setOther(Pixel pixel){
        other = pixel;
    }
}
