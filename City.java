/**
 * Class to represent the cities this program computes the TSP route for. 
 * Contains fields for name, x and y coordinate as given on the input, the  
 * city's index number, and angle from the lower rightmost city
 * @author Robert Colgan (rec2111)
 *
 */
public class City implements Comparable<City>{

	private String name;
	private int xCor, yCor, canvasX, canvasY;
	private double angle, dist;
	private int index;
	
	/**
	 * Constructor for the city
	 * @param nameIn City name
	 * @param x x coordinate as given in input file
	 * @param y y coordinate as given in input file
	 * @param indexIn Index number of city
	 */
	public City(String nameIn, int x, int y, int indexIn)
	{
		name = nameIn;
		xCor = x;
		yCor = y;
		index = indexIn;
	}
	
	/**
	 * Compares cities by angle, returning -1 if the city has a smaller angle 
	 * than the other city, 0 if they have the same angle, and 1 if the city
	 * has a larger angle than the other city, so cities are sorted in 
	 * increasing order by angle.
	 */
	public int compareTo(City other)
	{
		if (this.getAngle() > other.getAngle())
			return 1;
		else if (this.getAngle() == other.getAngle())
			return 0;
		else return -1;
	}
	
	/**
	 * Sets the canvas x coordinate for position to draw on the canvas
	 * @param xIn x coordinate for canvas
	 */
	public void setCanvasX(int xIn)
	{
		canvasX = xIn;
	}
	
	/**
	 * Sets the canvas y coordinate for position to draw on the canvas
	 * @param yIn y coordinate for canvas
	 */
	public void setCanvasY(int yIn)
	{
		canvasY = yIn;
	}
	
	/**
	 * Returns canvas x coordinate
	 * @return canvas x coordinate
	 */
	public int getCanvasX()
	{
		return canvasX;
	}
	
	/**
	 * Returns canvas y coordinate
	 * @return canvas y coordinate
	 */
	public int getCanvasY()
	{
		return canvasY;
	}
	
	/**
	 * Sets the canvas x coordinate for position to draw on the canvas
	 * @param xIn x coordinate for canvas
	 */
	public void setAngle(double angleIn)
	{
		angle = angleIn;
	}
	
	/**
	 * Returns angle
	 * @return angle
	 */
	public double getAngle()
	{
		return angle;
	}
	
	/**
	 * Returns city's name
	 * @return city's name
	 */
	public String getName()
	{
		return name;
	}
	
	/**
	 * Returns city's original x coordinate
	 * @return city's original x coordinate
	 */
	public int getX()
	{
		return xCor;
	}
	
	/**
	 * Returns city's original y coordinate
	 * @return city's original y coordinate
	 */
	public int getY()
	{
		return yCor;
	}
	
	/**
	 * Sets city's index number to the value provided
	 * @param indexIn New index number
	 */
	public void setIndex(int indexIn)
	{
		index = indexIn;
	}
	
	/**
	 * Returns city's index number
	 * @return city's index number
	 */
	public int getIndex()
	{
		return index;
	}

	/**
	 * Gets the distance of this city from the upper left corner
	 * @return the distance
	 */
	public double getDist() 
	{
		return dist;
	}

	/**
	 * Sets the distance of the city
	 * @param dist the dist to set
	 */
	public void setDist(double dist) 
	{
		this.dist = dist;
	}
}
