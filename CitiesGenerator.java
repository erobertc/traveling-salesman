import java.util.*;


public class CitiesGenerator {
	
	/**
	 * Generates an ArrayList of n unique cities
	 * @param n The number of unique cities to make
	 * @param xRange The maximum x coordinate
	 * @param yRange The maximum y coordinate
	 * @return An ArrayList of generated cities
	 */
	public static ArrayList<City> MakeCities(int n, int xRange, int yRange)
	{
		Random gen = new Random();
		ArrayList<City> cities = new ArrayList<City>();
		ArrayList<Coord> coordsUsed = new ArrayList<Coord>();
		for (int i = 0; i < n; i++)
		{
			boolean unique = false;
			int x = -1, y = -1;
			while (!unique)
			{
				x = gen.nextInt(xRange);
				y = gen.nextInt(yRange);
				unique = true;
				for (Coord c : coordsUsed)
				{
					if (c.getX() == x && c.getY() == y)
						unique = false;
				}
			}
			City newCity = new City(null, x, y, -1);
			cities.add(newCity);
			coordsUsed.add(new Coord(x, y));
		}
		return cities;
	}

}
