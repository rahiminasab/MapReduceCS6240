package weatherdata;

import java.util.ArrayList;
import java.util.Scanner;

import weatherdata.helpers.RunTimeStat;
import weatherdata.parallels.COARSE_TMAXCalculator;
import weatherdata.parallels.FINE_TMAXCalculator;
import weatherdata.parallels.NOLOCK_TMAXCalculator;
import weatherdata.parallels.NOSHARE_TMAXCalculator;
import weatherdata.sequential.SEQ_TMAXCalculator;

public class Main {
	private static final String SEQUENTIAL = "-sequential";
	private static final String NOLOCK = "-nolock";
	private static final String COARSE = "-coarse";
	private static final String FINE = "-fine";
	private static final String NOSHARE = "-noshare";
	private static ArrayList<String> methods = new ArrayList<String>();
	private static final int NUMBER_OF_RUNS = 10;
	
	static {
		methods.add(SEQUENTIAL);
		methods.add(NOLOCK);
		methods.add(COARSE);
		methods.add(FINE);
		methods.add(NOSHARE);
	}
	
	
	/*
	 * runs the clac function for NUMBER_OF_RUNS and stores the running times in an array,
	 * then it passes that array to RunTimeStat class for computing min/max/average running times
	 * and then prints them.
	 */
	public static void runJob(TMAXCalaculator obj, boolean isDelayed) throws InterruptedException {
		String prefix = (isDelayed)? "with" : "without";
		long[] runtimes = new long[NUMBER_OF_RUNS];
		for(int i = 0; i < runtimes.length; i++) {
			long start = System.currentTimeMillis();
			obj.calc(isDelayed);
			long end = System.currentTimeMillis(); 
			runtimes[i] = end-start;
		}
		RunTimeStat stats = new RunTimeStat(runtimes, prefix);
		stats.printResultsFor(obj.name);
		System.out.println("--------------------------------------------------\n");
		
	}
	
	/*
	 * the main method of program which needs at least two arguments,
	 * the first is the path to weather data.
	 * second is a key for the type of computation as described in the printHelp, it should be one of:
	 * 	-sequential
	 *  -nolock
	 *  -coarse
	 *  -fine
	 *  -noshare
	 * third is optional which asks whether we should have delays with Fib(17).
	 *  
	 */
	@SuppressWarnings("resource")
	public static void main(String[] args) throws InterruptedException {
		String path = args[0];
		TMAXCalaculator.readInput(path);
		while(true) {
			String method = "";
			
			Scanner s = new Scanner(System.in);
			while(true) {
				printHelp();
				
				method = s.next(); 
				if(method.equals("q")) System.exit(0);
				if(!methods.contains(method)) {
					System.out.println("\nInvalid key was entered!\n");
					continue;
				}
				break;
				
			}
			
			boolean isDelayed = false;
			while(true) {
				System.out.print("\nDo you want to enforce delays in the updates? (Y/n) ");
				//Scanner s = new Scanner(System.in);
				String ans = s.next().toLowerCase();
				isDelayed = (ans.equals("y") || ans.equals("yes"))? true : false;
				break;
			}
			//s.close();
			
			TMAXCalaculator calculator = null;
			
			switch (method) {
			case SEQUENTIAL:
				calculator = new SEQ_TMAXCalculator();
				break;
			case NOLOCK:
				calculator = new NOLOCK_TMAXCalculator();
				break;
			
			case COARSE:
				calculator = new COARSE_TMAXCalculator();
				break;
			
			case FINE:
				calculator = new FINE_TMAXCalculator();
				break;
			
			case NOSHARE:
				calculator = new NOSHARE_TMAXCalculator();
				break;
	
			default:
				printHelp();
				return;
			}
			runJob(calculator, isDelayed);
			//System.out.println(TMAXCalaculator.aveMap.get("CA007025280").getAverage());
		}
	}
	
	public static void printHelp() {
		System.out.println("\nPlease choose the method of computation (type one of the followings) or type q to quit:\n");
		System.out.println("\t -sequential");
		System.out.println("\t -nolock");
		System.out.println("\t -coarse");
		System.out.println("\t -fine");
		System.out.println("\t -noshare\n");
		
	}
}
