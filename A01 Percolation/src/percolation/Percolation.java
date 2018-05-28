//Written By
//Alex Perry
//Orrin Naylor

package percolation;

import edu.princeton.cs.algs4.WeightedQuickUnionUF;

//This code greates a grid that starts out Blocked (represented by a zero.) 
//This code figures out if the top of the grid is connected to the bottom of the grid by open blocks.
//Open blocks are block in the grid that equal 1 or are open they are not full by default.
//If an open block has an open path to the blocks touching the top grid it is considered full. 

public class Percolation {
	private WeightedQuickUnionUF perc;
	private int[] grid; //this contains the entire grid, and the values of each block.
	private int rowLength; //this is the length of a row of the grid. This also happens to be the length of a column, only because this code only creates grids of equal length and hight.  
	private int singleTopBlock; //this is the position in the array of the single top block. (second to last in the array).
	private int singleBottomBlock; //this is the position in the array of the single  bottom block (very last item in the array).
	private int openBlocks;
	private int gridSize;
	public Percolation(int N) { // create NbyN grid, with all sites blocked
		this.rowLength = N;
		this.gridSize = N*N +2;
		this.grid = new int[gridSize]; //creates NbyN grid. Extra 2 for top of the grid and bottom of the grid. This way the top and bottom row has something that connect all of them together when open 
			
		this.singleTopBlock = N*N + 1;
		this.singleBottomBlock = N*N + 2;
		perc = new WeightedQuickUnionUF(N*N + 3); //for some reason this need an extra element. 
	}
	
	public void open (int i, int j) { // open site (row i, column j) if it is not open already
		int indexValue = this.grid[xyToArray(i,j)];
		if (indexValue == 0) {
			this.grid[xyToArray(i,j)] = 1;
			connectOpenNeibors(i,j);
			openBlocks++;
		}
	}
	public boolean isOpen(int i, int j) { // is site (row i, column j) open?
		int indexValue = this.grid[xyToArray(i,j)];
		if (indexValue != 0) {
			return true;
		}
		else {
			return false;
		}

		
	}
	public boolean isFull(int i, int j) { // is site (row i, column j) full?
			if (perc.connected(this.singleTopBlock, xyToArray(i,j))){
			return true;
			}
			else{
			return false;
			}
		
	}
	public boolean percolates() { // does the system percolate?
		for (int k = 0; k < rowLength; k++) //fixes backwash
		{
			
			if (perc.connected(this.singleTopBlock, xyToArray(rowLength-1,rowLength-k)))
			{
				perc.union(this.singleBottomBlock, xyToArray(rowLength-1,rowLength-k));
			}
		}
		if (perc.connected(this.singleTopBlock, this.singleBottomBlock))
		{
			return true;
		}
		return false;
		
	}
	private int xyToArray(int i, int j) { //converts the x and y to a array index.  
		//We can do this by multiplying the x value by the rowLength. Then adding the y value to that.
		//This way we have the a grid that is in a single array.
			
		return (rowLength * i) + j;
		
	}
	public int numberOfOpenSites() { //return the number of openBlocks. (Required for the PercolationVisualizer to work.)
		// TODO Auto-generated method stub
		//return null;
		return openBlocks;
	}
	private void connectOpenNeibors(int i, int j)
	{
		int arrayIndex = xyToArray(i,j);
		//Check Top row
		if (arrayIndex <= rowLength)
		{
			perc.union(this.singleTopBlock, arrayIndex);
		}
		//Top
		if (i - 1 >= 0) //Check grid above. Checks to see if above grid exists
		{
			if (isOpen(i - 1,j))
			{
				perc.union(xyToArray(i - 1,j), arrayIndex);
			}
		}
		//Bottom
		if (i + 1 < this.rowLength) //Checks grid below. Checks to see if below grid exists
		{
			if (isOpen(i + 1,j))
			{
				perc.union(xyToArray(i + 1,j), arrayIndex);
			}
		}
		//Right
		if (j + 1 < this.rowLength)
		{
			if (isOpen(i,j + 1))
			{
				perc.union(xyToArray(i,j + 1), arrayIndex);
			}
		}
		//Left
		if (j - 1 >= 0)
		{
			if (isOpen(i,j - 1))
			{
				perc.union(xyToArray(i,j - 1), arrayIndex);
			}
		}
		
		//Check if full
		if (perc.connected(this.singleTopBlock, arrayIndex))
		{
			grid[arrayIndex] = 2;
		}
	}
}
