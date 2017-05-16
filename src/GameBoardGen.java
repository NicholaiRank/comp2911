import java.util.*;

public class GameBoardGen{
	private GameBoard board;
	private ArrayList<Coordinates> path;

	public GameBoardGen(int x, int y, int numBoxes, Player p){
		this.board = new GameBoard(x,y);
		ArrayList<ArrayList<Coordinates>> arr = addRandomObjs(numBoxes,p);
		this.path = puzzleGen(board.getLocationOf(p),arr.get(0),arr.get(1));
		ArrayList<ArrayList<Coordinates>> tempPaths = getRandomPaths();
		createBoard(tempPaths);
	}

	
	public GameBoard getBoard(){
		return board;
	}
	
	public ArrayList<Coordinates> getPath(){
		return path;
	}

	private ArrayList<ArrayList<Coordinates>> addRandomObjs(int numBoxes, Player p){
		//probably should add capability to have multiple players
		board.addOuterWall();
		board.addObj(p, getRandomCoords());
		ArrayList<Coordinates> goals = new ArrayList<Coordinates>();
		ArrayList<Coordinates> boxes = new ArrayList<Coordinates>();
		
		for (int i = 0; i < numBoxes; i ++){
			Goal g = new Goal();
			Box b = new Box();

			Coordinates bc = getRandomCoords();
			board.addObj(b,bc);
			goals.add(bc);
			
			Coordinates gc = getRandomCoords();
			board.addObj(g,gc);
			boxes.add(gc);
		}
		
		ArrayList<ArrayList<Coordinates>> toReturn = new ArrayList<ArrayList<Coordinates>>();
		toReturn.add(boxes);
		toReturn.add(goals);
		return toReturn;
	}



	private Coordinates getRandomCoords(){
		//coordinates are offset so that they can definitley be pushed
		
		Random rand = new Random();
		int  x = rand.nextInt(board.getX() -4) +2;
		int  y = rand.nextInt(board.getY() -4) +2;

		//gets random coordinates
		Coordinates rc = new Coordinates(x,y);
		while (board.getObjectAt(rc) != null){
			x = rand.nextInt(board.getX() -4) +2;
			y = rand.nextInt(board.getY() -4) +2;

			rc = new Coordinates(x,y);
		}
		return rc;
	}


	/**
	 * 
	 * @return
	 * @pre path is set
	 */
	private ArrayList<ArrayList<Coordinates>> getRandomPaths(){
		//	and then creates x number of paths of random bits of the
		//  path, where x = [length(path)] / 2
		ArrayList<ArrayList<Coordinates>> paths = new ArrayList<ArrayList<Coordinates>>();

		for (int i = 0; i < path.size() ; i++){
			//gets the coordinates of an element on the path
			Coordinates pos = path.get(i);//path.get(rand.nextInt(path.size()));
			ArrayList<Coordinates> tempPath = new ArrayList<Coordinates>();

			//generates random next coordinate
			// where the coordinate is in the immediate radius
			// of pos
			pos = getRandomOffset(pos);
			
			while (!path.contains(pos) && board.getObjectAt(pos) == null){
				tempPath.add(pos);
				pos = getRandomOffset(pos);
			}
			paths.add(tempPath);
		}
		return paths;

	}
	
	private Coordinates getRandomOffset(Coordinates pos){
		Random rand = new Random();
		
		ArrayList<Coordinates> possibleMoves = new ArrayList<Coordinates>();
		possibleMoves.add(new Coordinates(0,1));
		possibleMoves.add(new Coordinates(1,0));
		possibleMoves.add(new Coordinates(0,-1));
		possibleMoves.add(new Coordinates(-1,0));

		int offset = rand.nextInt(possibleMoves.size());
		Coordinates retCoords = pos.add(possibleMoves.get(offset));
		while (retCoords.getX() >= board.getX() || retCoords.getY() >= board.getY()){
			offset = rand.nextInt(possibleMoves.size());
			retCoords = pos.add(possibleMoves.get(offset));
		}
		return retCoords;
	}


	private void createBoard(ArrayList<ArrayList<Coordinates>> randomPaths){
		//for (ArrayList<Coordinates> a: randomPaths)
		//	System.out.println(a);
		
		//assumes board already has outer wall and boxes, goals and players in place
		for (int i = 0; i < board.getY(); ++i) {
			for (int j = 0; j < board.getX(); j++){
				Coordinates cors = new Coordinates(j,i);
				Object curr = board.getObjectAt(cors);

				if (curr == null){
					if (!path.contains(cors) && !contains2DArr(randomPaths,cors)){
						//add wall
						Wall w = new Wall();
						board.addObj(w,cors);
					}
				}
			}
		}
	}

	private boolean contains2DArr(ArrayList<ArrayList<Coordinates>> l,Coordinates c){
		for (int i = 0; i < l.size(); ++i) {
			ArrayList<Coordinates> curr = l.get(i);
			if (curr.contains(c)) return true;
		}

		return false;
	}

	
	private ArrayList<Coordinates> puzzleGen(Coordinates player, ArrayList<Coordinates> boxes, ArrayList<Coordinates> goals)
	{
		//takes in a gameboard.
		//need to have a valid path from player to box to goal.
		//two a star searches and combine the final result
		//combined path will be a complete list of untouchable coordinates where the walls cannot be placed

		Queue<Coordinates> q = new LinkedList<Coordinates>();
		q.addAll(goals);
		ArrayList<Coordinates> path = new ArrayList<Coordinates>();
		ArrayList<Coordinates> nextPath = new ArrayList<Coordinates>();
		ArrayList<Coordinates> combinedPath = new ArrayList<Coordinates>(); //all untouchable coordinates


		for(Coordinates box: boxes)
		{
			path = getPath(player,box);
			nextPath = getPath(box,q.poll());
			path.addAll(nextPath);
			combinedPath.addAll(path);
		}

		return combinedPath;
	}


	private ArrayList<Coordinates> getPath(Coordinates start, Coordinates finish)
	{
		ArrayList<Coordinates> path = new ArrayList<Coordinates>();
		path.add(start);
		Comparator<Coordinates> comparator = new DistanceToGoalComparator();
		PriorityQueue<Coordinates> q =  new PriorityQueue<Coordinates>(10, comparator); 

		ArrayList<Coordinates> adjacentAvailable = new ArrayList<Coordinates>();
		Coordinates current = start;

		while(current.getX()!=finish.getX() && current.getY()!=finish.getY()) //mightnt be correct condition
		{
			//taking the best heuristic at each stage
			adjacentAvailable.addAll(getadjacent(current,finish));
			q.addAll(adjacentAvailable);
			current = q.poll();
			path.add(current);
			//q.clear();
		}

		return path;
	}


	private int getHeuristicDistance(Coordinates current, Coordinates goal)
	{
		double distance = Math.hypot(current.getX()-goal.getX(),current.getY()-goal.getY());
		int value = (int)distance;
		return value;
	}

	private ArrayList<Coordinates> getadjacent(Coordinates current,Coordinates goal)
	{

		//add in the adjacents coordinates with their heuristic distances to goal

		ArrayList<Coordinates> adjacent = new ArrayList<Coordinates>();
		int value =0;

		Coordinates check = new Coordinates(current.getX()-1,current.getY());
		if(board.getObjectAt(check)==null)
		{
			value = getHeuristicDistance(check,goal);
			check.setCost(value);
			adjacent.add(check);


		}

		check = new Coordinates(current.getX()+1,current.getY());
		if(board.getObjectAt(check)==null)
		{
			value = getHeuristicDistance(check,goal);
			check.setCost(value);
			adjacent.add(check);
		}

		check = new Coordinates(current.getX(),current.getY()-1);
		if(board.getObjectAt(check)==null)
		{
			value = getHeuristicDistance(check,goal);
			check.setCost(value);
			adjacent.add(check);
		}


		check = new Coordinates(current.getX(),current.getY()+1);
		if(board.getObjectAt(check)==null)
		{
			value = getHeuristicDistance(check,goal);
			check.setCost(value);
			adjacent.add(check);
		}

		return adjacent;

	}


}