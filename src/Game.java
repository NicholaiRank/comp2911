
import java.io.File;
import java.util.ArrayList;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Label;
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
    private static final double W = 960, H = 720;

    private int velocity;
    private GameBoard g;
    private KeyPress keypress;
    private TileSet tileset;
    private Interaction interaction;
    
    /* SCENE FLAGS */
    private boolean scene_newgame, scene_game, scene_title, scene_roomcomplete;
    
    @Override
    public void start(Stage stage) throws Exception {
    	// unset all flags
    	unsetNewGame();
    	unsetGame();
    	unsetTitle();
    	unsetRoomComplete();
    	
    	// Get tileset
    	tileset = new TileSet();
   	
    	
    	String songString = "puzzleThink.mp3";
    	Media song = new Media(new File(songString).toURI().toString());
        MediaPlayer player = new MediaPlayer(song);
        player.setAutoPlay(true);
        player.setCycleCount(MediaPlayer.INDEFINITE);
        
        Scene scene = showNewGameScene();
        stage.setScene(scene);
        stage.setTitle("Wobquest");
        stage.show();
        System.out.println(g.toString());

        AnimationTimer timer = new AnimationTimer() {
            @Override
            public void handle(long now) {
            	// Play music
            	player.setAutoPlay(true);
            	Scene scene = showGameScene();

                stage.setScene(scene);
                stage.setTitle("Wobquest");
                stage.show();
                System.out.println(g.toString());
            };
            
            
        };	
        timer.start();
        
        
        
    }
    public static void main(String[] args) { launch(args); }
    
    /**
     * Generates a scene that displays the current state of the gameboard
     * @return Scene displaying current state of GameBoard
     */
    private Scene showGameScene(){
    	// Make change to the gameboard as dictated by input
    	keypress.handleInput();
    	
    	// Build scene accordingly
    	TilePane tilePane = g.buildGraphics(tileset);

    	Group dungeon = new Group(tilePane);
        Scene scene = new Scene(dungeon, W, H, Color.BLACK);
 
        // Handle scene events
        scene.setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent event) {
                keypress.setFlag(event.getCode());
                if (interaction.isGameComplete()){
                	
                	setRoomComplete();
                }
            }
        });

        scene.setOnKeyReleased(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent event) {
            	keypress.resetFlags(event.getCode());
            	unsetGame();
            }
        });
    	return scene;
    }
    
    private Scene showNewGameScene(){
    	// Generate new gameboard, player, keypress, interaction and tileset here
    	Player newPlayer = new Player("PLAYER");
        GameBoardGen ga = new GameBoardGen(10,10,3,newPlayer);
		g = new GameBoard(10,10);
		g.addOuterWall();
		for (Coordinates c: ga.getPath()){
			g.addObj(new Wall(), c);
		}
		g.addObj(newPlayer, 2, 2);
		
		// Attach keypress
        keypress = new KeyPress(g,newPlayer);
        interaction = new Interaction(g);
		
        // Build graphics
        TilePane tilePane = g.buildGraphics(tileset);

        
        // Make the scene
        Group dungeon = new Group(tilePane);
        Scene scene = new Scene(dungeon, W, H, Color.BLACK);
 
        // Handle scene events
        scene.setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent event) {
                keypress.setFlag(event.getCode());
                setGame();
                
                
            }
        });

        scene.setOnKeyReleased(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent event) {
            	keypress.resetFlags(event.getCode());
            	unsetNewGame();
            }
        });
        
    	return scene;
    }
    
    private Scene showRoomCompleteScene() {
    	Label victoryLabel = new Label("Room Completed. Congratulations.");
    	Group completedRoom = new Group(victoryLabel);
    	
    	Scene scene = new Scene(completedRoom, W, H, Color.BLACK);
    	scene.setOnKeyPressed( new EventHandler<KeyEvent>() {
    		@Override
    		public void handle(KeyEvent event) {
    			unsetRoomComplete();
    			setNewGame();
    		}
    	});
    	return scene;
    }
    
    /****************************************
     * SET/UNSET SCENE FLAG METHODS
     ***************************************/
	public void setNewGame() {
		scene_newgame = true;
	}
	public void unsetNewGame() {
		scene_newgame = false;
	}
	public void setGame() {
		scene_game = true;
	}
	public void unsetGame() {
		scene_game = false;
	}
	public void setTitle() {
		scene_title = true;
	}
	public void unsetTitle() {
		scene_title = false;
	}
	public void setRoomComplete() {
		scene_roomcomplete = true;
	}
	public void unsetRoomComplete() {
		scene_roomcomplete = false;
	}
}
