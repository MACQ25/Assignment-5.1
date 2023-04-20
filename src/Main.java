import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

/**
 * This is the start of the main application
 */
public class Main extends Application {
    /**
     * This is the root that will be used to create our GUI
     */
    Group root;
    /**
     * Scene used to show the user said GUI
     */
    Scene scene;
    /**
     * All possible stages to be used (some go unused, they are meant for future additions to this project)
     */
    Stage genStage;
    /**
     * Thread used for the animation function
     */
    Thread t;
    boolean ITD = false;
    boolean notSet = true;
    /**
     * Player object is initialized, will be used later
     */
    Player player1 = new Player();
    /**
     * Map object is initialized and given a single argument that indicates that it is currently not holding an actual map,
     * will be required for future random generation of a map
     */
    Map muhMap = new Map("Not Yet");

    Button clear;

    public void parseRubble(ActionEvent e){
        player1.setClearRubble(true);
    }

    /**
     * This is actually now what sends the program into different "state-based" stages depending
     * on a global integer, it is like a fork in the road per se which checks for the current state
     * and sets up the UI the way the current state demands it, currently there's only one state,
     * future ones can be added in retroactively.
     *
     * @param stage The main stage
     * @throws Exception
     * @link mapUI()
     */
    @Override
    public void start(Stage stage) throws Exception {
        genStage = stage;
        mapUI();
        genStage.show();
    }

    /**
     * This is our main "map" stage GUI design associated with state 3,
     * this one in particular launches a thread used for animation
     */
    public void mapUI(){
        root = new Group();
        scene = new Scene(root, 900, 650);
        genStage.setScene(scene);
        genStage.show();
        scene.setFill(Color.BLACK);

        Canvas canvas = new Canvas(900, 600);
        clear = new Button("Clear a way");
        root.getChildren().addAll(canvas, clear);

        clear.relocate(220, 605);
        genStage.setTitle("Map"); // set the window title here
        genStage.setScene(scene);
        GraphicsContext gc = canvas.getGraphicsContext2D();

        scene.setOnKeyPressed(new EventHandler<KeyEvent>(){
            @Override
            public void handle(KeyEvent event){
                if(player1.getClearRubble()) {
                    switch (event.getCode()) {
                        case W -> {
                            muhMap.makeWay(player1.moveRubble("up"));
                            player1.setAvailableSpace(muhMap.giveCorridors());
                        }
                        case A -> {
                            muhMap.makeWay(player1.moveRubble("left"));
                            player1.setAvailableSpace(muhMap.giveCorridors());
                        }
                        case S -> {
                            muhMap.makeWay(player1.moveRubble("down"));
                            player1.setAvailableSpace(muhMap.giveCorridors());
                        }
                        case D -> {
                            muhMap.makeWay(player1.moveRubble("right"));
                            player1.setAvailableSpace(muhMap.giveCorridors());
                        }
                    }
                } else switch (event.getCode()){
                    case W ->{
                        player1.movePlayer("up");
                    }
                    case A ->{
                        player1.movePlayer("left");
                    }
                    case S ->{
                        player1.movePlayer("down");
                    }
                    case D ->{
                        player1.movePlayer("right");
                    }
                }

                if (event.getCode() == KeyCode.ESCAPE){
                    ITD = false;
                }
            }
        });
        clear.setOnAction(this::parseRubble);

        if (notSet){
            muhMap = new Map(gc, 250);
            player1.setSpawn(muhMap);
            notSet = false;
        }

        player1.setPlaying(true);
        t = new Thread(() -> animate1(gc, muhMap));
        t.start();
    }


    /**
     * Animation where the actual map of our dungeon is being shown
     * @param gc
     * @param map
     */
    public void animate1(GraphicsContext gc, Map map) {
        ITD = true;
        int numeventsFound = 0;
        while (ITD) {
            map.drawMap(gc, player1.getSpawnLoc());
            player1.drawPlayer(gc);
            if(player1.checkEvent()){
                //player1.setPlaying(false);
                numeventsFound++;
                System.out.println("Player has found event, num " + numeventsFound + " and it is: " + player1.getFacing());
            }
            pause(100);
        }
    }

    /**
     * Use this method instead of Thread.sleep(). It handles the possible
     * exception by catching it, because re-throwing it is not an option in this
     * case.
     *
     * @param duration Pause time in milliseconds.
     */
    public static void pause(int duration) {
        try {
            Thread.sleep(duration);
        } catch (InterruptedException ex) {
        }
    }

    /**
     * Exits the app completely when the window is closed. This is necessary to
     * kill the animation thread.
     */
    @Override
    public void stop() {
        System.exit(0);
    }

    /**
     * Launches the app
     *
     * @param args unused
     */
    public static void main(String[] args) {
        launch(args);
    }
}