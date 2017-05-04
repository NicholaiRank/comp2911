
public class Interaction {
	private Player player;
	private GameBoard board;
	
	public Interaction(GameBoard board){
		this.board = board;
	}
	
	/**
	 * Moves, only if possible, a player one space to
	 * 	the left
	 * @param player - The player to be moved
	 */
	public void moveLeft(Player player){
		Coordinates off = new Coordinates(-1,0);
		moveOffset(player,off);
	}
	
	/**
	 * Moves, only if possible, a player one space to
	 * 	the right
	 * @param player - The player to be moved
	 */
	public void moveRight(Player player){
		Coordinates off = new Coordinates(+1,0);
		moveOffset(player,off);
	}
	
	/**
	 * Moves, only if possible, a player one space to
	 * 	the up
	 * @param player - The player to be moved
	 */
	public void moveUp(Player player){
		Coordinates off = new Coordinates(0,-1);
		moveOffset(player,off);
	}
	
	/**
	 * Moves, only if possible, a player one space to
	 * 	the down
	 * @param player - The player to be moved
	 */
	public void moveDown(Player player){
		Coordinates off = new Coordinates(0,+1);
		moveOffset(player,off);
	}
	
	/**
	 * Attempts to move a player an offset amount.
	 * If the player cannot be move they stay in their current position
	 * @param player - The player to be moved
	 * @param offset - The offset amount from the current position
	 */
	private void moveOffset(Player player, Coordinates offset){
		Coordinates c = board.getLocationOf(player);
		Coordinates desired = new Coordinates(c.getX(), c.getY()).add(offset);
		
		Object nextToMe = board.getObjectAt(desired);
		if (nextToMe == null){
			if (canPlaceObj(desired)) board.moveObj(player,desired);
		} else if (nextToMe.getClass() == Box.class){
			Coordinates nextToDesired = new Coordinates(desired.getX(), desired.getY()).add(offset);
			
			if (canPlaceObj(nextToDesired)){
				//moves box first
				moveBox((Box) nextToMe,nextToDesired);
				//then moves player to boxe's original position
				board.moveObj(player,desired);
			}
		}
	}
	
	
	/**
	 * Checks if an object can be placed in the desired position
	 * @param x - The desired x coordinate
	 * @param y - The desired y coordinate
	 * @return true if can place, otherwise false
	 */
	private boolean canPlaceObj(int x, int y){
		Object obj = board.getObjectAt(x,y);
		
		if (obj == null) return true;
		if (obj.getClass() == Wall.class) return false;
		
		return true;
	}
	
	/**
	 * Checks if an object can be placed in the desired position
	 * @param c - The desired coordinates
	 * @return true if can place, otherwise false
	 */
	private boolean canPlaceObj(Coordinates c){
		return canPlaceObj(c.getX(),c.getY());
	}
	
	/**
	 * Moves a box (it's in the name)
	 * @param b - The box to be moved (must be on board)
	 * @param c - The new coordinates
	 */
	private void moveBox(Box b, Coordinates c){		
		if (board.getObjectAt(c).getClass() == Goal.class){
			//if it's a goal, the entire position is reset
			board.moveObj(b, c);
			board.removeObj(c);
		} else {
			board.moveObj(b, c);
		}
	}
	
}
