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

	
	private ArrayList<Coordinates> puzzleGen(Coordinates player, ArrayList<Coordinates> goals, ArrayList<Coordinates> boxes)
	{
		//was initially passed in wrongly
		//takes in a gameboard.
		//need to have a valid path from player to box to goal.
		//two a star searches and combine the final result
		//combined path will be a complete list of untouchable coordinates where the walls cannot be placed

		//randomly generate whether path will be side-up or up-side for the harder cases.

		Queue<Coordinates> q = new LinkedList<Coordinates>();
		q.addAll(goals);
		ArrayList<Coordinates> path = new ArrayList<Coordinates>();
		ArrayList<Coordinates> nextPath = new ArrayList<Coordinates>();
		ArrayList<Coordinates> combinedPath = new ArrayList<Coordinates>(); //all untouchable coordinates


		for(Coordinates box: boxes)
		{
			
			Coordinates goal = q.poll();

			if(box.getX() == goal.getX() && box.getY() < goal.getY())
			{
				path = getPath(player,new Coordinates(box.getX(),box.getY()-1));

				for(int i=box.getY();i<goal.getY();i++)
				{
					Coordinates added = new Coordinates(box.getX(),i);
					path.add(added);

				}

			}

			else if(box.getX() == goal.getX() && box.getY() > goal.getY())
			{					
				path = getPath(player,new Coordinates(box.getX(),box.getY()+1));
				for(int i=goal.getY();i<box.getY();i++)
				{
					path.add(new Coordinates(goal.getX(),i));
				}
			}


			else if(box.getX() > goal.getX() && box.getY() == goal.getY())
			{
				//goal is to the left of box.
				//come in from the box's right side + clear path to goal
				path = getPath(player,new Coordinates(box.getX()+1,box.getY()));
				for(int i=goal.getX();i<box.getX();i++)
				{
					path.add(new Coordinates(i,goal.getY()));
				}
			}


			else if(box.getX() < goal.getX() && box.getY() == goal.getY())
			{

				//goal is to the right of the box.
				//come in from left of box and clear path to goal
				path = getPath(player,new Coordinates(box.getX()-1,box.getY()));
				for(int i=box.getX();i<goal.getX();i++)
				{
					path.add(new Coordinates(i,box.getY()));
				}
			}


			//harder paths to goals: top left, top right,bottom left, bottom right

			else if(box.getX() > goal.getX() && box.getY() > goal.getY())  
			{
				//goal is top left
				//gaining access right and bottom
				String direction= "TL";				
				path = getPath(player,new Coordinates(box.getX(),box.getY()+1));
				
				Coordinates added = new Coordinates(box.getX()+1,box.getY());
				path.add(added);

				added = new Coordinates(box.getX()+1,box.getY()-1);
				path.add(added);
				
				nextPath = getSidePath(box,goal,direction);
				path.addAll(nextPath);
				nextPath.clear();	
			}


			else if(box.getX() < goal.getX() && box.getY() > goal.getY())  
			{
				//goal is top right
				//gaining accessing to box from left and bottom
				
				String direction= "TR";

				path = getPath(player,new Coordinates(box.getX(),box.getY()+1)); //correct
				
				Coordinates added = new Coordinates(box.getX()-1,box.getY());
				path.add(added);
				
				added = new Coordinates(box.getX()-1,box.getY()+1);
				path.add(added);

				nextPath = getSidePath(box,goal,direction);
				path.addAll(nextPath);
				nextPath.clear();
				
			}

			else if(box.getX() > goal.getX() && box.getY() < goal.getY())  
			{
				//bottom left
				//gaining accessing to box from right and top(yes)

				String direction= "BL";
				path = getPath(player,new Coordinates(box.getX(),box.getY()-1));

				Coordinates added = new Coordinates(box.getX()+1,box.getY());
				path.add(added);
				//System.out.println("added: "+added.getX()+","+added.getY());

				added = new Coordinates(box.getX()+1,box.getY()-1);
				path.add(added);
				//System.out.println("added: "+added.getX()+","+added.getY());

				nextPath = getSidePath(box,goal,direction);
				path.addAll(nextPath);
				nextPath.clear();
			}

			else if(box.getX() < goal.getX() && box.getY() < goal.getY())  
			{
				//bottom right
				//gaining accessing to box from left and top yes
				String direction= "BR";
				path = getPath(player,new Coordinates(box.getX(),box.getY()-1));

				Coordinates added = new Coordinates(box.getX()-1,box.getY());
				path.add(added);
				
				added = new Coordinates(box.getX()-1,box.getY()-1);
				path.add(added);
					
				nextPath = getSidePath(box,goal,direction);
				path.addAll(nextPath);
				nextPath.clear();				
			}

			else
			{
				//here just in case never quite reaches here
				path = getPath(player,box);
			}

			combinedPath.addAll(path);
		}

		return combinedPath;
	}


	private ArrayList<Coordinates> getPath(Coordinates start, Coordinates finish)
	{
		ArrayList<Coordinates> path = new ArrayList<Coordinates>();
		path.add(start);

		ArrayList<Coordinates> adjacentAvailable = new ArrayList<Coordinates>();
		Coordinates current = start;

		while (checkIfSame(current,finish)!=true) 
		{
			current = getClosest(current,finish,path);
			//System.out.println("just added: " +current.getX() + "," +current.getY());
			path.add(current);
		}

		return path;
	}


	private int getHeuristicDistance(Coordinates current, Coordinates goal)
	{
		double distance = Math.hypot(current.getX()-goal.getX(),current.getY()-goal.getY());
		int value = (int)distance;
		return value;
	}

	private Coordinates getClosest(Coordinates current,Coordinates goal,ArrayList<Coordinates> path)
	{

		//add in the adjacents coordinates with their heuristic distances to goal
		Comparator<Coordinates> comparator = new DistanceToGoalComparator();

		PriorityQueue<Coordinates> q =  new PriorityQueue<Coordinates>(10, comparator); 

		ArrayList<Coordinates> adjacent = new ArrayList<Coordinates>();
		int value =0;

		Coordinates check = new Coordinates(current.getX()-1,current.getY());

		if(board.getObjectAt(check)==null || board.getObjectAt(check) instanceof Goal || board.getObjectAt(check) instanceof Player || checkIfSame(check,goal)==true)
		{
			if(!path.contains(check))
			{
				value = getHeuristicDistance(check,goal);
				check.setCost(value);
				q.add(check);
			}

		}

		check = new Coordinates(current.getX()+1,current.getY());


		if(board.getObjectAt(check)==null || board.getObjectAt(check) instanceof Goal || board.getObjectAt(check) instanceof Player ||checkIfSame(check,goal)==true)
		{
			if(!path.contains(check))
			{
				value = getHeuristicDistance(check,goal);
				check.setCost(value);
				q.add(check);
			}
		}


		check = new Coordinates(current.getX(),current.getY()-1);
		
		if(board.getObjectAt(check)==null || board.getObjectAt(check) instanceof Goal || board.getObjectAt(check) instanceof Player || checkIfSame(check,goal)==true)
		{
			if(!path.contains(check))
			{
				value = getHeuristicDistance(check,goal);
				check.setCost(value);
				q.add(check);
			}
		}

		check = new Coordinates(current.getX(),current.getY()+1);
		
		if(board.getObjectAt(check)==null || board.getObjectAt(check) instanceof Goal || board.getObjectAt(check) instanceof Player || checkIfSame(check,goal)==true)
		{
			if(!path.contains(check))
			{
				value = getHeuristicDistance(check,goal);
				check.setCost(value);
				q.add(check);
			}
		}

		if(q.isEmpty())
		{
			//q.add(new Coordinates(5,5)); //might be issue here
			System.out.println("empty queue EXITING"); 
			System.exit(0);
		}

		Coordinates next = q.poll();

		q.clear();
		return next;
	}


	private ArrayList<Coordinates> getSidePath(Coordinates box,Coordinates goal,String direction)
	{
		ArrayList<Coordinates> path = new ArrayList<Coordinates>();	

		if(direction.equals("TR"))
		{

			for(int i = box.getX();i<=goal.getX(); i++)
			{
				Coordinates added = new Coordinates(i,box.getY());
				path.add(added);

			}
			//clear 2 directly below
			int last = path.size() - 1;
			Coordinates c = path.get(last);
			path.add(new Coordinates(c.getX(),c.getY()+1)); 
			path.add(new Coordinates(c.getX()-1,c.getY()+1));	


			for(int i = goal.getY();i<= c.getY(); i++)
			{
				Coordinates added = new Coordinates(c.getX(),i);
				path.add(added);

			}	
			
		}

		else if(direction.equals("TL"))
		{
			
			for(int i = goal.getX();i<=box.getX(); i++)
			{
				Coordinates added = new Coordinates(i,box.getY());
				path.add(added);
			}

			// //clear 2 directly below
			Coordinates c = path.get(0);
			Coordinates curr = new Coordinates(c.getX(),c.getY()+1);
			path.add(curr); 
			curr = new Coordinates(c.getX()+1,c.getY()+1); 
			path.add(curr);	


			for(int i = goal.getY();i<= c.getY(); i++)
			{
				Coordinates added = new Coordinates(c.getX(),i);
				path.add(added);
			}	
		}



		else if(direction.equals("BR"))
		{
			for(int i = box.getX();i<=goal.getX(); i++)
			{
				Coordinates added = new Coordinates(i,box.getY());
				path.add(added);
			}

			Coordinates c = path.get(path.size()-1);
			Coordinates curr = new Coordinates(c.getX(),c.getY()-1);
			path.add(curr); 
			curr = new Coordinates(c.getX()-1,c.getY()-1); 
			path.add(curr);	

			for(int i = c.getY();i<= goal.getY(); i++)
			{
				Coordinates added = new Coordinates(c.getX(),i);
				path.add(added);
			}	

		}

		else if(direction.equals("BL"))
		{
			for(int i = goal.getX();i<=box.getX(); i++)
			{
				Coordinates added = new Coordinates(i,box.getY());
				path.add(added);
			}

			// clear from path(0) in this case
			Coordinates c = path.get(0);
			Coordinates curr = new Coordinates(c.getX(),c.getY()-1);
			path.add(curr); 
			curr = new Coordinates(c.getX()+1,c.getY()-1); 
			path.add(curr);	

			//downwards path

			for(int i = c.getY();i<= goal.getY(); i++)
			{
				Coordinates added = new Coordinates(c.getX(),i);
				path.add(added);
			}	

		}

		else
		{
			//System.out.println("reaches here path empty");
			path.add(new Coordinates(1,1));
			return path; //path is empty
		}

		path.add(new Coordinates(1,1)); //arbitrary point for safety checks-> remove later

		return path;
	}


	boolean checkIfSame(Coordinates start,Coordinates finish)
	{
		if(start.getX() == finish.getX() && start.getY() == finish.getY())
		{
			return true;
		}

		else
		{
			return false;
		}
		
	}

}




