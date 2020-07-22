package sandbox;

import sandbox.pixels.Pixel;
import sandbox.pixels.Sand;

public class Grid {
    private final Pixel[][] grid;

    public Grid(int width, int height) {
        grid = new Pixel[width][height];
    }

    public Pixel getPixel(int x, int y) {
        return grid[x][y];
    }

    public void setPixel(int x, int y, Pixel pixel) {
        this.grid[x][y] = pixel;
    }

    public int getWidth() {
        return grid.length;
    }

    public int getHeight() {
        return grid[0].length;
    }

    public Pixel getPixelLeft(int x, int y) {
        return grid[x - 1][y];
    }

    public Pixel getPixelRight(int x, int y) {
        return grid[x + 1][y];
    }

    public Pixel getPixelUp(int x, int y) {
        return grid[x][y - 1];
    }

    public Pixel getPixelDown(int x, int y) {
        return grid[x][y + 1];
    }

    public void swapPositions(int x1, int y1, int x2, int y2) {
        Pixel position1 = grid[x1][y1];
        Pixel position2 = grid[x2][y2];

        position1.setX(x2);
        position1.setY(y2);

        position2.setX(x1);
        position2.setY(y1);

        grid[x1][y1] = position2;
        grid[x2][y2] = position1;
    }

    public void drawLine(int x1, int y1, int x2, int y2, Pixel pixel) {
        double dirX = x2-x1, dirY = y2-y1;
        double length = Math.sqrt(dirX*dirX+dirY*dirY);
        dirX/=length;
        dirY/=length;

        for (double i = 0; i < length; i++) {
            int x = (int)(x1+dirX*i), y = (int)(y1+dirY*i);            
            Pixel p = pixel.Clone();
            p.setX(x);
            p.setY(y);
            this.grid[x][y] = p;
        }
    }
}
