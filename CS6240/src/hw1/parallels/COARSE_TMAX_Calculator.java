package hw1.parallels;

import java.util.HashMap;

import hw1.misc.AverageInfo;
import hw1.misc.TmaxRecord;

public class COARSE_TMAX_Calculator extends ParallelTMAXCalculator {
	
	COARSE_TMAX_Calculator(String inputPath) {
		super(inputPath);
		name = "COARSE-LOCK";
	}
	
	public static void computeAverages(int from, int to, HashMap<String, AverageInfo> map, boolean withDelays) {
		for(int i = from; i < to; i++) {
			TmaxRecord record = parseLine(dataLines.get(i));
			if(record != null)
				insertRecord(record, map, withDelays);	
		}
	}
	
	protected static void insertRecord(TmaxRecord record, HashMap<String, AverageInfo> map, boolean withDelays) {
		synchronized (map) {
			AverageInfo info = map.get(record.getStation());
			if(info != null)
				info.updateAverage(record.getReading(), withDelays);
			else {
			    info = new AverageInfo();
				info.updateAverage(record.getReading(), withDelays);
				map.put(record.getStation(), info);
			}
		}
		
	}
	
	public static void main(String[] args) throws InterruptedException {
		COARSE_TMAX_Calculator obj = new COARSE_TMAX_Calculator("/home/ehsan/Desktop/MapReduce/HW1/input/1877.csv");
		runJob(obj, false);
		runJob(obj, true);
		//System.out.println(TMAXCalaculator.aveMap.get("CA007025280").getAverage());
	}

}
