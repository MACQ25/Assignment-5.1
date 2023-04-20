import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;

import java.awt.*;
import java.util.ArrayList;
import java.util.Random;
import java.util.random.RandomGenerator;

/**
 * This holds the information the user will require to navigate the GUI and play the game,
 * future additions such as lives, stats, an array that serves as an inventory and more are
 * pending
 */
public class Player {
    private double xPos;
    private double yPos;
    private int gridXpos;
    private int gridYpos;
    private javafx.scene.paint.Color playerColor;
    private ArrayList<ArrayList<Corridor>> availableSpace;
    private Tile spawnLoc;
    private double size;
    private boolean playing;
    private String facing;
    private boolean clearRubble;

    /**
     * Used to instantiate a player at the start of the game, takes no arguments,
     * variables that the player class can hold will be assigned through other functions
     * inside of this class and not inside the constructor.
     */
    public Player(){
        this.size = 30;
        this.playerColor = Color.MAGENTA;
        this.playing = false;
        this.clearRubble = false;
    }

    /**
     * Used after the map is initialized and has a grid map being held inside of it but right before it
     * is actually drawn inside the canvas, this will also set the rest of the player's variables, such as:
     * xPos, yPos, gridXPos, gridYPos & spawnLoc; on top of it will get the available space in which the
     * player can move or be spawned in using another method from this object class.
     * @param dungeon the map object in question
     * @link setAvailableSpace()
     */
    public void setSpawn(Map dungeon){
        setAvailableSpace(dungeon.giveCorridors());
        ArrayList<Corridor> longest = new ArrayList<>(1);
        for (ArrayList<Corridor> column: this.availableSpace) {
            if (column.size() > longest.size()){longest = column;}
        }
        int ranChosTil = RandomGenerator.getDefault().nextInt(0, longest.size());
        this.xPos = longest.get(ranChosTil).getLocX();
        this.yPos = longest.get(ranChosTil).getLocY();
        this.gridXpos = longest.get(ranChosTil).getLocXgrid();
        this.gridYpos = longest.get(ranChosTil).getLocYgrid();
        this.spawnLoc = new exit(longest.get(ranChosTil).getLocX(),longest.get(ranChosTil).getLocY(), longest.get(ranChosTil).getLocXgrid(), longest.get(ranChosTil).getLocYgrid());
    }

    /**
     * Used to set an array of tiles in which the player can move on the canvas
     * @param availableSpace a list of corridors.
     */
    public void setAvailableSpace(ArrayList<ArrayList<Corridor>> availableSpace) {
        this.availableSpace = availableSpace;
    }

    /**
     * Used to move the player through the canvas and map and does so while checking that said
     * move is valid and will end with the player inside of a corridor Tile.
     * @param dir string that indicates in which direction the player will move.
     * @link validMove()
     */
    public void movePlayer(String dir){
        if (playing){
            switch (dir){
                case "left" -> {if (validMove((-30), 0)){this.xPos += -30; this.gridXpos -= 1;}}
                case "right" -> {if (validMove((30), 0)) {this.xPos += 30; this.gridXpos += 1;}}
                case "up" -> {if (validMove(0, (-30))){this.yPos -= 30; this.gridYpos -= 1;}}
                case "down" -> {if (validMove(0, (30))){this.yPos += 30; this.gridYpos +=1;}}
            }
            System.out.println("Current player pos in Grid: " + this.gridXpos + " & " + this.gridYpos);
        }
    }

    /**
     * Checks that the player's next move will end with them in a Corridor
     * @param chngX change in position of the player on the X axis
     * @param chngY change in position of the player on the Y axis
     * @return boolean value reflecting that the player's next move will end with them on a corridor
     */
    public boolean validMove(int chngX, int chngY){
        for (ArrayList<Corridor> x : availableSpace) {
            for (Corridor y : x) {
                if ((y.getLocX() == (this.xPos + chngX)) && (y.getLocY() == (this.yPos + chngY))){
                    return true;
                }
            }
        }
        return false;
    }
    /**
     * Checks that the player's next move will be into a wall and will give the location in the grid inside of that wall
     * @param dir that indicates in which direction the player will move.
     * @return an array with the coordinates in the grid of the wall
     */

    public int[] moveRubble(String dir){
        if (playing && clearRubble){
            switch (dir){
                case "left" -> {if (!validMove((-30), 0)){this.xPos += -30; this.gridXpos -= 1;}}
                case "right" -> {if (!validMove((30), 0)) {this.xPos += 30; this.gridXpos += 1;}}
                case "up" -> {if (!validMove(0, (-30))){this.yPos -= 30; this.gridYpos -= 1;}}
                case "down" -> {if (!validMove(0, (30))){this.yPos += 30; this.gridYpos +=1;}}
            }
            System.out.println("Current player pos in Grid: " + this.gridXpos + " & " + this.gridYpos);
        }
        this.clearRubble = false;
        return new int[]{this.gridXpos, this.gridYpos};
    }

    /**
     * Checks if the current corridor in which the player is in and returns true if there's an event inside of it
     * (This is technically deprecated because a variable was added that makes looping through a list irrelevant,
     * will correct later)
     * @return boolean value representing if there's an event on the current corridor or not
     */
    public boolean checkEvent(){
        for (ArrayList<Corridor> x : availableSpace) {
            for (Corridor y : x) {
                if (((y.getLocX() == this.xPos) && (y.getLocY() == this.yPos)) && y.isHasEvent()){
                    y.setHasEvent(false);
                    facing = y.getMyEvent();
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Draws the player inside of the current Graphics Context
     * @param gc graphics context
     */
    public void drawPlayer(GraphicsContext gc){
        gc.setFill(playerColor);
        gc.fillRect(xPos, yPos, size, size);
    }

    /**
     * Getter that gives the Tile in which the User spawned
     * @return Tile in which this players started on the scene
     */
    public Tile getSpawnLoc(){
        return spawnLoc;
    }

    /**
     * Getter that returns the coordinates of the player in the X axis.
     * @return double X coordinate
     */
    public double getxPos() {
        return xPos;
    }
    /**
     * Getter that returns the coordinates of the player in the Y axis.
     * @return double Y coordinate
     */
    public double getyPos() {
        return yPos;
    }

    /**
     * Getter that returns the value of the event the player has found.
     * @return String with name of the event
     */
    public String getFacing(){
        return facing;
    }

    /**
     * Used with the controls related to the player to enable wall destruction
     * @return boolean value explaining if the player is able to destroy a wall or not.
     */
    public boolean getClearRubble(){
        return clearRubble;
    }

    public void setClearRubble(boolean clear){
        this.clearRubble = clear;
    }

    /**
     * Tells the game that the player is already in the game, used to disable some controls if needed.
     * @param playing boolean value
     */
    public void setPlaying(boolean playing) {
        this.playing = playing;
    }

    /**
     * Returns the current location of the player as a String
     * @return String explaining the player's location
     */
    public String toString(){
        return "My location is: " + this.xPos + " & " + this.yPos;
    }
}
