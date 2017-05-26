
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
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
    private int room;

    
    private Font gameFont;
    private HashMap<String, ArrayList<String>> cutsceneText = new HashMap<String, ArrayList<String>>();
    
    /* SCENE FLAGS */
    private boolean scene_newgame, scene_game, scene_title, scene_roomcomplete;
    private boolean scene_settings, scene_pause, scene_cutscene, scene_victory;
    private boolean stage_exit;
    private boolean stage_music;

    @Override
    public void start(Stage stage) throws Exception {
    	ArrayList<String> introText = new ArrayList<String>();
    	ArrayList<String> cutscene1 = new ArrayList<String>();
    	ArrayList<String> cutscene2 = new ArrayList<String>();
    	ArrayList<String> cutscene3 = new ArrayList<String>();
    	
    	introText.add("\nGreetings young knight-in-training. "
    			+ "\nI am Wobcke the Wise, here to guide "
    			+ "\nyou on the path of Toyota to become "
    			+ "\nthe ultimate Scrum Lord.");
    	introText.add("\nToday I will be needing you to push \n"
    			+ "some crates for me. \n"
    			+ "Simply push the crates onto the \n"
    			+ "squares marked with a red X.");
    	
    	introText.add("\nWhat? \n"
    			+ "Of course it's for training! \n"
    			+ "Being Scrum Lord requires a \n"
    			+ "little conditioning you know. \n\n"
    			+ "Come on now! \n"
    			+ "We have no more time to waste.");
 
    	cutscene1.add("\nExcellent. I see you have \n"
    			+ "completed your morning warm-up. \n"
    			+ "Now it is time for your real \n"
    			+ "training!");
    	
    	cutscene1.add("\nDue to unforeseen circumstances, \n"
    			+ "our campervan rental system \n"
    			+ "has died. What? Of course we \n"
    			+ "have campervans in the magical\n"
    			+ "kingdom of Java.");
    	
    	cutscene1.add("\nDid you think we were living in\n"
    			+ "the Dark Ages? Actually, don't\n"
    			+ "answer that. We must go now!");
    	
    	cutscene2.add("\nAh. I am mildly surprised you have\n"
    			+ "come this far. But it was to \n"
    			+ "be expected. Alas, your real trials \n"
    			+ "start now. ");
    	
    	cutscene2.add("\nOur trucks from A STAR have \n"
    			+ "yet to return from their deliveries. \n"
    			+ "Find them and send them back \n"
    			+ "into the nearest city so we \n"
    			+ "can fix their delivery systems. \n"
    			+ "Time is of the essence. Now go! ");
    	
    	cutscene3.add("Well, that's all I had prepared. \nGood job. ");
    	cutscene3.add("You are now Scrum Master.");
    	
    	cutsceneText.put("intro", introText);
    	cutsceneText.put("cutscene1", cutscene1);
    	cutsceneText.put("cutscene2", cutscene2);
    	cutsceneText.put("cutscene3", cutscene3);
    	
    	// unset all flags
    	unsetNewGame();
    	unsetGame();
    	unsetTitle();
    	unsetRoomComplete();
    	unsetSettings();
    	unsetPause();
    	unsetCutscene();
    	unsetVictory();
    	
    	stage_exit = false;
    	room = 0;
    	
   	
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
            	if (scene_cutscene == true) scene = showCutScene();
            	else if (scene_victory == true) scene = showVictoryScene();
            	else if (scene_roomcomplete == true) scene = showRoomCompleteScene();
            	else if (scene_newgame == true) scene = showNewGameScene();
            	else if (scene_settings == true) scene = showSettingsScene();
            	else if (scene_pause == true) scene = showPauseScene();
            	else if (scene_game == true) scene = showGameScene();

            	else if (scene_title == true) scene = showTitleScreenScene();
            	else scene = showTitleScreenScene();

                stage.setScene(scene);
                stage.setTitle("Wobquest");
                stage.show();
                
                if (stage_exit) {
            		super.stop();
            		stage.close(); 
            		try {
						TimeUnit.SECONDS.sleep(1);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
            	}

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
            	if (event.getCode() == KeyCode.ESCAPE) {
            		unsetGame(); 
            		setPause();
            	} else if (event.getCode() == KeyCode.N){
            		unsetNewGame();
            		setRoomComplete();
            	} else {
            		keypress.setFlag(event.getCode());
            	}
                
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
        GameBoardGen ga;
        if (room <= 5) ga = new GameBoardGen(10, 5, 1,newPlayer);
        else if (room <= 10) ga = new GameBoardGen(10, 8, 2,newPlayer);
        else if (room <= 15) ga = new GameBoardGen(10, 10, 2,newPlayer);
        else if (room <= 20) ga = new GameBoardGen(10, 12, 3,newPlayer);
        else if (room <= 25) ga = new GameBoardGen(15, 10, 5,newPlayer);
        else if (room <= 30) ga = new GameBoardGen(16, 16, 6,newPlayer);
        else ga = new GameBoardGen(22, 16, 20,newPlayer);
	g = ga.getBoard();
		
		// Get tileset
    	if (room <= 10) tileset = new TileSet(1);
    	else if (room <= 20) tileset = new TileSet(2);
    	else tileset = new TileSet(3);
    	
		
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
            	if (event.getCode() == KeyCode.ESCAPE) {
            		unsetGame(); 
            		setPause();
            	} else if (event.getCode() == KeyCode.N){
            		unsetNewGame();
            		setRoomComplete();
            	} else {
            		keypress.setFlag(event.getCode());
            	}
                
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
    	t.setTextAlignment(TextAlignment.CENTER);
    	t.setFont(gameFont);
    	t.setFill(Color.CRIMSON);
    	
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
    	menuOptions.add("Settings");
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
    					case 0: unsetTitle(); setNewGame(); cursorReset(); break;
    					case 1: unsetTitle(); setSettings(); cursorReset(); break;
    					case 2: stage_exit = true; break;
    					}
    				}
    			
    			}
    		}
    	});
    	
    	return scene;
    }
    
    private Scene showSettingsScene(){
    	BorderPane borderpane = new BorderPane();
    	
    	ArrayList<String> menuOption = new ArrayList<String>();
    	menuOption.add("main menu");
    	
    	VBox options = optionsBox(menuOption);
    	borderpane.setCenter(options);
    	
    	Scene scene = new Scene(borderpane, W, H, Color.BLACK);
    	scene.setOnKeyPressed(new EventHandler<KeyEvent>(){
    		@Override
    		public void handle(KeyEvent event){
    			switch(event.getCode()){
    			case UP: cursorUp(); break;
    			case DOWN: cursorDown(); break;
    			case ENTER: {
    					switch(cursor){
    					case 0: {
    							// Toggle music
    							if (stage_music) unsetMusic();
    							else setMusic();
    							break;
    						}
    					case 1: unsetSettings(); setTitle(); cursorReset(); break;

    					}
    				}
    			
    			}
    		}
    	});
    	return scene;
    	
    }
    private Scene showPauseScene(){
    	BorderPane borderpane = new BorderPane();
    	ArrayList<String> menuOption = new ArrayList<String>();
    	menuOption.add("return to game");
    	menuOption.add("main menu");
    	
    	VBox options = optionsBox(menuOption);
    	borderpane.setCenter(options);
    	
    	Scene scene = new Scene(borderpane, W, H, Color.BLACK);
    	scene.setOnKeyPressed(new EventHandler<KeyEvent>(){
    		@Override
    		public void handle(KeyEvent event){
    			switch(event.getCode()){
    			case UP: cursorUp(); break;
    			case DOWN: cursorDown(); break;
    			case ESCAPE: unsetSettings(); setGame(); cursorReset(); break;
    			case ENTER: {
    					switch(cursor){
    					case 0: {
    							// Toggle music
    							if (stage_music) unsetMusic();
    							else setMusic();
    							break;
    						}
    					case 1: unsetPause(); setGame(); cursorReset(); break;
    					case 2: unsetPause(); setTitle(); cursorReset(); break;

    					}
    				}
    			
    			}
    		}
    	});
	
    	return scene;
    }
    
    private Scene showCutScene(){
    	// Determine current string
    	try {
			TimeUnit.MILLISECONDS.sleep(200);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	ArrayList<String> cutsceneList = new ArrayList<String>();
    	if (room < 10) cutsceneList = cutsceneText.get("intro");
    	else if (room < 20) cutsceneList = cutsceneText.get("cutscene1");
    	else if (room < 30) cutsceneList = cutsceneText.get("cutscene2");
    	else if (room < 40) cutsceneList = cutsceneText.get("cutscene3");
    	
    	final int listsize = cutsceneList.size();
    	String currString = cutsceneList.get(cursor);
    	BorderPane cutscenePane = cutsceneTemplate(currString);
    	
    	Scene scene = new Scene(cutscenePane, W, H, Color.BLACK);
    	scene.setOnKeyPressed(new EventHandler<KeyEvent>(){
    		@Override
    		public void handle(KeyEvent event){
    			cursor++;
    			
    			if (cursor == listsize) {
    				if (room == 31) {
        				setVictory();
        			}
    				unsetCutscene(); 
    				cursorReset();
    			}
    		}
    	});
	
    	return scene;
    	
    }
    
    private Scene showVictoryScene(){
    	BorderPane borderpane = new BorderPane();
    	borderpane.setPadding(new Insets(150.0, 10.0, 10.0, 10.0));
    	borderpane.setStyle("-fx-background-color: #000000");
    	
    	Label thanks = new Label("Thanks for playing!");
		thanks.setFont(gameFont);
		thanks.setTextFill(Color.CRIMSON);
		thanks.setTextAlignment(TextAlignment.CENTER);
		thanks.setStyle("-fx-background-color: #000000");
    	
		ImageView victory = getImageView("images/victory.png");
		
		Text credits = new Text("Made by:\nAsher Silvers        Max Kelly-Mills\nNaomi Que        Usman Haidar\n"
				+ "\nSpecial Thanks:\nNicholai Rank");
		credits.setFont(gameFont);
		credits.setFill(Color.POWDERBLUE);
		credits.setTextAlignment(TextAlignment.CENTER);
		VBox center = new VBox();
		center.setAlignment(Pos.CENTER);
		center.setStyle("-fx-background-color: #000000");
		center.getChildren().add(victory);
		center.getChildren().add(credits);
	
		ArrayList<String> options = new ArrayList<String>();
		options.add("Continue Playing");
		options.add("Main Menu");
		
		VBox menu = createMenu(options);
		borderpane.setTop(thanks);
		borderpane.setAlignment(thanks, Pos.CENTER);
		borderpane.setCenter(center);
		borderpane.setBottom(menu);
		
		Scene scene = new Scene(borderpane, W, H, Color.BLACK);
    	scene.setOnKeyPressed(new EventHandler<KeyEvent>(){
    		@Override
    		public void handle(KeyEvent event){
    			switch(event.getCode()){
    			case UP: cursorUp(); break;
    			case DOWN: cursorDown(); break;
    			case ESCAPE: unsetSettings(); setGame(); cursorReset(); break;
    			case ENTER: {
    					switch(cursor){

    					case 0: unsetVictory(); cursorReset(); break;
    					case 1: unsetVictory(); setTitle(); cursorReset(); break;

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
		if (room == 0 || room == 10 || room == 20 || room == 30) setCutscene();
		room++;
		
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
		room = 0;
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
	
	private void setSettings() {
		scene_settings = true;
	}
	
	private void unsetSettings() {
		scene_settings = false;
	}
	
	private void setPause() {
		scene_pause = true;
	}
	
	private void unsetPause() {
		scene_pause = false;
	}
	
	private void setCutscene() {
		cursorReset();
		scene_cutscene = true;
	}
	
	private void unsetCutscene() {
		scene_cutscene = false;
	}
	
	private void setVictory() {
		cursorReset();
		scene_victory = true;
	}
	
	private void unsetVictory() {
		scene_victory = false;
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
		
		BorderPane borderpane = new BorderPane();
		VBox box = new VBox();
		box.setStyle("-fx-background-color: #000000");
		box.setAlignment(Pos.CENTER);
		box.getChildren().add(n);
		borderpane.setCenter(box);
		
		HBox topRow = new HBox();
		
		String roomString = "Room " + room;
		Text roomText = new Text(roomString);
		roomText.setFont(gameFont);
		roomText.setFill(Color.WHITE);
		roomText.setTextAlignment(TextAlignment.RIGHT);
		
		topRow.getChildren().add(roomText);
		topRow.setAlignment(Pos.BOTTOM_RIGHT);
		topRow.setStyle("-fx-background-color: #000000");
		topRow.setPadding(new Insets(10.0,10.0,0.0,0.0));
		borderpane.setTop(topRow);

		return borderpane;
	}
	

	private VBox optionsBox(ArrayList<String> moreOptions){
		ArrayList<String> options = new ArrayList<String>();
		String musicOption = "";
		if (stage_music) musicOption = "music on";
		else musicOption = "music off";
		options.add(musicOption);
		
		for (String option: moreOptions){
			options.add(option);
		}
		VBox optionsBox = createMenu(options);
		return optionsBox;
	}
	
	private VBox createMenu(ArrayList<String> menuOptions){
		int currOption = 0;
		VBox menu = new VBox();

		if (cursor == -1) cursor += menuOptions.size();
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
	
	private BorderPane cutsceneTemplate(String cutsceneDialogue){
		BorderPane borderpane = new BorderPane();
		ImageView wizard = getImageView("images/wobckethewise-big.png");
		
		borderpane.setStyle("-fx-background-color: #000000");
		
		// create dialogue box
		String wizardName = "Wobcke the Wise";
		VBox dialogueBox = new VBox();
		dialogueBox.setAlignment(Pos.CENTER);
		Label name = new Label(wizardName);
		name.setMinWidth(500);
		name.setFont(gameFont);
		name.setTextFill(Color.CRIMSON);
		name.setAlignment(Pos.CENTER_LEFT);
		Text dialogue = new Text(cutsceneDialogue);
		dialogue.setFont(gameFont);
		dialogue.setFill(Color.POWDERBLUE);
		dialogue.setTextAlignment(TextAlignment.LEFT);
		
		
		HBox speakerBox = new HBox();
		speakerBox.setAlignment(Pos.CENTER);
		speakerBox.setSpacing(20.0);
		speakerBox.getChildren().add(wizard);
		speakerBox.getChildren().add(name);
		dialogueBox.setMinHeight(500);
		dialogueBox.setMinWidth(700);
		dialogueBox.getChildren().add(speakerBox);
		dialogueBox.getChildren().add(dialogue);

		
		Text pressNext = new Text("Press any key to continue");
		pressNext.setFont(gameFont);
		pressNext.setFill(Color.WHITE);
		pressNext.setTextAlignment(TextAlignment.CENTER);
		
		borderpane.setCenter(dialogueBox);
		borderpane.setBottom(pressNext);
		borderpane.setAlignment(pressNext, Pos.CENTER);
		borderpane.setPadding(new Insets(10.0, 10.0, 10.0, 10.0));
		return borderpane;
	}
	
	private void cursorReset(){
		cursor = 0;
	}
	private void cursorUp(){
		cursor--;
	}
	
	private void cursorDown(){
		cursor++;
	}
}
