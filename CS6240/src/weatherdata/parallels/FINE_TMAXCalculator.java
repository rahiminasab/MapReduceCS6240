package weatherdata.parallels;

import java.util.concurrent.ConcurrentHashMap;

import weatherdata.helpers.AverageInfo;
import weatherdata.helpers.TmaxRecord;
/*
 * Object of this class has different map for saving the results, and different computeAverages
 * function.
 */
public class FINE_TMAXCalculator extends ParallelTMAXCalculator {
	
	/*
	 * we use concurrent map here to restrict the same time access for a same key to avoid collision scenarios.
	 */
	public static ConcurrentHashMap<String, AverageInfo> aveMap = new ConcurrentHashMap<String, AverageInfo>();
	
	public FINE_TMAXCalculator(String inputPath) {
		super(inputPath);
		name = "FINE-LOCK";
	}

	/*
	 * same as its parent computeAverages, but it calls this insertRecord function.
	 */
	public static void computeAverages(int from, int to, ConcurrentHashMap<String, AverageInfo> map, boolean isDelayed) {
		for(int i = from; i < to; i++) {
			TmaxRecord record = parseLine(dataLines.get(i));
			if(record != null)
				insertRecord(record, map, isDelayed);	
		}
	}
	
	/*
	 * do the same job as its parent function, but it calls the synchronizedUpdate on the value objects to maintain a lock on them.
	 */
	private static void insertRecord(TmaxRecord record, ConcurrentHashMap<String, AverageInfo> map, boolean isDelayed) {
		AverageInfo info = map.get(record.getStation());
		if(info != null)
			info.synchronizedUpdateAverage(record.getReading(), isDelayed);
		else {
		    info = new AverageInfo();
			info.synchronizedUpdateAverage(record.getReading(), isDelayed);
			map.put(record.getStation(), info);
		}
	}

}