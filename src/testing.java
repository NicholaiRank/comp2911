
public class testing {

	public static void main(String[] args) {
		GameBoard g = new GameBoard(10,10);
		Box b = new Box();
		
		System.out.println(g);
		g.addObj(new Box(), 1, 1);
		g.addObj(new Box(), 2, 2);
		g.addObj(b, 3, 3);
		System.out.println();
		System.out.println(g);
		g.moveObj(b, 4, 4);
		System.out.println();
		System.out.println(g);
	}

}
