package hw1.parallels;

import java.util.concurrent.ConcurrentHashMap;

import hw1.misc.AverageInfo;
import hw1.misc.TmaxRecord;

public class FINE_TMAX_Calculator extends ParallelTMAXCalculator {
	
	public static ConcurrentHashMap<String, AverageInfo> aveMap = new ConcurrentHashMap<String, AverageInfo>();;
	protected FINE_TMAX_Calculator(String inputPath) {
		super(inputPath);
		name = "FINE-LOCK";
	}

	public static void computeAverages(int from, int to, ConcurrentHashMap<String, AverageInfo> map, boolean withDelays) {
		for(int i = from; i < to; i++) {
			TmaxRecord record = parseLine(dataLines.get(i));
			if(record != null)
				insertRecord(record, map, withDelays);	
		}
	}
	
	protected static void insertRecord(TmaxRecord record, ConcurrentHashMap<String, AverageInfo> map, boolean withDelays) {
		AverageInfo info = map.get(record.getStation());
		if(info != null)
			info.synchronizedUpdateAverage(record.getReading(), withDelays);
		else {
		    info = new AverageInfo();
			info.synchronizedUpdateAverage(record.getReading(), withDelays);
			map.put(record.getStation(), info);
		}
	}
	
	public static void main(String[] args) throws InterruptedException {
		FINE_TMAX_Calculator obj = new FINE_TMAX_Calculator("/home/ehsan/Desktop/MapReduce/HW1/input/1877.csv");
		runJob(obj, false);
		runJob(obj, true);
		//System.out.println(FINE_TMAX_Calculator.fineMap.get("CA007025280").getAverage());
	}

}