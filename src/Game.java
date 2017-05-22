
import java.io.File;
import java.util.ArrayList;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.TilePane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;

/*
 * This class is used to display the game for the user. 
 * It creates the gameboard and displays the needed information to the user.
 */

public class Game extends Application {
    private static final double W = 1000, H = 1000;

    private int velocity;
    private GameBoard g;

    @Override
    public void start(Stage stage) throws Exception {
    	
    	// Get tileset
    	TileSet tileset = new TileSet();
   	
    	
    	String songString = "puzzleThink.mp3";
    	Media song = new Media(new File(songString).toURI().toString());
        MediaPlayer player = new MediaPlayer(song);
        player.setAutoPlay(true);
        player.setCycleCount(MediaPlayer.INDEFINITE);
        
        // BUILD GAMEBOARD HERE
        g = new GameBoard(15, 10);
        g.addObj(new Box(), 7, 5);
        g.addObj(new Box(), 7, 3);
        g.addObj(new Box(), 5, 5);
        g.addObj(new Goal(), 13, 8);
        g.addObj(new Goal(), 13, 1);
        g.addObj(new Goal(), 1, 8);
        
        // Build walls
        g.addOuterWall();        

        // Insert player
        Player newPlayer = new Player("PLAYER");
        g.addObj(newPlayer, 2, 2);

        // Attach keypress
        KeyPress keypress = new KeyPress(g,newPlayer);
        
		
        // Build graphics
        TilePane tilePane = g.buildGraphics(tileset);

        
        // Make the scene
        Group dungeon = new Group(tilePane);
        Scene scene = new Scene(dungeon, W, H, Color.WHITE);
 
        // Handle scene events
        scene.setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent event) {
                keypress.setFlag(event.getCode());
            }
        });

        scene.setOnKeyReleased(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent event) {
            	keypress.resetFlags(event.getCode());
            }
        });


        stage.setScene(scene);
        stage.setTitle("Wobquest");
        stage.show();
        System.out.println(g.toString());

        AnimationTimer timer = new AnimationTimer() {
            @Override
            public void handle(long now) {
            	// Play music
            	player.setAutoPlay(true);
            	
            	// Make change to the gameboard as dictated by input
            	keypress.handleInput();
            	
            	// Build scene accordingly
            	TilePane tilePane = g.buildGraphics(tileset);
            	Group dungeon = new Group(tilePane);
                Scene scene = new Scene(dungeon, W, H, Color.WHITE);
         
                // Handle scene events
                scene.setOnKeyPressed(new EventHandler<KeyEvent>() {
                    @Override
                    public void handle(KeyEvent event) {
                        keypress.setFlag(event.getCode());
                    }
                });

                scene.setOnKeyReleased(new EventHandler<KeyEvent>() {
                    @Override
                    public void handle(KeyEvent event) {
                    	keypress.resetFlags(event.getCode());
                    }
                });


                stage.setScene(scene);
                stage.setTitle("Wobquest");
                stage.show();
                System.out.println(g.toString());
            };
            
            
        };	
        timer.start();
    }
    public static void main(String[] args) { launch(args); }
}
