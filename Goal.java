
public class Goal implements GameBoardObject{

	public Goal(){}
	
	public char getByteRep(){
		return 'X';
	}

	@Override
	public boolean isOnGoal() {
		return false;
	}

	@Override
	public void setGoal(boolean set) {		
	}
}
