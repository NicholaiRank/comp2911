
import java.io.File;
import java.util.ArrayList;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ToggleButton;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.TilePane;
import javafx.scene.layout.VBox;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;

/*
 * This class is used to display the game for the user. 
 * It creates the gameboard and displays the needed information to the user.
 */

public class Game extends Application {
    private static final double W = 1280, H = 840;

    private int velocity;
    private GameBoard g;
    private KeyPress keypress;
    private TileSet tileset;
    private Interaction interaction;
    private Scene scene;
    
    
    private Font gameFont;
    
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
   	
    	// Initialise resources
    	String songString = "resources/music/puzzleThink.mp3";
    	Media song = new Media(getFileName(songString));
        MediaPlayer player = new MediaPlayer(song);
        player.setAutoPlay(true);
        player.setCycleCount(MediaPlayer.INDEFINITE);
        
        String fontString = "resources/fonts/PressStart2P.ttf";
        gameFont = Font.loadFont(getFileName(fontString), 30);
        		
        scene = showTitleScreenScene();
        stage.setScene(scene);
        stage.setTitle("Wobquest");
        stage.show();


        AnimationTimer timer = new AnimationTimer() {
            @Override
            public void handle(long now) {
            	// Play music
            	player.setAutoPlay(true);
            	if (scene_game == true) scene = showGameScene();
            	else if (scene_roomcomplete == true) scene = showRoomCompleteScene();
            	else if (scene_newgame == true) scene = showNewGameScene();
            	else scene = showTitleScreenScene();

                stage.setScene(scene);
                stage.setTitle("Wobquest");
                stage.show();

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
        if (interaction.isGameComplete()){
        	unsetGame();
        	setRoomComplete();
        }
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
    	return scene;
    }
    
    private Scene showNewGameScene(){
    	// Generate new gameboard, player, keypress, interaction and tileset here
    	Player newPlayer = new Player("PLAYER");
        GameBoardGen ga = new GameBoardGen(20,15,5,newPlayer);
		g = ga.getBoard();
		
		
		// Attach keypress
        keypress = new KeyPress(g,newPlayer);
        interaction = new Interaction(g);
		
        // Build graphics
        TilePane tilePane = g.buildGraphics(tileset);

        
        // Make the scene
        Group dungeon = new Group(tilePane);
        Scene scene = new Scene(dungeon, W, H, Color.BLACK);   
        unsetNewGame();
        setGame();
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
        
    	return scene;
    }
    
    private Scene showRoomCompleteScene() {
    	// Setup layout
    	BorderPane borderpane = new BorderPane();

    	VBox vbox = new VBox();
    	vbox.setAlignment(Pos.CENTER);

    	borderpane.setCenter(vbox);
    	
    	Text t = new Text();
    	t.setText("Room Completed.\nCongratulations.");
    	t.setFont(gameFont);
    	t.setFill(Color.RED);
    	
    	Text pressNext = new Text();
    	pressNext.setText("\n\n\n\n\n\nPress any key to continue");
    	pressNext.setFont(gameFont);
    	pressNext.setFill(Color.WHITE);
    	
    	vbox.getChildren().add(t);
    	vbox.getChildren().add(pressNext);
    	
    	Scene scene = new Scene(borderpane, W, H, Color.BLACK);
    	scene.setOnKeyPressed( new EventHandler<KeyEvent>() {
    		@Override
    		public void handle(KeyEvent event) {
    			unsetRoomComplete();
    			setNewGame();
    		}
    	});
    	return scene;
    }
    
    private Scene showTitleScreenScene() {
    	// Setup layout
    	BorderPane borderpane = new BorderPane();

    	VBox vbox = new VBox();
    	vbox.setAlignment(Pos.CENTER);

    	borderpane.setCenter(vbox);
    	
    	String titleString = "images/title-no-bg.png";
    	ImageView title = getImageView(titleString);
    	
    	Text pressNext = new Text();
    	pressNext.setText("\n\n\n\n\n\nPress space bar to start");
    	pressNext.setFont(gameFont);
    	pressNext.setFill(Color.WHITE);
    	
    	vbox.getChildren().add(title);
    	vbox.getChildren().add(pressNext);
    	
    	Scene scene = new Scene(borderpane, W, H, Color.BLACK);
    	scene.setOnKeyPressed(new EventHandler<KeyEvent>(){
    		@Override
    		public void handle(KeyEvent event){
    			if (event.getCode() == KeyCode.SPACE) {
    				setNewGame();
    			}
    		}
    	});
    	
    	return scene;
    }
    
    /****************************************
     * SET/UNSET SCENE FLAG METHODS
     ***************************************/
	private void setNewGame() {
		scene_newgame = true;
	}
	private void unsetNewGame() {
		scene_newgame = false;
	}
	private void setGame() {
		scene_game = true;
	}
	private void unsetGame() {
		scene_game = false;
	}
	private void setTitle() {
		scene_title = true;
	}
	private void unsetTitle() {
		scene_title = false;
	}
	private void setRoomComplete() {
		scene_roomcomplete = true;
	}
	private void unsetRoomComplete() {
		scene_roomcomplete = false;
	}
	
	private String getFileName(String rpath) {
		return new File(rpath).toURI().toString();
	}
	
	private ImageView getImageView(String rpath) {
		javafx.scene.image.Image image = new javafx.scene.image.Image(getFileName(rpath));
		ImageView imageView = new ImageView(image);
		return imageView;
		
	}
	
private BorderPane decorateGameBorder(Node n){
		String musicOnString = "images/music-button-on.png";
		String musicOffString = "images/music-button-off.png";
		ImageView musicOn = getImageView(musicOnString);
		ImageView musicOff = getImageView(musicOffString);
		
		BorderPane borderpane = new BorderPane();
		VBox box = new VBox();
		box.getChildren().add(n);
		borderpane.setCenter(box);
		
		
		// Music button
		HBox topButtons= new HBox();
		topButtons.setStyle("-fx-background-color: #000000;");
		ToggleButton musicButton = new ToggleButton("", musicOn);
		topButtons.setAlignment(Pos.CENTER_RIGHT);
		topButtons.getChildren().add(musicButton);
		
		borderpane.setTop(topButtons);
		
		return borderpane;
	}

}
