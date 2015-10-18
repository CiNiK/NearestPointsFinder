## NearestPointsFinder
### Description
 Find radius and count of nearest neighbors to each point in the set of points. Point is represented by two integers coordinates
between -99 000 and 99 000. Nearest neighbors of given point are considered points that lies within doubled radius, where radius 
is a distance between given point and nearest point.

 Search is based on 2d-tree, which is a generalization of a BST to two-dimensional keys. 
 
 [More about BST] (https://en.wikipedia.org/wiki/Binary_search_tree)
 
 [More about k-d tree] (https://en.wikipedia.org/wiki/K-d_tree)
 
### Compilation
	javac NeighborsFinder.java
 
### Usage
- to input from keyboard
  
	`java NeighborsFinder`
  
- to input from file
	
	`java NeighborsFinder filename`
	
### Input file structure
- each point begin from new line;
- coordinates split by whitespace;
		
	#### Example
	```
	//start of the file
	10 10
	14 16
	18 200
	//end of the file
	```
