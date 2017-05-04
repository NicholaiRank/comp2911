
public class Coordinates {
	private int x;
	private int y;
	
	public Coordinates(int x, int y){
		this.x = x;
		this.y = y;
	}

	public int getX() {
		return x;
	}

	public void setX(int x) {
		this.x = x;
	}

	public int getY() {
		return y;
	}

	public void setY(int y) {
		this.y = y;
	}
	
	public Coordinates add(Coordinates c){
		return new Coordinates(c.getX() + x, c.getY() + y);
	}
	
	@Override
	public String toString(){
		return "[" + x + "," + y +"]";
	}
	
}
