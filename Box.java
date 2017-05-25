
public class Box implements GameBoardObject{
	
	private boolean onGoal = false;
	public Box(){}
	
	public char getByteRep(){
		return 'B';
	}

	@Override
	public boolean isOnGoal() {
		return onGoal;
	}
	public void setGoal(boolean set) {
		this.onGoal = set;
	}
}
