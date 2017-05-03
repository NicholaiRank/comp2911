
public class testing {

	public static void main(String[] args) {
		GameBoard g = new GameBoard(20,10);
		g.addOuterWall();
		System.out.println(g);
		Player p = new Player("me");
		Box b = new Box();
		Goal o = new Goal();
		g.addObj(p,7,5);
		g.addObj(b,4,5);
		g.addObj(o,3,5);
		System.out.println(g);
		
		
		System.out.println(g);
		Interaction i = new Interaction(g);
		i.moveLeft(p);
		System.out.println(g);
		i.moveLeft(p);
		System.out.println(g);
		i.moveLeft(p);
		System.out.println(g);
		i.moveLeft(p);
		System.out.println(g);
		i.moveLeft(p);
		System.out.println(g);
	}

}
