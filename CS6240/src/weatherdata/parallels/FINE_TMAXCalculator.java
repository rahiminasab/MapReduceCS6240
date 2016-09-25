package weatherdata.parallels;

import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;

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
	
	public FINE_TMAXCalculator() {
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
	private static void insertRecord(TmaxRecord newRecord, ConcurrentHashMap<String, AverageInfo> map, boolean isDelayed) {
		String station = newRecord.getStation();
		AverageInfo inf = map.get(station);
		if(inf == null)
			map.computeIfAbsent(station, makeAverageInfo(newRecord.getReading(),isDelayed));
		else
			inf.synchronizedUpdateAverage(newRecord.getReading(), isDelayed);
		
	}
	
	/*
	 * mapping function if the key was not present in the map
	 */
	private static Function<? super String, ? extends AverageInfo> makeAverageInfo(double reading, boolean isDelayed) {
		Function<String, AverageInfo> func = new Function<String, AverageInfo>() {

			@Override
			public AverageInfo apply(String t) {
				AverageInfo info = new AverageInfo();
				info.synchronizedUpdateAverage(reading, isDelayed);
				return info;
			}
		};
		
		return func;
	}


}