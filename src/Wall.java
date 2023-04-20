import javafx.scene.paint.Color;

public class Wall extends Tile{
    boolean breakable;
    public Wall(double x, double y, int w, int z){
        super(x, y, w, z);
        super.fillCol = Color.GRAY;
        super.identity = "Wall";
        this.breakable = x != 0 && y != 0;
    }
}
