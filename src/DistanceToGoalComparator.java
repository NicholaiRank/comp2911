import java.util.Comparator;

public class DistanceToGoalComparator implements Comparator<Coordinates>
{

	@Override
	public int compare(Coordinates o1, Coordinates o2) 
	{
	    return Integer.compare(o1.getCost(), o2.getCost());
	}
	
	
}