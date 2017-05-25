
import java.io.File;
import java.util.ArrayList;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ToggleButton;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
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
import javafx.scene.text.TextAlignment;
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
    private int cursor;

    
    private Font gameFont;
    
    /* SCENE FLAGS */
    private boolean scene_newgame, scene_game, scene_title, scene_roomcomplete;
    private boolean stage_music;
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
//        player.setAutoPlay(true);
        player.setCycleCount(MediaPlayer.INDEFINITE);
        stage_music = true;
        
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
            	if (stage_music == true) player.play();
            	else player.stop();
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

    	BorderPane dungeon = decorateGameBorder(tilePane);
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
        GameBoardGen ga = new GameBoardGen(10,15,5,newPlayer);
		g = ga.getBoard();
		
		
		// Attach keypress
        keypress = new KeyPress(g,newPlayer, tileset);
        interaction = new Interaction(g);
		
        // Build graphics
        TilePane tilePane = g.buildGraphics(tileset);

        
        // Make the scene
        BorderPane dungeon = decorateGameBorder(tilePane);
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
    	vbox.setStyle("-fx-background-color: #000000");
    	
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
    	vbox.setStyle("-fx-background-color: #000000");
    	borderpane.setCenter(vbox);
    	
    	String titleString = "images/title-no-bg.png";
    	ImageView title = getImageView(titleString);
    	
    	ArrayList<String> menuOptions = new ArrayList<String>();
    	menuOptions.add("Play");
    	menuOptions.add("Options");
    	menuOptions.add("Quit Game");
    	VBox menu = createMenu(menuOptions);
    	
    	vbox.getChildren().add(title);
    	vbox.getChildren().add(menu);
    	
    	Scene scene = new Scene(borderpane, W, H, Color.BLACK);
    	scene.setOnKeyPressed(new EventHandler<KeyEvent>(){
    		@Override
    		public void handle(KeyEvent event){
    			switch(event.getCode()){
    			case UP: cursorUp(); break;
    			case DOWN: cursorDown(); break;
    			case ENTER: {
    					switch(cursor){
    					case 0: setNewGame(); cursorReset(); break;
    					case 1: cursorReset(); break;
    					case 2: cursorReset(); break;
    					}
    				}
    			
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
	
	private void setMusic() {
		stage_music = true;
	}
	private void unsetMusic() {
		stage_music = false;
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
		box.setStyle("-fx-background-color: #000000");
		box.setAlignment(Pos.CENTER);
		box.getChildren().add(n);
		borderpane.setCenter(box);
		
		
		// Music button
		HBox topButtons= new HBox();
		topButtons.setStyle("-fx-background-color: #000000;");
		Button musicButton = new Button();

		if (stage_music) musicButton.setGraphic(musicOn);
		else musicButton.setGraphic(musicOff);
		
		musicButton.setStyle("-fx-background-color: #000000");
		
		
		musicButton.setOnMouseClicked(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent e){
				System.out.println("I'VE BEEN CLICKED");
				if (stage_music)unsetMusic();
				else setMusic();
			 }
			
		});
		
		
		
		topButtons.setAlignment(Pos.CENTER_RIGHT);
		topButtons.getChildren().add(musicButton);
		
		
		
		borderpane.setTop(topButtons);
		
		return borderpane;
	}
	
	private VBox optionsBox(){
		ArrayList<String> options = new ArrayList<String>();
		String musicOption = "";
		if (stage_music) musicOption = "music on";
		else musicOption = "music off";
		options.add(musicOption);
		options.add("exit");
		VBox optionsBox = createMenu(options);
		return optionsBox;
	}

	private VBox createMenu(ArrayList<String> menuOptions){
		int currOption = 0;
		VBox menu = new VBox();
		cursor = cursor % menuOptions.size();
		menu.setStyle("-fx-background-color: #000000");
		menu.setAlignment(Pos.CENTER);
		for (String menuOption: menuOptions){
			// Make a label for each option
			Label label = new Label(menuOption);
			label.setFont(gameFont);
			
			label.setTextAlignment(TextAlignment.CENTER);
			
			if (currOption == cursor){
				label.setTextFill(Color.DARKGRAY);
				label.setStyle("-fx-background-color: #FFFFFF");
			} else {
				label.setTextFill(Color.WHITE);
				label.setStyle("-fx-background-color: #000000");
			}
			menu.getChildren().add(label);
			currOption ++;
		}
		return menu;
	}
	
	public void cursorReset(){
		cursor = 0;
	}
	public void cursorUp(){
		cursor--;
	}
	
	public void cursorDown(){
		cursor++;
	}
}
