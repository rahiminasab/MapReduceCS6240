package hw1.helpers;

/*
 * Objects of this class hold the average TMAX for an station.
 */
public class AverageInfo  {
	
	private static final int N = 17; //If delayed, we compute Fib(N)
	
	private double average; //Average TMAX for an station so far
	private int numOfInstances; // number of instances for an station so far

	public AverageInfo() {
		average = 0;
		numOfInstances = 0;
	}
	
	public double getAverage() {
		return average;
	}
	
	/*
	 * updating the average after reading a new tmax for an station which is paired by this object.
	 */
	public void updateAverage(double newInstance, boolean withDelays) {
		if(withDelays)
			fibonacciBarrier(N);
		numOfInstances++;
		average += (newInstance - average)/(numOfInstances);
	}
	
	/*
	 * a synchronized updating procedure which is used in fine lock
	 * an alternative way is to lock this object in the following function, but I prefer it to be
	 * in this way.
	 */
	public synchronized void synchronizedUpdateAverage(double newInstance, boolean withDelays) {
		if(withDelays)
			fibonacciBarrier(N);
		numOfInstances++;
		average += (newInstance - average)/(numOfInstances);
	}
	
	/*
	 * if we want to merge a value of AverageInfo object into this object for a same key.
	 */
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
