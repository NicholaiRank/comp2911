
public class testing {

	public static void main(String[] args) {
		GameBoard g = new GameBoard(10,10);
		System.out.println(g);
		g.addObj("1", 1, 3);
		//g.removeObj(1,3);
		g.addObj("3", 1, 3);
		//g.addObj("4", 0, 0);
		System.out.println();
		System.out.println(g);
		
	}

}
