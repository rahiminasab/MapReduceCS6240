package hw1.parallels;

import java.util.concurrent.ConcurrentHashMap;

import hw1.helpers.AverageInfo;
import hw1.helpers.TmaxRecord;
/*
 * Object of this class has different map for saving the results, and different computeAverages
 * function.
 */
public class FINE_TMAX_Calculator extends ParallelTMAXCalculator {
	
	/*
	 * we use concurrent map here to restrict the same time access for a same key to avoid collision scenarios.
	 */
	public static ConcurrentHashMap<String, AverageInfo> aveMap = new ConcurrentHashMap<String, AverageInfo>();
	
	public FINE_TMAX_Calculator(String inputPath) {
		super(inputPath);
		name = "FINE-LOCK";
	}

	/*
	 * same as its parent computeAverages, but it calls this insertRecord function.
	 */
	public static void computeAverages(int from, int to, ConcurrentHashMap<String, AverageInfo> map, boolean withDelays) {
		for(int i = from; i < to; i++) {
			TmaxRecord record = parseLine(dataLines.get(i));
			if(record != null)
				insertRecord(record, map, withDelays);	
		}
	}
	
	/*
	 * do the same job as its parent function, but it calls the synchronizedUpdate on the value objects to maintain a lock on them.
	 */
	private static void insertRecord(TmaxRecord record, ConcurrentHashMap<String, AverageInfo> map, boolean withDelays) {
		AverageInfo info = map.get(record.getStation());
		if(info != null)
			info.synchronizedUpdateAverage(record.getReading(), withDelays);
		else {
		    info = new AverageInfo();
			info.synchronizedUpdateAverage(record.getReading(), withDelays);
			map.put(record.getStation(), info);
		}
	}

}