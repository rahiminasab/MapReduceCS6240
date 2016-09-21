package hw1.parallels;

import java.util.ArrayList;
import java.util.HashMap;

import hw1.TMAXCalaculator;
import hw1.misc.AverageInfo;

public class ParallelTMAXCalculator extends TMAXCalaculator {
	
	private static final int MAX_NUM_THREADS = 4;
	
	protected ParallelTMAXCalculator(String inputPath) {
		super(inputPath);
	}

	@Override
	protected void calc(boolean withDelays) throws InterruptedException {
		
		TMAXCalaculator.clearMap();
		
		ArrayList<WorkThread> pool = new ArrayList<WorkThread>();
		
		int numOfRecords = TMAXCalaculator.numberOfRecords;
		int recordsPerWorker = numOfRecords/MAX_NUM_THREADS;
		
		for(int i = 0; i < MAX_NUM_THREADS; i++) {
			int from = i*recordsPerWorker;
			int to = (i+1)*recordsPerWorker + ((i==MAX_NUM_THREADS-1)?numOfRecords%MAX_NUM_THREADS : 0);
			pool.add(new WorkThread(from, to, this, withDelays));
		}
		for(int i = 0; i < MAX_NUM_THREADS; ++i) {
			pool.get(i).start();
        }
        
        for(int i = 0; i < MAX_NUM_THREADS; ++i) {
            pool.get(i).join();
        }
        
        if(this instanceof NOSHARE_TMAX_Calculator) {
        	mergeMaps(pool, TMAXCalaculator.aveMap);
        }

	}
	
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

class WorkThread extends Thread {
	int from = -1;
	int to = -1;
	ParallelTMAXCalculator calculator;
	HashMap<String, AverageInfo> aveMap;
	boolean withDelays;
	
	public WorkThread(int from, int to, ParallelTMAXCalculator obj, boolean withDelays) {
		this.from = from;
		this.to = to;
		this.calculator = obj;
		aveMap = new HashMap<String, AverageInfo>();
		this.withDelays = withDelays;
	}
	
	@Override
	public void run() {
		if(calculator instanceof NOLOCK_TMAX_Calculator) {
			NOLOCK_TMAX_Calculator.computeAverages(from, to, TMAXCalaculator.aveMap, withDelays);
			return;
		} else if(calculator instanceof COARSE_TMAX_Calculator) {
			COARSE_TMAX_Calculator.computeAverages(from, to, TMAXCalaculator.aveMap, withDelays);
			return;
		} else if(calculator instanceof FINE_TMAX_Calculator) {
			FINE_TMAX_Calculator.computeAverages(from, to, FINE_TMAX_Calculator.aveMap, withDelays);
			return;
		} else if(calculator instanceof NOSHARE_TMAX_Calculator) {
			NOSHARE_TMAX_Calculator.computeAverages(from, to, aveMap, withDelays);
			return;
		}
		return;
	}
}