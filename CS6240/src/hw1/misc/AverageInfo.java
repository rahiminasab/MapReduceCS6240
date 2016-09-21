package hw1.misc;

public class AverageInfo  {
	
	private static final int N = 17;
	private double average;
	private int numOfInstances;

	public AverageInfo() {
		average = 0;
		numOfInstances = 0;
	}
	
	public double getAverage() {
		return average;
	}
	
	public void updateAverage(double newInstance, boolean withDelays) {
		if(withDelays)
			fibonacciBarrier(N);
		numOfInstances++;
		average += (newInstance - average)/(numOfInstances);
	}
	
	public synchronized void synchronizedUpdateAverage(double newInstance, boolean withDelays) {
		if(withDelays)
			fibonacciBarrier(N);
		numOfInstances++;
		average += (newInstance - average)/(numOfInstances);
	}
	
	public void mergeAverage(AverageInfo other) {
		average = (average*numOfInstances+other.average*other.numOfInstances)/(numOfInstances+other.numOfInstances);
		numOfInstances += other.numOfInstances;
	}
	
	private int fibonacciBarrier(int n) {
		if(n == 1)
			return 1;
		if(n == 2)
			return 1;
		return fibonacciBarrier(n-1)+fibonacciBarrier(n-2);
	}
}
