package weatherdata.parallels;

import java.util.ArrayList;
import java.util.HashMap;

import weatherdata.TMAXCalaculator;
import weatherdata.helpers.AverageInfo;

/*
 * All TMAX calculators which have a parallel approach should be a child of this super class.
 */
public class ParallelTMAXCalculator extends TMAXCalaculator {
	
	private static final int MAX_NUM_THREADS = 4;
	
	protected ParallelTMAXCalculator() {

	}
	
	// the records are split equally between the threads to compute averages.
	// Children will tell the WorkThreads the way to compute averages.
	@Override
	public void calc(boolean isDelayed) throws InterruptedException {
		
		TMAXCalaculator.clearMap();
		
		ArrayList<WorkThread> pool = new ArrayList<WorkThread>();
		
		int numOfRecords = TMAXCalaculator.numberOfRecords;
		int recordsPerWorker = numOfRecords/MAX_NUM_THREADS;
		
		for(int i = 0; i < MAX_NUM_THREADS; i++) {
			int from = i*recordsPerWorker;
			int to = (i+1)*recordsPerWorker + ((i==MAX_NUM_THREADS-1)?numOfRecords%MAX_NUM_THREADS : 0);
			pool.add(new WorkThread(from, to, this, isDelayed));
		}
		for(int i = 0; i < MAX_NUM_THREADS; ++i) {
			pool.get(i).start();
        }
        
        for(int i = 0; i < MAX_NUM_THREADS; ++i) {
            pool.get(i).join();
        }
        
        //If this is an instance of parallel NOSHARE calculator, then we should merge the maps.
        if(this instanceof NOSHARE_TMAXCalculator) {
        	mergeMaps(pool, TMAXCalaculator.aveMap);
        }

	}
	
	/*
	 * merges the maps of the working threads in the given map.
	 */
	private void mergeMaps(ArrayList<WorkThread> pool, HashMap<String, AverageInfo> map) {
		for(WorkThread t : pool) {
			HashMap<String, AverageInfo> subMap = t.aveMap;
			for(String station : subMap.keySet()) {
				if(map.containsKey(station)) {
					AverageInfo info = map.get(station);
					info.mergeAverage(subMap.get(station));
				} else {
					map.put(station, subMap.get(station));
				}
			}
		}
	}

}

/*
 * The only thread class for all parallel calculators.
 */
class WorkThread extends Thread {
	int from = -1;
	int to = -1;
	ParallelTMAXCalculator calculator;
	HashMap<String, AverageInfo> aveMap; // to be used in NOSHARE case.
	boolean isDelayed;
	
	public WorkThread(int from, int to, ParallelTMAXCalculator obj, boolean isDelayed) {
		this.from = from;
		this.to = to;
		this.calculator = obj;
		aveMap = new HashMap<String, AverageInfo>();
		this.isDelayed = isDelayed;
	}
	
	/*
	 * Each parallel calculator has its own compute averages function.
	 * NOLOCK:  Just uses its parent function and there is no lock on anything there.
	 * COARSE:  uses its own compute average function which has a lock on the whole TMAXCalaculator.aveMap.
	 * FINE:    Has its own map (i.e. FINE_TMAX_Calculator.aveMap) and its own compute averages function, 
	 *          in which we use the synchronizedAverageUpdate to have a lock only on value objects.
	 * NOSHARE: Uses this WorkThread map to store the intermediate results. It uses also its parent 
	 *          compute average function. 
	 */
	@Override
	public void run() {
		if(calculator instanceof NOLOCK_TMAXCalculator) {
			NOLOCK_TMAXCalculator.computeAverages(from, to, TMAXCalaculator.aveMap, isDelayed);
			return;
		} else if(calculator instanceof COARSE_TMAXCalculator) {
			COARSE_TMAXCalculator.computeAverages(from, to, TMAXCalaculator.aveMap, isDelayed);
			return;
		} else if(calculator instanceof FINE_TMAXCalculator) {
			FINE_TMAXCalculator.computeAverages(from, to, FINE_TMAXCalculator.aveMap, isDelayed);
			return;
		} else if(calculator instanceof NOSHARE_TMAXCalculator) {
			NOSHARE_TMAXCalculator.computeAverages(from, to, aveMap, isDelayed);
			return;
		}
		return;
	}
}