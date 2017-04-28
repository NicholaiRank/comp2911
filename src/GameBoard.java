import java.util.*;

public class GameBoard {
	private ArrayList<ArrayList<Object>> matrix;
	private int x;
	private int y;
	
	public GameBoard(int x,int y){
		this.matrix = new ArrayList<ArrayList<Object>>(x);
		for (int i = 0; i < x; ++i) {
			this.matrix.add(new ArrayList<Object>());
			for (int j = 0; j < y; j++){
				this.matrix.get(i).add(null);
			}
		}
		this.x = x;
		this.y = y;
	}
	
	/**
	 * Returns the Object at the coordinates (x,y)
	 * @pre (x,y) is in GameBoard (x <= this.x && y <= this.y)
	 * @param x - The x coordinate
	 * @param y - The y coordinate
	 */
	public Object getObjectAt(int x, int y){
		return matrix.get(x).get(y);
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
		for (int i = 0; i < x; ++i) {
			for (int j = 0; j < y; j++){
				Object curr = matrix.get(i).get(j);
				if (curr.equals(searchObj)){
					Coordinates c = new Coordinates(i,j);
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
		matrix.get(x).add(y, obj);
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
	 * Removes an object from the GameBoard
	 * @pre (x,y) is in GameBoard (x <= this.x && y <= this.y)
	 * @param x - The x coordinate
	 * @param y - The y coordinate
	 */
	public void removeObj(int x,int y){
		System.out.println(y);
		matrix.get(x).set(y, null);
	}
	
	/**
	 * Removes an object from the GameBoard
	 * @pre (x,y) is in GameBoard (c.getX() <= this.x && c.getY() <= this.y)
	 * @param c - The Coordinates of the object to be added
	 */
	public void removeObj(Coordinates c){
		removeObj(c.getX(),c.getY());
	}
	
	@Override
	public String toString(){
		String retString = "";
		for (int i = 0; i < x; ++i) {
			for (int j = 0; j < y; j++){
				Object addText = matrix.get(i).get(j);
				if (addText == null) addText = "X";
				retString += addText;
			}
			retString += "\n";
		}
		
		return retString;
	}
}
