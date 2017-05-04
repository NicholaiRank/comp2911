
public class Player implements GameBoardObject{
	private String name;
	
	public Player(String name){
		this.name = name;
	}
	
	public String getName(){
		return name;
	}
	
	public char getByteRep(){
		return name.charAt(0);
	}
}
