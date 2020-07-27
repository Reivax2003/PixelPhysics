package sandbox;

import sandbox.pixels.Pixel;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;

public class Renderer extends JPanel {

    private final Grid grid;
    public boolean slimeExists = false;
    public int slimeGoalX;
    public int slimeGoalY;

    public float hOffset = 0;
    public boolean bool = false;

    private BufferedImage pausedImage;
    private BufferedImage unpausedImage;

    private boolean paused = false;
    private boolean imagesFailedLoading;

    public Renderer(Grid grid) {
        this.grid = grid;
        try {
            File pausedFile = new File(this.getClass().getClassLoader().getResource("assets/paused.png").getFile());
            File unpausedFile = new File(this.getClass().getClassLoader().getResource("assets/unpaused.png").getFile());
            pausedImage = ImageIO.read(pausedFile);
            unpausedImage = ImageIO.read(unpausedFile);
            imagesFailedLoading = false;
        } catch (Exception e) {
            imagesFailedLoading = true;
            System.out.println("Pause images failed loading, disabled indicator.");
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        int gridWidth = grid.getWidth();
        int gridHeight = grid.getHeight();
        // set sandbox.pixels per square to the smallest dimension, so
        int pixelsPerSquare = Math.min(getWidth() / gridWidth, getHeight() / gridHeight);

        // get the size of the unused margins (getWidth - used space), then divide it by two to get the center
        int xOffset = (getWidth() - gridWidth * pixelsPerSquare) / 2;
        int yOffset = (getHeight() - gridHeight * pixelsPerSquare) / 2;

        for (int x = 0; x < grid.getWidth(); x++) {
            for (int y = 0; y < grid.getHeight(); y++) {
                Pixel pixel = grid.getPixel(x, y);

                Color color = pixel.getColor();
                if (pixel.getStateOrDefault("recovering", 0) != 0) {
                    color = Color.yellow.darker();
                } else if (pixel.getStateOrDefault("conducting", 0) != 0) {
                    color = Color.yellow;
                }
                if (pixel.getStateOrDefault("flower", 0) != 0) {
                    int flower = pixel.getState("flower");

                    Color[] pastels = new Color[]{
                            new Color(255, 107, 255),
                            new Color(255, 85, 85),
                            new Color(255, 165, 48),
                            new Color(225, 255, 54),
                            new Color(54, 175, 255)
                    };

                    //if flower color has not yet been set, set it
                    flower = flower != -1 ? flower : (int) (Math.random() * pastels.length) + 1;
                    pixel.setState("flower", flower);
                    color = pastels[flower - 1];
                }
                g.setColor(bool ? nudgeColor(color) : color);
                g.fillRect(x * pixelsPerSquare + xOffset, y * pixelsPerSquare + yOffset, pixelsPerSquare, pixelsPerSquare);
            }
        }

        if (slimeExists) {
            g.setColor(Color.red);
            g.drawRect(slimeGoalX * pixelsPerSquare + xOffset, slimeGoalY * pixelsPerSquare + yOffset, pixelsPerSquare, pixelsPerSquare);
        }

        if (!imagesFailedLoading) {
            if (paused) {
                g.drawImage(pausedImage, xOffset, yOffset + 2, null);
            } else {
                g.drawImage(unpausedImage, xOffset + 2, yOffset + 2, null);
            }
        }
    }

    public void setPaused(boolean paused) {
        this.paused = paused;
    }

    private Color nudgeColor(Color color) {
        float[] hsb = Color.RGBtoHSB(color.getRed(), color.getGreen(), color.getBlue(), null);
        float h = hsb[0];
        h = (h + hOffset) % 1.0f;
        return Color.getHSBColor(h, hsb[1], hsb[2]);
    }
}
