public class testing{

	public static void main(String[] args) {
		Player p = new Player("Tom");
		GameBoardGen g = new GameBoardGen(10,10,3,p);
		GameBoard ga = new GameBoard(10,10);
		ga.addOuterWall();
		for (Coordinates c: g.getPath()){
			ga.addObj(new Wall(), c);
		}
		
		System.out.println(g.getBoard());
		System.out.println(ga);
		
	}
	
}
