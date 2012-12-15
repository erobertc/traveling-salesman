import java.util.*;
/**
 * Class to contain methods that 
 * @author Robert Colgan (rec2111), using code modified from Data Structures 
 * and Algorithm Analysis in Java (3rd edition) by Mark Allen Weiss and from
 * http://www.cs.columbia.edu/~allen/S12/NOTES/sortplusradix.pdf
 *
 */
public class Sorter 
{
	private static final int CUTOFF = 3;
	
	public static void insertionSort(ArrayList<City> cities, int start, 
			int end) 
	{
		int j;
		for(int p = start; p <= end; p++)
		{
			City tmp = cities.get(p);
			for (j = p; j > start && tmp.compareTo(cities.get(j - 1)) < 0; j--)
			{
				cities.set(j, cities.get(j - 1));
			}
			cities.set(j, tmp);
		}
	}
	
	
	public static void quicksort(ArrayList<City> cities) 
	{
		quicksort(cities, 0, cities.size() - 1);
	}
	
	
	private static void quicksort(ArrayList<City> cities, int left, 
			int right) 
	{
		if (left + CUTOFF <= right)
		{
			City pivot = median3(cities, left, right);
			int i = left, j = right - 1;
			for ( ; ; )
			{
				while (cities.get(++i).compareTo(pivot) < 0) {}
				while (cities.get(--j).compareTo(pivot) > 0) {}
				if (i < j)
					swapReferences(cities, i, j);
				else break;
			}
			swapReferences(cities, i, right - 1); // Restore pivot
			quicksort(cities, left, i - 1); // Sort small elements
			quicksort(cities, i + 1, right); // Sort large elements
		}
		else insertionSort(cities, left, right);
	}
	
	
	private static City median3(ArrayList<City> cities, int left, int right) 
	{
		int center = (left + right) / 2;
		if (cities.get(center).compareTo(cities.get(left)) < 0)
			swapReferences(cities, left, center);
		if (cities.get(right).compareTo(cities.get(left)) < 0)
			swapReferences(cities, left, right);
		if (cities.get(right).compareTo(cities.get(center)) < 0)
			swapReferences(cities, center, right);
		
		// Place pivot at position right - 1
		swapReferences(cities, center, right - 1);
		return cities.get(right - 1);
	}
	
	
	public static void swapReferences(ArrayList<City> cities, int i, int j) 
	{
		City temp = cities.get(i);
		cities.set(i, cities.get(j));
		cities.set(j, temp);
	}

}
