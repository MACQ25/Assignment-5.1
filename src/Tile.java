import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

import java.awt.*;

public class Tile {
    private double locX;
    private double locY;
    private int locXgrid;
    private int locYgrid;
    Color fillCol;
    String identity;
    public Tile(double X, double Y, int W, int Z){
        locX = X;
        locY = Y;
        locXgrid = W;
        locYgrid = Z;
    }

    public double getLocX() {
        return locX;
    }

    public double getLocY() {
        return locY;
    }

    public int getLocXgrid() {return locXgrid;}

    public int getLocYgrid() {return locYgrid;}

    public void draw(GraphicsContext gc){
        gc.setFill(this.fillCol);
        gc.fillRect(this.locX, this.locY, 30, 30);
    }

    public String getIdentity() {
        return identity;
    }

    @Override
    public String toString() {
        return "[" + locX + ", " + locY + "]";
    }
}

