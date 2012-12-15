import java.util.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

/**
 * Displays a window allowing a user to enter a number of cities. Draws the 
 * cities and allows a user to calculate and display the convex hull of the
 * cities, the best tour as determined by the Cheapest Insertion algorithm,
 * and the optimal tour determined by brute force. 
 * @author Robert Colgan (rec2111), modified from code obtained from
 * http://www.cs.columbia.edu/~allen/S12/NOTES/DisplaySimpleTree.java
 * and
 * http://www.cs.columbia.edu/~allen/S12/NOTES/InchwormSimulation.zip
 */
public class TSPgui implements ActionListener {
	private DrawingCanvas canvas;
	private JFrame frame;
	private JTextArea numCitiesInputArea;
	private ArrayList<City> cities;
	private ArrayList<City> remainingCities = new ArrayList<City>();
	private ArrayList<CityPair> convexHullEdges;
	private ArrayList<CityPair> tour;
	private final int OVAL_DIAM = 6, TEXT_AREA_WIDTH = 5;
	private CitiesGenerator g;
	private int xSize, ySize;

	/**
	 * Constructor for the GUI. Creates the window and buttons, and contains 
	 * actions for the buttons and the necessary code.
	 * @param xSizeIn x size of city display (i.e. greatest possible x 
	 * coordinate)
	 * @param xBufferIn Buffer to add to the window in the x direction
	 * @param ySizeIn y size of city display (i.e. greatest possible y
	 * coordinate)
	 * @param yBufferIn Buffer to add to the window in the y direction
	 */
	public TSPgui(int xSizeIn, int xBufferIn, 
			int ySizeIn, int yBufferIn)
	{
		xSize = xSizeIn;
		ySize = ySizeIn;

		frame = new JFrame("Traveling Salesman");
		frame.setPreferredSize(new Dimension(
				xSizeIn + xBufferIn, ySizeIn + yBufferIn));
		canvas = new DrawingCanvas();

		// The text input area and button
		JPanel topPanel = new JPanel(new FlowLayout());
		numCitiesInputArea = new JTextArea(1, TEXT_AREA_WIDTH);
		topPanel.add(numCitiesInputArea);
		JPanel buttonPanel = new JPanel(new java.awt.GridLayout(2,0));
		addButton(buttonPanel, "Display cities");
		addButton(buttonPanel, "Compute convex hull");
		addButton(buttonPanel, "Cheapest insertion tour");
		addButton(buttonPanel, "Optimal tour");
		topPanel.add(buttonPanel);

		// frame.getContentPane().add(canvas, BorderLayout.CENTER);
		frame.add(canvas, BorderLayout.CENTER);
		frame.add(topPanel, BorderLayout.NORTH);
		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		frame.pack();
		frame.setVisible(true);	
	}

	/**
	 * ActionPerformed method to define actions for buttons as required by 
	 * ActionListener interface
	 */
	public void actionPerformed(ActionEvent e)
	{
		String cmd = e.getActionCommand();
		if (cmd.equals("Display cities"))
			displayCities(Integer.parseInt(numCitiesInputArea.getText()));
		else if (cmd.equals("Compute convex hull"))
		{
			convexHullEdges = computeHull(cities);
			drawTour(convexHullEdges, Color.RED);
		}
		else if (cmd.equals("Cheapest insertion tour"))
		{
			cheapestInsertionTour(Color.BLUE);
		}
		else if (cmd.equals("Optimal tour"))
		{
			optimalTour(Color.GREEN);
		}
		// Insert other buttons here

		else throw new RuntimeException("No such button: " + cmd);
	}


	/**
	 * Computes by brute force the optimal tour of all cities and draws it. 
	 * @param color The color to draw the optimal tour in. 
	 */
	public void optimalTour(Color color)
	{
		ArrayList<CityPair> optimalTour = new ArrayList<CityPair>();
		ArrayList<CityPair> currentTour = new ArrayList<CityPair>();
		int numCities = cities.size();
		int[] order = new int[numCities];
		for(int i = 0; i < order.length; i++)
		{
			order[i] = i;
		}
		currentTour = makeTourFromCities(order, cities);
		optimalTour = currentTour;
		double currentLength = tourLength(currentTour);
		double optimalLength = currentLength;

		while(findNextPermutation(order))
		{
			currentTour = makeTourFromCities(order, cities);
			currentLength = tourLength(currentTour);
			if (currentLength < optimalLength)
			{
				optimalLength = currentLength;
				optimalTour = currentTour;
			}
		}		

		drawTour(optimalTour, color);

		canvas.drawString("Optimal tour length: " + optimalLength, 
				10, 540);
	}


	/**
	 * Given a permutation of integers in an array, finds the next permutation
	 * in lexicographic order.
	 * Based on the pseudocode from http://www.java-forums.org/advanced-java/
	 * 27505-array-permutations.html#post113556
	 * @param order The permutation
	 * @return True if successful, false if no next permutation found
	 */
	private boolean findNextPermutation(int[] order)
	{
		if (order.length < 2) return false;
		int i = order.length - 2;
		while (order[i] >= order[i + 1])
		{
			i--;
			if (i < 0) return false;
		}
		int j = order.length - 1;
		while (order[j] <= order[i])
			j--;
		int temp = order[i];
		order[i] = order[j];
		order[j] = temp;
		reverseElements(order, i + 1, order.length - 1);
		return true;
	}


	/**
	 * Helper method to reverse the elements of a subarray, provided the array
	 * and start and end indices
	 * @param elements The array to reverse the elements of
	 * @param start The index to start the reversal
	 * @param end The index to end the reversal
	 */
	private void reverseElements(int[] elements, int start, int end)
	{
		int[] reversedElements = new int[elements.length];
		int i = end;
		int j = start;
		while (i >= start)
		{
			reversedElements[j] = elements[i];
			i--; 
			j++;
		}
		for(int k = start; k <= end; k++)
		{
			elements[k] = reversedElements[k];
		}
	}


	/**
	 * Helper method to make a tour of an ArrayList of CityPairs given a list
	 * of all the cities and an order to visit them in
	 * @param order The order to visit the cities in
	 * @param allCities All the cities
	 * @return
	 */
	private ArrayList<CityPair> makeTourFromCities(int[] order, 
			ArrayList<City> allCities)
	{
		ArrayList<CityPair> tourMade = new ArrayList<CityPair>();
		for(int i = 1; i < order.length; i++)
		{
			tourMade.add(new CityPair(allCities.get(order[i - 1]), 
					allCities.get(order[i])));
		}
		tourMade.add(new CityPair(allCities.get(order[order.length - 1]), 
				allCities.get(order[0])));
		return tourMade;
	}


	/**
	 * Computes a tour using the cheapest insertion metric and draws it.
	 * @param color The color to draw the tour in.
	 */
	public void cheapestInsertionTour(Color color)
	{
		// Initializes the tour with the hull edges
		tour = new ArrayList<CityPair>();
		tour.addAll(convexHullEdges);

		while(remainingCities.size() > 0)
		{
			double smallestIncrease = 99999999; // Initialize to a large number
			CityPair[] smallestNewEdges = new CityPair[2];
			CityPair edgeToRemove = null;
			for(CityPair curEdge : tour)
			{
				CityPair[] newEdges = findCheapestInsertion(
						curEdge, remainingCities);
				double newDist = newEdges[0].getDist() + newEdges[1].getDist();
				double newIncrease = newDist - curEdge.getDist();
				if(newIncrease < smallestIncrease)
				{
					smallestIncrease = newIncrease;
					smallestNewEdges = newEdges;
					edgeToRemove = curEdge;
				}
			}
			// Now we have found the two new edges with which to replace an old
			// edge that will result in the smallest increase to the tour. We
			// remove the old edge and add the two new edges in its place, and 
			// remove the corresponding city from remainingCities.
			int oldEdgeIndex = tour.indexOf(edgeToRemove);
			tour.remove(edgeToRemove);
			tour.add(oldEdgeIndex, smallestNewEdges[0]);
			tour.add(oldEdgeIndex + 1, smallestNewEdges[1]);
			remainingCities.remove(smallestNewEdges[0].getCity2());
		}

		drawTour(tour, Color.BLUE);

		double tourLength = tourLength(tour);
		canvas.drawString("Cheapest insertion tour length: " + tourLength, 
				10, 520);
	}


	/**
	 * Helper method to calculate the length of a tour.
	 * @param tour The ArrayList of CityPairs to calculate the total length of
	 * @return The length of the tour given
	 */
	private double tourLength(ArrayList<CityPair> tourToCheck)
	{
		double tourLength = 0;
		for(CityPair p : tourToCheck)
		{
			tourLength += p.getDist();
		}
		return tourLength;
	}


	/**
	 * Helper method to find the cheapest city to insert for a given edge
	 * @param edge The edge to find the cheapest city to insert into it
	 * @param cities All the cities
	 * @return The cheapest city to insert
	 */
	public CityPair[] findCheapestInsertion(CityPair edge, 
			ArrayList<City> remainingCities)
	{
		CityPair[] newEdges = new CityPair[2];
		City c1 = edge.getCity1();
		City c2 = edge.getCity2();
		double origDist = edge.getDist();
		double smallestIncrease = 99999999; // Initialize to a large number
		for (City c : remainingCities)
		{
			CityPair pair1 = new CityPair(c1, c);
			CityPair pair2 = new CityPair(c, c2);
			double newDist = pair1.getDist() + pair2.getDist();
			double increase = newDist - origDist;
			if (increase < smallestIncrease) 
			{
				smallestIncrease = increase;
				newEdges[0] = pair1;
				newEdges[1] = pair2;
			}
		}
		return newEdges;
	}

	/**
	 * Draws a tour of cities provided as an ArrayList of CityPairs in a 
	 * specified color.
	 * @param edges The ArrayList of CityPairs to draw
	 * @param color The color to draw the tour in
	 */
	public void drawTour(ArrayList<CityPair> edgesToDraw, Color color)
	{
		Color origColor = canvas.getForeground();
		canvas.setForeground(color);
		for (CityPair edge : edgesToDraw)
		{
			City c1 = edge.getCity1();
			City c2 = edge.getCity2();
			canvas.drawLine(c1.getCanvasX() + (int) OVAL_DIAM / 2, 
					c1.getCanvasY() + (int) OVAL_DIAM / 2, 
					c2.getCanvasX() + (int) OVAL_DIAM / 2, 
					c2.getCanvasY() + (int) OVAL_DIAM / 2, false);
		}
		canvas.display();
		canvas.setForeground(origColor);
	}

	/**
	 * Computes the convex hull of an ArrayList of cities. Also sets up the
	 * ArrayList remainingCities as all the cities, then removes the cities it
	 * has added to the hull.
	 * @param citiesIn ArrayList of all the cities
	 * @return LinkedList of the cities in the convex hull
	 */
	public ArrayList<CityPair> computeHull(ArrayList<City> citiesIn)
	{
		remainingCities = new ArrayList<City>();
		ArrayList<City> cities = citiesIn;
		remainingCities.addAll(cities);

		// Finds the lowest city (city with smallest y value); if more than 
		// one, takes rightmost. 
		City p0 = null;
		int minY = 99999; // Initialize to a very large number
		ArrayList<City> ties = new ArrayList<City>();
		for (City c : cities)
		{
			if (c.getY() < minY)
			{
				minY = c.getY();
				p0 = c;
				ties.clear();
				ties.add(c);
			}
			else if (c.getY() == minY)
			{
				ties.add(c);
			}
		}
		if (ties.size() > 1)
		{
			int maxX = 0;
			for (City c : ties)
			{
				if (c.getX() > maxX)
				{
					maxX = c.getX();
					p0 = c;
				}
			}
		}

		// Removes p0 from the ArrayList of cities so it doesn't get its angle
		// set or sorted
		cities.remove(p0);

		// Calculates and sets the angles for all other cities from p0
		for (City c : cities)
		{
			double x = c.getX() - p0.getX();
			double y = c.getY() - p0.getY();

			if (y == 0) c.setAngle(180);
			else if (x == 0) c.setAngle(90);
			else if (x > 0)
			{
				double angleRad = Math.atan(y / x);
				double angle = Math.toDegrees(angleRad);
				c.setAngle(angle);
			}
			else if (x < 0)
			{
				double angleRad = Math.atan(y / x);
				double angle = Math.toDegrees(angleRad);
				angle += 180;
				c.setAngle(angle);
			}
		}

		// Sorts the cities using Quicksort
		Sorter.quicksort(cities);

		// Check for ties and break in favor of closeness to p0
		for(int i = 1; i < cities.size(); i++)
		{
			if(cities.get(i - 1).compareTo(cities.get(i)) == 0)
			{
				City c1 = cities.get(i - 1);
				City c2 = cities.get(i);
				int c1xDist = p0.getX() - c1.getX();
				int c2xDist = p0.getX() - c2.getX();
				int c1yDist = p0.getY() - c1.getY();
				int c2yDist = p0.getY() - c2.getY();
				double c1Hyp = Math.hypot(c1xDist, c1yDist);
				double c2Hyp = Math.hypot(c2xDist, c2yDist);
				if (c2Hyp < c1Hyp)
					Sorter.swapReferences(cities, i, i - 1);
			}
		}

		// Adds p0 back to the ArrayList of cities at the beginning
		cities.add(0, p0);

		// Indexes the cities in sorted order
		for(int i = 0; i < cities.size(); i++)
		{
			cities.get(i).setIndex(i);
		}

		// Graham's Algorithm
		LinkedList<City> stack = new LinkedList<City>();
		stack.push(cities.get(cities.size() - 1));
		remainingCities.remove(cities.get(cities.size() - 1));
		stack.push(p0);
		remainingCities.remove(p0);
		int i = 1;
		while(i < cities.size())
		{
			City pI = cities.get(i);

			if(isLeftTurn(pI, stack))
			{
				// If test prevents it from adding the last city a second time
				if (i < cities.size() - 1) stack.push(pI);
				remainingCities.remove(pI);
				i++;
			}
			else remainingCities.add(stack.pop());
		}
		// End Graham's Algorithm

		ArrayList<CityPair> hullEdges = new ArrayList<CityPair>();

		ListIterator<City> hullIterator = stack.listIterator(0);
		City P0 = hullIterator.next();
		City curCity = P0;
		City nextCity;
		// For all the cities in the hull, creates an edge between them and the
		// next city and adds it to the ArrayList of edges
		while(hullIterator.hasNext())
		{
			nextCity = hullIterator.next();
			CityPair newEdge = new CityPair(curCity, nextCity);
			hullEdges.add(newEdge);
			curCity = nextCity;
		}
		// Adds the edge between the last city and the first city
		hullEdges.add(new CityPair(curCity, P0));

		return hullEdges;
	}

	/**
	 * Helper method for Graham's algorithm: tests whether adding the next city
	 * to the stack will cause a "right turn" based on the line between the top
	 * two stack entries (uses cross product)
	 * @param pI the new city to test
	 * @param stack the stack of cities to get the first two from
	 */
	public boolean isLeftTurn(City pI, LinkedList<City> stack)
	{
		int x1 = stack.get(0).getX(), x2 = stack.get(1).getX();
		int y1 = stack.get(0).getY(), y2 = stack.get(1).getY();
		int x3 = pI.getX(), y3 = pI.getY();
		long crossProduct = (x2 - x1)*(y3 - y1) - (y2 - y1)*(x3 - x1);
		if (crossProduct < 0) return true;
		else return false;
	}

	/**
	 * Calculates the cities' canvas positions (reversing the y coordinate) and
	 * draws them at their canvas positions
	 * @param n The number of cities
	 */
	public void displayCities(int n)
	{
		canvas.clear();
		if (n < 3)
		{
			canvas.drawString("You must enter at least 3 cities!", 100, 100);
		}
		else
		{
			cities = CitiesGenerator.MakeCities(n, xSize, ySize);
			for (City c : cities)
			{
				c.setCanvasX(c.getX());
				c.setCanvasY(ySize - c.getY());
				drawCity(c, Color.BLACK);
			}
			canvas.display();
		}
	}

	/**
	 * Helper method for drawing cities
	 * @param c The city to draw
	 * @param Color the color to draw the city in
	 */
	private void drawCity(City c, Color color)
	{
		canvas.setForeground(color);
		canvas.fillOval(c.getCanvasX(), c.getCanvasY(), 
				OVAL_DIAM, OVAL_DIAM, false);
	}

	/** Helper method for adding buttons */
	private JButton addButton(JPanel panel, String name)
	{
		JButton button = new JButton(name);
		button.addActionListener(this);
		panel.add(button);
		return button;
	}

}
