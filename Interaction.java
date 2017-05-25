import java.util.ArrayList;

public class Interaction {
	private GameBoard board;
	
	public Interaction(GameBoard board){
		this.board = board;
	}
	
	/**
	 * Checks if a game interaction is complete
	 * @return true if the game is complete, otherwise false
	 */
	public boolean isGameComplete(){
		for (int i = 0; i < board.getX(); ++i) {
			for (int j = 0; j < board.getY(); j++){
				if ((board.getObjectAt(i, j) != null) && (board.getObjectAt(i, j).getClass() == Goal.class)){
					return false;
				}
				else if ((board.getObjectAt(i, j) != null && board.getObjectAt(i, j).getClass() == Player.class)) {
					if (((GameBoardObject) board.getObjectAt(i, j)).isOnGoal()) return false;
				}
			}
		}
		return true;
	}
	
	/**
	 * Moves, only if possible, a player one space to
	 * 	the left
	 * @param player - The player to be moved
	 * @return true if move has been successful, otherwise false
	 */
	public boolean moveLeft(Player player){
		Coordinates off = new Coordinates(-1,0);
		return moveOffset(player,off);
	}
	
	/**
	 * Moves, only if possible, a player one space to
	 * 	the right
	 * @param player - The player to be moved
	 * @return true if move has been successful, otherwise false
	 */
	public boolean moveRight(Player player){
		Coordinates off = new Coordinates(+1,0);
		return moveOffset(player,off);
	}
	
	/**
	 * Moves, only if possible, a player one space to
	 * 	the up
	 * @param player - The player to be moved
	 * @return true if move has been successful, otherwise false
	 */
	public boolean moveUp(Player player){
		Coordinates off = new Coordinates(0,-1);
		return moveOffset(player,off);
	}
	
	/**
	 * Moves, only if possible, a player one space to
	 * 	the down
	 * @param player - The player to be moved
	 * @return true if move has been successful, otherwise false
	 */
	public boolean moveDown(Player player){
		Coordinates off = new Coordinates(0,+1);
		return moveOffset(player,off);
	}
	
	/**
	 * Attempts to move a player an offset amount.
	 * If the player cannot be move they stay in their current position
	 * @param player - The player to be moved
	 * @param offset - The offset amount from the current position
	 * @return true if move has been successful, otherwise false
	 */
	private boolean moveOffset(Player player, Coordinates offset){
		Coordinates c = board.getLocationOf(player);
		Coordinates desired = new Coordinates(c.getX(), c.getY()).add(offset);
		
		Object nextToMe = board.getObjectAt(desired);
		if (nextToMe == null){
			if (canPlaceObj(desired)){
				board.moveObj(player,desired);
				if (player.isOnGoal() == true) {
					player.setGoal(false);
					board.addObj(new Goal(), c);
				}
				
				return true;
			}
		} else if (nextToMe.getClass() == Goal.class) {
			if (canPlaceObj(desired)){
				board.moveObj(player, desired);
				if (player.isOnGoal() == true) board.addObj(new Goal(), c);
				player.setGoal(true);
				return true;
			}
		} else if (nextToMe.getClass() == Box.class){
			Coordinates nextToDesired = new Coordinates(desired.getX(), desired.getY()).add(offset);
			
			if (canPlaceObj(nextToDesired)){
				if (board.getObjectAt(nextToDesired) == null || board.getObjectAt(nextToDesired).getClass() != Box.class) {
					//moves box first
					moveBox((Box) nextToMe,nextToDesired, desired);
					//then moves player to boxe's original position
					boolean ontoGoal = false;
					if (board.getObjectAt(desired) != null && board.getObjectAt(desired).getClass() == Goal.class) {
						ontoGoal = true;
					}
					board.moveObj(player,desired);
					if (player.isOnGoal() == true) {
						board.addObj(new Goal(), c);
					}
					if (ontoGoal) player.setGoal(true);
					else player.setGoal(false);
					return true;
				}
			}
		}
		
		return false;
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
	 * @param start - The box's original coordinates
	 * @pre canPlaceObj(c)
	 */
	private void moveBox(Box b, Coordinates c, Coordinates start){
		
		if (board.getObjectAt(c) != null) {
			if (board.getObjectAt(c).getClass() == Goal.class){
				//if it's a goal, the entire position is reset
				board.moveObj(b, c);
				if (b.isOnGoal() == true) {
					board.addObj(new Goal(), start);
				}
				b.setGoal(true);

			}
		} else {
			board.moveObj(b, c);
			if (b.isOnGoal() == true) {
				b.setGoal(false);
				board.addObj(new Goal(), start);
			}
		}
	}
	
}