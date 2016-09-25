package weatherdata;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

import weatherdata.helpers.AverageInfo;
import weatherdata.helpers.TmaxRecord;

/*
 * Each of the 5 version of calculating the averages inherits from this abstract class.  
 * All children should implement the clac method.
 */
public abstract class TMAXCalaculator {
	
    private static final String TMAX_ID = "TMAX";
	
	protected static ArrayList<String> dataLines; //stores the lines of weather data file.
	protected static int numberOfRecords = 0; //number of records in weather data file.
	
	/*
	 * a Map structure which holds stations as keys, and objects of AverageInfo as values.
	 * The average TMAX for a station key is always saved in an AverageInfo value object.
	 */
	public static HashMap<String, AverageInfo> aveMap = new HashMap<String, AverageInfo>();
	
	/*
	 * name of this calculator, which can be one of the followings:
	 * - SEQUENTIAL
	 * - NO-LOCK
	 * - COARSE-LOCK
	 * - FINE_LOCK
	 * - NO_SHARE
	 */
	public String name;
	
	public static void clearMap() {
		aveMap = new HashMap<String, AverageInfo>();
	}
	
	public HashMap<String, AverageInfo> getAveMap() {
		return aveMap;
	}
	
	protected TMAXCalaculator() {
		
	}
	
	/*
	 * stores all the records as separate lines in dataLines.
	 */
	public static void readInput(String path) {
		System.out.println("\nReading the input file...");
		ArrayList<String> lines = null;
		File f = new File(path);
		try {
			lines = new ArrayList<String>();
			Scanner inputStream = new Scanner(f);
			while(inputStream.hasNextLine()) {
				lines.add(inputStream.nextLine());
			}
			inputStream.close();
		} catch (FileNotFoundException e) {
			System.err.println("The path to weather data is invalid!");
			System.exit(0);
		}
		dataLines =  lines;
		System.out.println("\nReading the input file COMPLETE\n");
		numberOfRecords = dataLines.size();
	}
	
	/*
	 * if a record has TMAX_ID in the third column, then we return a TmaxRecord object which
	 * stores its station name and the reading for the tmax of that station.
	 */
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
	
	/*
	 * This method processes the dataLines from index "from" to index "to" and saves the 
	 * results in the given map.
	 * the forth argument is a boolean which specifies whether we should enforce delays with Fib(17) or not.
	 */
	public static void computeAverages(int from, int to, HashMap<String, AverageInfo> map, boolean isDelayed) {
		for(int i = from; i < to; i++) {
			TmaxRecord record = parseLine(dataLines.get(i));
			if(record != null)
				insertRecord(record, map, isDelayed);	
		}
	}
	
	/*
	 * inserts a TmaxRecord to the given map where the third argument specifies whether we have dleays with Fib(17) or not.
	 */
	protected static void insertRecord(TmaxRecord record, HashMap<String, AverageInfo> map, boolean isDelayed) {
		AverageInfo info = map.get(record.getStation());
		if(info != null)
			info.updateAverage(record.getReading(), isDelayed);
		else {
		    info = new AverageInfo();
			info.updateAverage(record.getReading(), isDelayed);
			map.put(record.getStation(), info);
		}
	}
	
	/*
	 * the main command to start the calculations is this function.
	 * children of this class may have different approaches for average computation (ex. sequential/parallel).
	 * That difference is reflected by this method.
	 */
	public abstract void calc(boolean isDelayed) throws InterruptedException;
	
	
}
