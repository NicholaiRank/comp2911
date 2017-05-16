
public class Coordinates {
	private int x;
	private int y;
	private int cost; 
	//used for heuristic distance
	
	public Coordinates(int x, int y){
		this.x = x;
		this.y = y;
	}
	
	public Coordinates(Coordinates c){
		this.x = c.getX();
		this.y = c.getY();
		this.cost = 0;
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
	
	public int setCost(int cost) {
		return this.cost = cost;
	}

	public int getCost() {
		return this.cost;
	}

	
	public Coordinates add(int x, int y){
		return new Coordinates(this.x + x, this.y + y);
	}
	
	public Coordinates add(Coordinates c){
		return add(c.getX(),c.getY());
	}
	
	@Override
	public boolean equals(Object o){
		if (o == null) return false;
		if (o.getClass() != Coordinates.class) return false;
		Coordinates c = (Coordinates) o;
		//System.out.println(x + ", " + y + " <--> " + c.getX() + ", " + c.getY() + " > " + (c.getY() == y && c.getY() == y));
		return (c.getX() == x && c.getY() == y);
	}
	
	@Override
	public String toString(){
		return "[" + x + "," + y +"]";
	}
	
}
