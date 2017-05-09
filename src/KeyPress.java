import java.io.File;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.scene.*;
import javafx.scene.image.*;
import javafx.scene.input.KeyEvent;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

/**
 * Hold down an arrow key to have your hero move around the screen.
 * Hold down the shift key to have the hero run.
 */
public class KeyPress extends Application {

    private static final double W = 50, H = 50;

    private static final String HERO_IMAGE_LOC =
            "http://www.polyvore.com/cgi/img-thing?.out=jpg&size=l&tid=22402544";

    private Image heroImage;
    private Node hero;
    private int velocity;
    private GameBoard g = new GameBoard(15, 10);
    private Interaction i = new Interaction(g);


    boolean running, goNorth, goSouth, goEast, goWest, grounded, northPressed, southPressed, eastPressed, westPressed;

    @Override
    public void start(Stage stage) throws Exception {
    	String songString = "puzzleThink.mp3";
    	Media song = new Media(new File(songString).toURI().toString());
        MediaPlayer player = new MediaPlayer(song);
        player.setAutoPlay(true);
        player.setCycleCount(MediaPlayer.INDEFINITE);
        g.addObj(new Box(), 7, 5);
        g.addObj(new Box(), 7, 3);
        g.addObj(new Box(), 5, 5);
        g.addObj(new Goal(), 13, 8);
        g.addObj(new Goal(), 13, 1);
        g.addObj(new Goal(), 1, 8);

        heroImage = new Image(HERO_IMAGE_LOC);
        hero = new ImageView(heroImage);
        g.addOuterWall();
        Group dungeon = new Group(hero);
        Player newPlayer = new Player("PLAYER");
        g.addObj(newPlayer, 2, 2);

        Scene scene = new Scene(dungeon, W, H, Color.WHITE);

        scene.setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent event) {
                switch (event.getCode()) {
                    case UP: goNorth = true; break;                 
                    case DOWN:  goSouth = true; break;
                    case LEFT:  goWest  = true; break;
                    case RIGHT: goEast  = true; break;
                    case SHIFT: running = true; break;
                    case R: {
			for (int i = 0; i < 15; i++) {
                    		for (int j = 0; j < 10; j++) {
                    			g.removeObj(i, j);
                    		}
                    	}
                    	g.addOuterWall();
                    	g.addObj(newPlayer, 2, 2);
                        g.addObj(new Box(), 7, 5);
                        g.addObj(new Box(), 7, 3);
                        g.addObj(new Box(), 5, 5);
                        g.addObj(new Goal(), 13, 8);
                        g.addObj(new Goal(), 13, 1);
                        g.addObj(new Goal(), 1, 8);
                        System.out.println(g.toString());
                    }
                }	
            }
        });

        scene.setOnKeyReleased(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent event) {
                switch (event.getCode()) {
                    case UP:    goNorth = false; northPressed = false; break;
                    case DOWN:  goSouth = false; southPressed = false; break;
                    case LEFT:  goWest  = false; westPressed = false; break;
                    case RIGHT: goEast  = false; eastPressed = false; break;
                    case SHIFT: running = false; break;
                }
            }
        });

        stage.setScene(scene);
        stage.show();
        System.out.println(g.toString());

        AnimationTimer timer = new AnimationTimer() {
            @Override
            public void handle(long now) {
            	player.setAutoPlay(true);
                int dx = 0, dy = 0;

                if (goNorth) {
                	dy += 1;
                	if (northPressed == false) {
                		northPressed = true;
                		i.moveUp(newPlayer);                		
                	}
                }
                if (goSouth) {
                	dy -= 1;
                  	if (southPressed == false) {
                		southPressed = true;
                		i.moveDown(newPlayer);
                		
                	}                	                	
                }
                if (goEast) {
                	dx += 3;
                  	if (eastPressed == false) {
                		eastPressed = true;
                		i.moveRight(newPlayer);
                	}
                }
                if (goWest) {
                	dx -= 3;
                  	if (westPressed == false) {
                		westPressed = true;
                		i.moveLeft(newPlayer);
                	}
                }
                if (running) { dx *= 2; dy *= 2; }
                if (dx != 0 || dy != 0) {
                	System.out.println(g.toString());
                }
               // moveHeroBy(dx, dy);
            }
        };
        timer.start();
    }
/*
    private void moveHeroBy(int dx, int dy) {
 //       if (dx == 0 && dy == 0) return;

        final double cx = hero.getBoundsInLocal().getWidth()  / 2;
        final double cy = hero.getBoundsInLocal().getHeight() / 2;

        double x = cx + hero.getLayoutX() + dx;
        double y = cy + hero.getLayoutY() - velocity;
        velocity--;

        moveHeroTo(x, y);
    }

    private void moveHeroTo(double x, double y) {
        final double cx = hero.getBoundsInLocal().getWidth()  / 2;
        final double cy = hero.getBoundsInLocal().getHeight() / 2;
        if (y == H && velocity != 0) velocity = 0;
        if (x - cx >= 0 &&
            x + cx <= W &&
            y - cy >= 0 &&
            y + cy <= H) {
            hero.relocate(x - cx, y - cy);
        } else if (y - cy < 0 && (x - cx >= 0 && x + cx <= W)) {
        	hero.relocate(x - cx, 0 );
        	velocity = 0;
    	} else if (y + cy > H && (x - cx >= 0 && x + cx <= W)) {
    		hero.relocate(x - cx, H - 2*cy);
    		velocity = 0;
    		grounded = true;
		} else if (x - cx < 0 && (y - cy >= 0 && y + cy <= H)) {
        	hero.relocate(0, y - cy);
    	} else if (x + cx > W && (y - cy >= 0 && y + cy <= H)) {
    		hero.relocate(W - 2*cx, y - cy);
    	} else if (y + cy > H && x + cx > W) {
    		hero.relocate(W - 2*cx, H - 2*cy);
    		grounded = true;
    	} else if (y + cy > H && x - cx < 0) {
    		hero.relocate(0, H - 2*cy);
    		grounded = true;
    	}
    }*/

    public static void main(String[] args) { launch(args); }
}
