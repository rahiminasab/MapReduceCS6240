package hw1.misc;

public class RunTimeStat {
	private long max;
	private long min;
	private double average;
	
	public long getMin() {
		return min;
	}
	public long getMax() {
		return max;
	}
	public double getAverage() {
		return average;
	}
	public RunTimeStat(long[] runTimes) {
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
	}
	public void printResultsFor(String method) {
		System.out.println("running time stats for "+ method+" case:");
		System.out.println("-----------------------------");
		System.out.println("max: "+ getMax());
		System.out.println("min: "+ getMin());
		System.out.println("ave: "+ getAverage());
		
	}
}
