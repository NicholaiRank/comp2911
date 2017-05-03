import java.util.*;

public class GameBoard {
	private ArrayList<ArrayList<Object>> matrix;
	private int y;
	private int x;
	
	public GameBoard(int x,int y){
		this.matrix = new ArrayList<ArrayList<Object>>(y);
		for (int i = 0; i < y; ++i) {
			this.matrix.add(new ArrayList<Object>());
			for (int j = 0; j < x; j++){
				this.matrix.get(i).add(null);
			}
		}
		this.y = y;
		this.x = x;
	}
	
	/**
	 * Adds an outerWall to the board (it is recommended this is used
	 *  immediately after creating a new board)
	 */
	public void addOuterWall(){
		System.out.println(x);
		for (int i = 0; i < x; i++){
			Wall w = new Wall();
			addObj(w,i,0);
			addObj(w,i,y-1);
		}
		for (int i = 1; i < y-1; i++){
			Wall w = new Wall();
			addObj(w,0,i);
			addObj(w,x-1,i);
		}
	}
	
	/**
	 * Returns the Object at the coordinates (x,y)
	 * @pre (x,y) is in GameBoard (x <= this.x && y <= this.y)
	 * @param x - The x coordinate
	 * @param y - The y coordinate
	 */
	public Object getObjectAt(int x, int y){
		return matrix.get(y).get(x);
	}
	
	/**
	 * Returns the Object at the coordinates (x,y)
	 * @pre (x,y) is in GameBoard (c.getX() <= this.x && c.getY() <= this.y)
	 * @param c - The Coordinates of the wanted object
	 */
	public Object getObjectAt(Coordinates c){
		return getObjectAt(c.getX(), c.getY());
	}
	
	/**
	 * Gets the location of an Object on  GameBoard
	 * @param c - The Coordinates of the wanted object
	 * @return The Coordinates of the object or null if not found (or null)
	 */
	public Coordinates getLocationOf(Object searchObj){
		for (int i = 0; i < y; ++i) {
			for (int j = 0; j < x; j++){
				Object curr = matrix.get(i).get(j);
				if (curr != null && curr.equals(searchObj)){
					Coordinates c = new Coordinates(j,i);
					return c;
				}
			}
		}
		return null;
	}
	
	/**
	 * Adds an object to the GameBoard, rewrites over anything
	 * 	that was there
	 * @pre (x,y) is in GameBoard (x <= this.x && y <= this.y)
	 * @param x - The x coordinate
	 * @param y - The y coordinate
	 */
	public void addObj(Object obj,int x,int y){
		removeObj(x,y);
		matrix.get(y).set(x, obj);
	}
	
	/**
	 * Adds an object to the GameBoard, rewrites over anything
	 * 	that was there
	 * @pre (x,y) is in GameBoard (c.getX() <= this.x && c.getY() <= this.y)
	 * @param c - The Coordinates of the object to be added
	 */
	public void addObj(Object obj,Coordinates c){
		addObj(obj,c.getX(),c.getY());
	}
	
	/**
	 * Sets the position (x,y) to null on GameBoard
	 * @pre (x,y) is in GameBoard (x <= this.x && y <= this.y)
	 * @param x - The x coordinate
	 * @param y - The y coordinate
	 */
	public void removeObj(int x,int y){
		matrix.get(y).set(x, null);
	}
	
	/**
	 * Sets the position (x,y) to null on GameBoard
	 * @pre (x,y) is in GameBoard (c.getX() <= this.x && c.getY() <= this.y)
	 * @param c - The Coordinates of the object to be added
	 */
	public void removeObj(Coordinates c){
		removeObj(c.getX(),c.getY());
	}
	
	/**
	 * Moves an object from its current location to
	 * 	(x,y) on GameBoard
	 * @pre (x,y) is in GameBoard (x <= this.x && y <= this.y)
	 * @param x - The x coordinate
	 * @param y - The y coordinate
	 */
	public void moveObj(Object obj,int x,int y){
		Coordinates c = getLocationOf(obj);
		removeObj(c);
		addObj(obj,x,y);
	}
	
	/**
	 * Moves an object from its current location to
	 * 	(x,y) on GameBoard
	 * @pre (x,y) is in GameBoard (c.getX() <= this.x && c.getY() <= this.y)
	 * @param c - The desired, final Coordinates of the object
	 */
	public void moveObj(Object obj,Coordinates c){
		moveObj(obj,c.getX(),c.getY());
	}
	
	
	@Override
	public String toString(){
		String retString = "";
		for (int i = 0; i < y; ++i) {
			for (int j = 0; j < x; j++){
				GameBoardObject addText = (GameBoardObject) matrix.get(i).get(j);
				if (addText == null) retString += " ";
				else retString += addText.getByteRep();
			}
			retString += "\n";
		}
		
		return retString;
	}
}
