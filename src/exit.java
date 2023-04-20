import javafx.scene.paint.Color;

public class exit extends Tile{
    public exit(double x, double y, int w, int z){
        super(x, y, w, z);
        super.identity = "exit";
        super.fillCol = Color.GREEN;
    }
}
