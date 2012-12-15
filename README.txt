README for traveling-salesman
by Robert Colgan
erobertc92@gmail.com
rec2111@columbia.edu

This project is an implementation of a Traveling Salesman Problem solver with a
GUI, written in Java for COMS W3137 "Data Structures and Algorithms" taught by 
Peter Allen at Columbia University in spring 2012. 

This program deals with the problem of finding the shortest path that visits 
all of a set of points and returns to the start point. It allows a user to 
enter a number (greater than or equal to 3) of points representing cities, 
which it draws on the press of a button on a canvas at random x and y 
coordinates between 0 and 500. Another button computes the convex hull of the 
points using Graham's algorithm, and draws it in red. The implementation of 
Graham's algorithm uses a custom implementation of Quicksort (contained in the 
Sorter class) to sort the cities by angle. A third button uses the cheapest 
insertion metric to compute a tour of all cities and draws it in blue. In 
practice (at least, as tested for N <= 10 where N is the number of cities) 
this almost always finds the optimal tour. It displays the distance of the 
tour in the GUI below the cities. A fourth button uses brute force to 
calculate the optimal tour by finding every possible tour (N factorial tours) 
and choosing the one with the smallest total distance. It draws the tour in 
green and displays its length underneath the length of the cheapest insertion 
tour. The cheapest insertion tour button must be pressed after the convex hull 
has been calculated, otherwise the behavior of the program is undefined. 

It uses the DrawingCanvas class provided by Prof. Allen for creating the canvas
to draw cities and paths upon. In addition, the TSPgui class includes code 
modified from
http://www.cs.columbia.edu/~allen/S12/NOTES/DisplaySimpleTree.java and
http://www.cs.columbia.edu/~allen/S12/NOTES/InchwormSimulation.zip, and the 
Sorter class (a repository of static methods used for sorting) includes 
code modified from Data Structures and Algorithm Analysis in Java (3rd 
edition) by Mark Allen Weiss and from
http://www.cs.columbia.edu/~allen/S12/NOTES/sortplusradix.pdf.