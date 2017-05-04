import java.util.*;


public class FreightSearch {
	private FreightGraph<String> graph;
	
	public FreightSearch(){
		this.graph = new FreightGraph<String>();
	}
	
	public FreightGraph<String> getGraph(){
		return graph;
	}
	
	public boolean bfs(FreightNode<String> start, FreightNode<String> find){
		Queue<FreightNode<String>> frontier = new LinkedList<FreightNode<String>>();
		ArrayList<FreightNode<String>> explored = new ArrayList<FreightNode<String>>();
		frontier.add(start);
		
		while (frontier.peek() != null){
			FreightNode<String> curr = frontier.poll();
			System.out.println(curr.getName());
			explored.add(curr);
			ArrayList<FreightNode<String>> successors = curr.successors();
			
			for (FreightNode<String> n: successors){
				if (n.equals(find)) return true;
				
				if(!explored.contains(n) && !frontier.contains(n))
					frontier.add(n);
			}
		}
		
		return false;
	}
	

	/**
	 * Performs an A* Search
	 * @param start - the starting FreightNode
	 * @param find  - the desired end FreightNode
	 * @returns the path from start to node or null if no path found
	 */
	public ArrayList<FreightNode<String>> aStarSearch(FreightNode<String> start, FreightNode<String> find){
		Comparator<FreightNode<String>> comp = new FreightNodeComparator<String>();
		PriorityQueue<FreightNode<String>> open = new PriorityQueue<FreightNode<String>>(comp);
		ArrayList<FreightNode<String>> closed = new ArrayList<FreightNode<String>>();

		start.setCompare(getHeuristic(start));
		open.add(start);
		
		while (!open.isEmpty()){
			FreightNode<String> curr = open.poll();
			closed.add(curr);
			
			if (curr.equals(find)){
				ArrayList<FreightNode<String>> list = new ArrayList<FreightNode<String>>();
				list.add(curr);
				return getPath(list);
			}
			
			ArrayList<FreightNode<String>> successors = curr.successors();
			for (FreightNode<String> n: successors){
				
				if (closed.contains(n)) continue;
				
				if (!open.contains(n)){
					n.setCompare(getHeuristic(n) + graph.getEdgeCost(curr, n) + curr.getCompare() - getHeuristic(curr));
					n.setFrom(curr);
					open.add(n);
				}
			}
			System.out.println(open);
		}
				
		return null;
	}
	
	
	/**
	 * Gets a path using set 'from' values in each FreighNode
	 * @param l - A list containing the desired path to in its first parameter
	 * @pre l has at least one (non-null) element
	 * @returns the path from the the first found node with getFrom() == null to
	 * 	the first element of l (i.e. [someNode, ... , l[0]]
	 */
	private ArrayList<FreightNode<String>> getPath(ArrayList<FreightNode<String>> l){
		//this function is recursive
		int start = 0;
		
		if (l.get(start).getFrom() == null) return l;
		
		l.add(start,l.get(start).getFrom());
		return getPath(l);
	}
	
	
	private int getHeuristic(FreightNode<String> n){
		//this is a stub atm
		return n.getUnloading();
		//return 0;
	}
	
	
	
	
}

