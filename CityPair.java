
/**
 * Class to represent a pair of cities (i.e. an edge of the graph).
 * CityPairs are comparable by the distance between them so they can be sorted
 * into a queue.
 * @author Robert Colgan (rec2111)
 *
 */
public class CityPair implements Comparable<CityPair> {
	
	City city1, city2;
	double dist;
	
	public CityPair(City city1in, City city2in) 
	{
		city1 = city1in;
		city2 = city2in;
		dist = pythag(city1.getX(), city1.getY(), city2.getX(), city2.getY());
	}
	
	public double pythag(int x1, int y1, int x2, int y2)
	{
		int xdist = x2 - x1;
		int ydist = y2 - y1;
		return Math.hypot(xdist, ydist);
	}
	
	public double getDist()
	{
		return dist;
	}
	
	public City getCity1()
	{
		return city1;
	}
	
	public City getCity2()
	{
		return city2;
	}
	
	public int compareTo(CityPair other)
	{
		if (this.dist > other.getDist()) return 1;
		else if (this.dist == other.getDist()) return 0;
		else return -1;
	}

}
