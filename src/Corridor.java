import javafx.scene.paint.Color;

import java.util.random.RandomGenerator;

public class Corridor extends Tile {
    private boolean hasEvent;
    private mapEvents myEvent;
    public Corridor(double x, double y, int w, int z){
        super(x, y, w ,z);
        super.fillCol = Color.WHITE;
        super.identity = "Corridor";

        this.hasEvent = Math.random() > 0.9;

        if (hasEvent){
            myEvent = new mapEvents();
            if (myEvent.getEventType().equals("Fight")){super.fillCol = Color.RED;}
            if (myEvent.getEventType().equals("Choice")){super.fillCol = Color.CYAN;}
            if (myEvent.getEventType().equals("Item")){super.fillCol = Color.GOLD;}
        }
    }

    public boolean isHasEvent() {
        return hasEvent;
    }

    public void setHasEvent(boolean hasEvent) {
        this.hasEvent = hasEvent;
    }

    public String getMyEvent() {
        return myEvent.getEventType();
    }

    public void draw(){

    }
}