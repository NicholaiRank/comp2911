
public class Interaction {
	private Player player;
	private GameBoard board;
	
	public Interaction(GameBoard board){
		this.board = board;
	}
	
	/**
	 * 
	 * @param player
	 * @pre player is in GameBoard Matrix
	 */
	public void moveLeft(Player player){
		Coordinates c = board.getLocationOf(player);
		Coordinates desired = new Coordinates(c.getX()-1, c.getY());
		
		Object nextToMe = board.getObjectAt(desired);
		if (nextToMe == null){
			if (canPlaceObj(desired)) board.moveObj(player,desired);
		} else if (nextToMe.getClass() == Box.class){
			Coordinates nextToDesired = new Coordinates(c.getX()-2, c.getY());
			
			if (canPlaceObj(nextToDesired)){
				moveBox((Box) nextToMe,nextToDesired);
				//moves player
				board.moveObj(player,desired);
			}
		}
	}
	
	private boolean canPlaceObj(int x, int y){
		Object obj = board.getObjectAt(x,y);
		
		if (obj == null) return true;
		if (obj.getClass() == Wall.class) return false;
		
		return true;
	}
	
	private boolean canPlaceObj(Coordinates c){
		return canPlaceObj(c.getX(),c.getY());
	}
	
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
