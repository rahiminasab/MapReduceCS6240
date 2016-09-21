package hw1;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

import hw1.misc.AverageInfo;
import hw1.misc.RunTimeStat;
import hw1.misc.TmaxRecord;


public abstract class TMAXCalaculator {
	
    private static final String TMAX_ID = "TMAX";
	
	protected static ArrayList<String> dataLines;
	protected static int numberOfRecords = 0;
	public static HashMap<String, AverageInfo> aveMap = new HashMap<String, AverageInfo>();
	
	protected String name;
	
	public static void clearMap() {
		aveMap = new HashMap<String, AverageInfo>();
	}
	
	public HashMap<String, AverageInfo> getAveMap() {
		return aveMap;
	}
	
	protected TMAXCalaculator(String inputPath) {
		dataLines = readInput(inputPath);
		numberOfRecords = dataLines.size();
	}
	
	private ArrayList<String> readInput(String path) {
		ArrayList<String> lines = new ArrayList<String>();
		File f = new File(path);
		try {
			Scanner inputStream = new Scanner(f);
			while(inputStream.hasNextLine()) {
				lines.add(inputStream.nextLine());
			}
			inputStream.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		return lines;
	}
	
	protected static TmaxRecord parseLine(String line) {
		String[] info = line.split(",");
		String station = info[0];
		String measure = info[2];
		double reading = 0;
		try { 
	        reading = Double.parseDouble(info[3]); 
	    } catch(NumberFormatException e) { 
	        reading = 0;
	    } catch(NullPointerException e) {
	        reading = 0;
	    }
		if(measure.equals(TMAX_ID)) {
			return new TmaxRecord(station, reading);
		}
		return null;
	}
	
	public static void computeAverages(int from, int to, HashMap<String, AverageInfo> map, boolean withDelays) {
		for(int i = from; i < to; i++) {
			TmaxRecord record = parseLine(dataLines.get(i));
			if(record != null)
				insertRecord(record, map, withDelays);	
		}
	}
	
	protected static void insertRecord(TmaxRecord record, HashMap<String, AverageInfo> map, boolean withDelays) {
		AverageInfo info = map.get(record.getStation());
		if(info != null)
			info.updateAverage(record.getReading(), withDelays);
		else {
		    info = new AverageInfo();
			info.updateAverage(record.getReading(), withDelays);
			map.put(record.getStation(), info);
		}
	}
	
	protected abstract void calc(boolean withDelays) throws InterruptedException;
	
	protected static void runJob(TMAXCalaculator obj, boolean withDelays) throws InterruptedException {
		String prefix = (withDelays)? "with" : "without";
		System.out.println(prefix+" delay version:");
		long[] runtimes = new long[10];
		for(int i = 0; i < runtimes.length; i++) {
			long start = System.currentTimeMillis();
			obj.calc(withDelays);
			long end = System.currentTimeMillis(); 
			runtimes[i] = end-start;
		}
		RunTimeStat stats = new RunTimeStat(runtimes);
		stats.printResultsFor(obj.name);
		System.out.println("--------------------------------------------------\n");
		
	}
}
