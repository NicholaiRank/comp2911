
public class Player implements GameBoardObject{
	private String name;
	private boolean onGoal = false;
	
	public Player(String name){
		this.name = name;
	}
	public String getName(){
		return name;
	}
	
	public char getByteRep(){
		return name.charAt(0);
	}
	@Override
	public boolean isOnGoal() {
		return onGoal;
	}
	@Override
	public void setGoal(boolean set) {
		onGoal = set;
	}
}
