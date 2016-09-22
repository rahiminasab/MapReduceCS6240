package weatherdata.sequential;

import weatherdata.TMAXCalaculator;

/*
 * sequential way of calculating the TMAX averages.
 */
public class SEQ_TMAXCalculator extends TMAXCalaculator {
	
	public SEQ_TMAXCalculator(String inputPath) {
		super(inputPath);
		name = "SEQUENTIAL";
	}
	
	//Here we just compute averages from the first record to the last, sequentially.
	@Override
	protected void calc(boolean isDelayed) {
		SEQ_TMAXCalculator.clearMap();
		SEQ_TMAXCalculator.computeAverages(0, SEQ_TMAXCalculator.numberOfRecords, SEQ_TMAXCalculator.aveMap, isDelayed);
	}

}
