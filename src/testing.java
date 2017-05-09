public class testing{

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
		
		
		while (true){}
		
		/*
		System.out.println(g);
		Interaction i = new Interaction(g);
		System.out.println("Moving left");
		System.out.println(i.moveLeft(p));
		System.out.println(g);
		System.out.println(i.moveLeft(p));
		System.out.println(g);
		System.out.println(i.moveLeft(p));
		System.out.println(g);
		System.out.println(i.moveLeft(p));
		System.out.println(g);
		System.out.println(i.moveLeft(p));
		System.out.println(g);
		System.out.println(i.moveLeft(p));
		System.out.println(g);
		System.out.println(i.moveLeft(p));
		System.out.println(g);
		System.out.println("Moving down");
		System.out.println(i.moveDown(p));
		System.out.println(g);
		System.out.println(i.moveDown(p));
		System.out.println(g);
		System.out.println(i.moveDown(p));
		System.out.println(g);
		System.out.println(i.moveDown(p));
		System.out.println(g);
		System.out.println("Moving up");
		System.out.println(i.moveUp(p));
		System.out.println(g);
		System.out.println(i.moveUp(p));
		System.out.println(g);
		System.out.println(i.moveUp(p));
		System.out.println(g);
		System.out.println(i.moveUp(p));
		System.out.println(g);
		System.out.println("Moving right");
		System.out.println(i.moveRight(p));
		System.out.println(g);
		System.out.println(i.moveRight(p));
		System.out.println(g);
		System.out.println(i.moveRight(p));
		System.out.println(g);
		System.out.println(i.moveRight(p));
		System.out.println(g);
		System.out.println(i.moveRight(p));
		System.out.println(g);
		*/
	}
	
}
