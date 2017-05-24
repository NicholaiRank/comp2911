import javafx.scene.input.KeyCode;

/**
 * Hold down an arrow key to have your hero move around the screen.
 * Hold down the shift key to have the hero run.
 */
public class KeyPress{

    private GameBoard g;	// This should be the gameboard that it is associated with
    private Interaction i;
    private Player player;	// This should be the player that is associated with this keypress
	private GameBoard original;

    boolean success, goNorth, goSouth, goEast, goWest, grounded, northPressed, southPressed, eastPressed, westPressed;


    public KeyPress(GameBoard gameboard, Player player){
    	this.g = gameboard;
    	this.i = new Interaction(this.g);
    	this.player = player;
		this.original = this.g.copyBoard();
    }
    
    
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
        case R: {
			this.g = this.original.copyBoard();
			break;	
    		}
    	}
    }
    
    public void resetFlags(KeyCode keypress){
        success = false;
    	switch (keypress) {
        case UP:    goNorth = false; northPressed = false; break;
        case DOWN:  goSouth = false; southPressed = false; break;
        case LEFT:  goWest  = false; westPressed = false; break;
        case RIGHT: goEast  = false; eastPressed = false; break;
        case W:    goNorth = false; northPressed = false; break;
        case S:  goSouth = false; southPressed = false; break;
        case A:  goWest  = false; westPressed = false; break;
        case D: goEast  = false; eastPressed = false; break;

        //case SHIFT: running = false; break;
    	}
    }
    
    public void handleInput(){

        if (goNorth) {
        	if (northPressed == false) {
        		northPressed = true;
        		i.moveUp(player);                		
        	}
        }
        if (goSouth) {
          	if (southPressed == false) {
        		southPressed = true;
        		i.moveDown(player);
        		
        	}                	                	
        }
        if (goEast) {
          	if (eastPressed == false) {
        		eastPressed = true;
        		i.moveRight(player);
        	}
        }
        if (goWest) {
          	if (westPressed == false) {
        		westPressed = true;
        		i.moveLeft(player);
        	}
        } 
    }
    
}
