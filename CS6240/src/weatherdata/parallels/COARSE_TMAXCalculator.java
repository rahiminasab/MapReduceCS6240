package weatherdata.parallels;

import java.util.HashMap;

import weatherdata.helpers.AverageInfo;
import weatherdata.helpers.TmaxRecord;

/*
 * an object of this class uses the same map as its parent for storing the results, but has
 * its own computeAverages function, which holds a lock on the parent map.
 */
public class COARSE_TMAXCalculator extends ParallelTMAXCalculator {
	
	public COARSE_TMAXCalculator(String inputPath) {
		super(inputPath);
		name = "COARSE-LOCK";
	}
	
	/*
	 * same as parent computeAverges but it calls this insertRecord function.
	 */
	public static void computeAverages(int from, int to, HashMap<String, AverageInfo> map, boolean isDelayed) {
		for(int i = from; i < to; i++) {
			TmaxRecord record = parseLine(dataLines.get(i));
			if(record != null)
				insertRecord(record, map, isDelayed);	
		}
	}
	
	/*
	 * same as parent insertRecord function, but it has a lock on the whole map.
	 */
	protected static void insertRecord(TmaxRecord record, HashMap<String, AverageInfo> map, boolean isDelayed) {
		synchronized (map) {
			AverageInfo info = map.get(record.getStation());
			if(info != null)
				info.updateAverage(record.getReading(), isDelayed);
			else {
			    info = new AverageInfo();
				info.updateAverage(record.getReading(), isDelayed);
				map.put(record.getStation(), info);
			}
		}
		
	}

}
