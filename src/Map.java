import javafx.scene.canvas.GraphicsContext;

import java.util.ArrayList;
import java.util.Random;
import java.util.random.RandomGenerator;

/**
 * This is the Map class, used for drawing the map inside of our 3rd stage, it also
 * has a map generating algorithm which is called upon initialization and has multiple
 * ways of communicating the information it holds to other objects which need said data.
 */
public class Map {
    private ArrayList<ArrayList<Tile>> grid;

    private String init;

    /**
     * Our first constructor, a Map constructor to be used only at the start of our code to
     * disable a specific function from working from the start.
     * @param notYet a string that preferably holds the words "Not Yet"
     */
    public Map(String notYet){
        init = notYet;
    }

    /**
     * The main constructor to be used when building our Dungeon, it will execute a loop
     * of the appropriate size to generate a "grid" with which to represent a dungeon
     * through the draw method and with which to inform the player object of certain
     * information pertinent for its function.
     * @param gc graphical context.
     * @param numCor number of corridors to place inside of the dungeon.
     * @see Player
     */
    public Map(GraphicsContext gc, int numCor) {
        init = "Yes indeed";
        int x = (int) gc.getCanvas().getWidth();
        int y = (int) gc.getCanvas().getHeight();

        int likelyHood = 85;
        int corsToAdd = numCor;

        grid = new ArrayList<ArrayList<Tile>>((x / 10));
        double xCoor = 0;
        for (int u = 0; u < (x / 30); u++) {

            ArrayList<Tile> thisRow = new ArrayList<Tile>(y / 10);
            double yCoor = 0;

            for (int w = 0; w < y / 30; w++) {
                if ((u > 0 && w > 0) && !((y/30 - w == 1) || (x/30 - u == 1))) {
                    if (RandomGenerator.getDefault().nextInt(0, 101) > likelyHood) {
                        thisRow.add(new Corridor(xCoor, yCoor, u, w));
                        corsToAdd--;
                    } else {
                        if (corsToAdd > 0) {
                            if ((((grid.get(u - 1).get(w).getIdentity().equals("Corridor")) || (thisRow.get(w - 1).getIdentity().equals("Corridor")))) && (RandomGenerator.getDefault().nextInt(0, 101) > (likelyHood - 45))) {
                                /*
                                 * Logic to make corridors happen at random and interconnected goes here!!! Let the pain begin!
                                 * */
                                thisRow.add(new Corridor(xCoor, yCoor, u, w));
                                corsToAdd--;
                            } else {
                                thisRow.add(new Wall(xCoor, yCoor, u, w));
                            }
                        } else {
                            thisRow.add(new Wall(xCoor, yCoor, u, w));
                        }
                    }
                }
                else thisRow.add(new Wall(xCoor, yCoor, u, w));
                yCoor += 30;
            }
            xCoor += 30;
            grid.add(thisRow);
        }
    }

    public ArrayList<ArrayList<Tile>> giveGrid() {
        return grid;
    }

    /**
     * Used on to give the corridor objects (together with the information that they are currently holding)
     * to any such object, function or operation which needs it.
     * @return
     * @see Player
     */
    public ArrayList<ArrayList<Corridor>> giveCorridors() {
        ArrayList<ArrayList<Corridor>> corridorArList = new ArrayList<>();
        for (ArrayList<Tile> col : grid){
            ArrayList<Corridor> section = new ArrayList<>();
            for(Tile row : col){
                if (row.getIdentity().equals("Corridor")){
                    section.add((Corridor) row);
                }
            }
            if (section.size() > 0){ corridorArList.add(section);}
        }
        return corridorArList;
    }

    /**
     * Getter used for said specific disabling of a button mentioned on the 1st constructor
     * @return
     * @link Map(String notYet)
     */
    public String getInit(){
        return init;
    }

    /**
     * Used in conjuction with the player to generate a new corridor in the previous position of a wall.
     * @param locArray
     * @see Player
     */
    public void makeWay(int[] locArray){
        Tile TTC = grid.get(locArray[0]).get(locArray[1]);
        grid.get(locArray[0]).remove(locArray[1]);
        grid.get(locArray[0]).add(locArray[1], new Corridor(TTC.getLocX(), TTC.getLocY(), TTC.getLocXgrid(), TTC.getLocYgrid()));

    }

    /**
     * Used in conjunction with the player object to set a tile where the player can exit the map
     * @param gc graphics context used on this stage
     * @param spawnTile the tile in which the player spawned
     * @see Player
     */
    public void setEscape(GraphicsContext gc, Tile spawnTile){
        gc.setFill(spawnTile.fillCol);
        gc.fillRect(spawnTile.getLocX(), spawnTile.getLocY(), 30, 30);
    }

    /**
     * Used for debugging purposes and map planning, prints out the grid of the map as an
     * array list and details the coordinates of each tile.
     */
    public void showMap(){
        int x = 0;
        for (ArrayList<Tile> joe : grid){
           System.out.println(joe + "\n " + "array of index " + x);
           x++;
        }
        System.out.println(grid.size());
    }

    /**
     * Used to draw the map on the canvas and set the tile with which the player can exit said map.
     * @param gc
     * @param spawnTile tile passed on from the player object and given to the setEscape function
     * @see Player
     * @link setEscape()
     */
    public void drawMap(GraphicsContext gc, Tile spawnTile){
        for (ArrayList<Tile> joe : grid){
            for(Tile mama : joe){
                mama.draw(gc);
            }
        }
        setEscape(gc, spawnTile);
    }
}
