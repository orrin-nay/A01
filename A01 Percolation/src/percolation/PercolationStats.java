package percolation;

import java.util.Random;
import java.util.stream.DoubleStream;

public class PercolationStats {
	Random randomGenorator;
	double[] arrayOfFractionOfOpenSites; // this is all the x sub t's in the estimation of percolation threshold equation
	double numberOfAvailableSpots;
	int T;
	int N;
	public PercolationStats(int N, int T) // perform T independent experiments on an N­by­N grid
	{
		this.N = N;
		this.randomGenorator = new Random();
		this.T = T;
		this.arrayOfFractionOfOpenSites = new double[T]; // this is all the x sub t's in the estimation of percolation threshold equation
		this.numberOfAvailableSpots = N*N;
		for(int i = 0; i < arrayOfFractionOfOpenSites.length; i++){
			Percolation percolation = new Percolation(N);
			double numberOfOpen = 0;
			while(true){
				if(percolation.percolates()) break;
				while(true){
					int randomI = getRandomIOrJ(N);
					int randomJ = getRandomIOrJ(N);
					if (percolation.isOpen(randomI, randomJ)) continue;
					percolation.open(randomI, randomJ);
					numberOfOpen++;
					break;
				}
			}
			arrayOfFractionOfOpenSites[i] = numberOfOpen/numberOfAvailableSpots;
		}
		System.out.println(toString());
		System.out.println("Mean = " + mean());
		System.out.println("Stddev = " + stddev());
		System.out.println("Confidence Low = " + confidenceLow());
		System.out.println("Confidence High = " + confidenceHigh());
	}
	int getRandomIOrJ(int max)//max is exclusive
	{
		return randomGenorator.nextInt(max);
	}
	public double mean() // sample mean of percolation threshold
	{
		double sumation = DoubleStream.of(arrayOfFractionOfOpenSites).sum();
		return sumation/this.T;
	}
	public double stddev() // sample standard deviation of percolation threshold
	{
		double thresh = mean();
		double topPartOfEquation = 0;
		for(int i = 0; i< arrayOfFractionOfOpenSites.length; i ++)
			topPartOfEquation += Math.pow((arrayOfFractionOfOpenSites[i] - thresh), 2);
		double rightHandSide = topPartOfEquation/(this.T - 1);
		double isolateStddev = Math.sqrt(rightHandSide);
		return isolateStddev;
	}
	public double confidenceLow() // low endpoint of 95% confidence interval
	{
		return mean() -1.96 * stddev()/Math.sqrt(this.T);
	}
	public double confidenceHigh() // high endpoint of 95% confidence interval
	{
		return mean() + 1.96 * stddev()/Math.sqrt(this.T);
	}
	public String toString() {
		return "Percolation Stats for a grid " + this.N + 
				"X" + this.N + " at " + this.T + " itterations.";
	}
	public static void main(String args[]) {
		new PercolationStats(200, 100000);
	}
}
