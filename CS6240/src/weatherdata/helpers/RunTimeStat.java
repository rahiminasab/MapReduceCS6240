package weatherdata.helpers;

/*
 * given an array of running times, this class will compute what are min/max/average running times.
 */
public class RunTimeStat {
	private long max;
	private long min;
	private double average;
	
	String delayPrefix;
	
	public long getMin() {
		return min;
	}
	public long getMax() {
		return max;
	}
	public double getAverage() {
		return average;
	}
	public RunTimeStat(long[] runTimes, String prefix) {
		long sum = 0;
		long max = Long.MIN_VALUE;
		long min = Long.MAX_VALUE;
		for(int i = 0; i < runTimes.length; i++) {
			sum+=runTimes[i];
			if(runTimes[i] > max)
				max = runTimes[i];
			if(runTimes[i] < min) 
				min = runTimes[i];
		}
		this.max = max;
		this.min = min;
		this.average = sum/(double)runTimes.length;
		
		delayPrefix = prefix;
	}
	public void printResultsFor(String method) {
		System.out.println("\n\nrunning time stats for "+ method+" case "+delayPrefix+" delays:");
		System.out.println("-----------------------------");
		System.out.println("max: "+ getMax());
		System.out.println("min: "+ getMin());
		System.out.println("ave: "+ getAverage());
		
	}
}
