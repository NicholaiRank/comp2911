
public interface GameBoardObject {;
	/**
	 * A byte representation for printing the object on
	 *  the GameBoard
	 */
	public char getByteRep();
	public boolean isOnGoal();
	public void setGoal(boolean set);
}
