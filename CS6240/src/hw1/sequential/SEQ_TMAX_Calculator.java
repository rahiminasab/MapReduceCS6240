package hw1.sequential;

import hw1.TMAXCalaculator;

public class SEQ_TMAX_Calculator extends TMAXCalaculator {
	
	SEQ_TMAX_Calculator(String inputPath) {
		super(inputPath);
		name = "SEQUENTIAL";
	}
	
	@Override
	protected void calc(boolean withDelays) {
		SEQ_TMAX_Calculator.clearMap();
		SEQ_TMAX_Calculator.computeAverages(0, SEQ_TMAX_Calculator.numberOfRecords, SEQ_TMAX_Calculator.aveMap, withDelays);
	}
	
	public static void main(String[] args) throws InterruptedException {
		SEQ_TMAX_Calculator obj = new SEQ_TMAX_Calculator("/home/ehsan/Desktop/MapReduce/HW1/input/1912.csv");
		runJob(obj, false);
		runJob(obj, true);
		//System.out.println(TMAXCalaculator.aveMap.get("CA007025280").getAverage());
	}

}
