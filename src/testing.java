
public class testing {

	public static void main(String[] args) {
		GameBoard g = new GameBoard(10,10);
		Integer in = new Integer(1);
		
		System.out.println(g);
		g.addObj(in, 1, 1);
		g.addObj("2", 2, 2);
		g.addObj("3", 3, 3);
		System.out.println();
		System.out.println(g);
		g.moveObj(in, 4, 4);
		System.out.println();
		System.out.println(g);
	}

}
