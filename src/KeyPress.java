import javafx.scene.input.KeyCode;

/**
 * Hold down an arrow key to have your hero move around the screen.
 * Hold down the shift key to have the hero run.
 */
public class KeyPress{

    private GameBoard g;	// This should be the gameboard that it is associated with
    private Interaction i;
    private Player player;	// This should be the player that is associated with this keypress

    boolean running, goNorth, goSouth, goEast, goWest, grounded, northPressed, southPressed, eastPressed, westPressed;

 


    public KeyPress(GameBoard gameboard, Player player){
    	this.g = gameboard;
    	this.i = new Interaction(this.g);
    	this.player = player;
    }
    
    /**
     * Sets whichever keys that have been pressed to True
     * @param keypress Key code for the corresponding key pressed
     */
    public void setFlag(KeyCode keypress){
    	switch (keypress) {
        case UP: goNorth = true; break;                 
        case DOWN:  goSouth = true; break;
        case LEFT:  goWest  = true; break;
        case RIGHT: goEast  = true; break;
        case W: goNorth = true; break;
        case S: goSouth = true; break;
        case A: goWest = true; break;
        case D: goEast = true; break;
        case SHIFT: running = true; break;
        case R: {
        	for (int i = 0; i < 15; i++) {
        		for (int j = 0; j < 10; j++) {
        			g.removeObj(i, j);
        		}
        	}
        	g.addOuterWall();
        	g.addObj(player, 2, 2);	// Not sure if should add new player or maintain a Player in this class
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
    
    /**
     * Resets all keys to False (i.e. nothing is being pressed)
     * @param keypress Key code for the corresponding key pressed
     */
    public void resetFlags(KeyCode keypress){
    	switch (keypress) {
        case UP:    goNorth = false; northPressed = false; break;
        case DOWN:  goSouth = false; southPressed = false; break;
        case LEFT:  goWest  = false; westPressed = false; break;
        case RIGHT: goEast  = false; eastPressed = false; break;
        case W:    goNorth = false; northPressed = false; break;
        case S:  goSouth = false; southPressed = false; break;
        case A:  goWest  = false; westPressed = false; break;
        case D: goEast  = false; eastPressed = false; break;
        case SHIFT: running = false; break;
    	}
    }
    
    /**
     * Makes changes to the gameboard according to what keys have been pressed 
     */
    public void handleInput(){

        int dx = 0, dy = 0;

        if (goNorth) {
        	dy += 1;
        	if (northPressed == false) {
        		northPressed = true;
        		i.moveUp(player);                		
        	}
        }
        if (goSouth) {
        	dy -= 1;
          	if (southPressed == false) {
        		southPressed = true;
        		i.moveDown(player);
        		
        	}                	                	
        }
        if (goEast) {
        	dx += 3;
          	if (eastPressed == false) {
        		eastPressed = true;
        		i.moveRight(player);
        	}
        }
        if (goWest) {
        	dx -= 3;
          	if (westPressed == false) {
        		westPressed = true;
        		i.moveLeft(player);
        	}
        }
        if (running) { dx *= 2; dy *= 2; }
        System.out.println(g.toString());
    }
    
}
