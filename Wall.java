
public class Wall implements GameBoardObject{
	
	public Wall(){}
	
	public char getByteRep(){
		return '#';
	}

	@Override
	public boolean isOnGoal() {
		return false;
	}

	@Override
	public void setGoal(boolean set) {
	}
}
