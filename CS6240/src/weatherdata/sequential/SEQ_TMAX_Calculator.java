package hw1.sequential;

import hw1.TMAXCalaculator;

/*
 * sequential way of calculating the TMAX averages.
 */
public class SEQ_TMAX_Calculator extends TMAXCalaculator {
	
	public SEQ_TMAX_Calculator(String inputPath) {
		super(inputPath);
		name = "SEQUENTIAL";
	}
	
	//Here we just compute averages from the firs record to the last, sequentially.
	@Override
	protected void calc(boolean withDelays) {
		SEQ_TMAX_Calculator.clearMap();
		SEQ_TMAX_Calculator.computeAverages(0, SEQ_TMAX_Calculator.numberOfRecords, SEQ_TMAX_Calculator.aveMap, withDelays);
	}

}
