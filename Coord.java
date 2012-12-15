/**
 * Class to contain two integers representing the x and y coordinates of a 
 * point 
 * @author Robert Colgan (rec2111)
 *
 */
public class Coord {
	int x, y;
	
	/**
	 * Constructor for the Coord object. Takes ints for x y and makes a 
	 * new Coord.
	 * @param xIn The x
	 * @param yIn The y
	 */
	public Coord(int xIn, int yIn)
	{
		x = xIn;
		y = yIn;		
	}

	/**
	 * Gets the x coordinate
	 * @return the x
	 */
	public int getX() {
		return x;
	}

	/**
	 * Gets the y coordinate
	 * @return the y
	 */
	public int getY() {
		return y;
	}
}
