package weatherdata;

import weatherdata.helpers.RunTimeStat;
import weatherdata.parallels.COARSE_TMAXCalculator;
import weatherdata.parallels.FINE_TMAXCalculator;
import weatherdata.parallels.NOLOCK_TMAXCalculator;
import weatherdata.parallels.NOSHARE_TMAXCalculator;
import weatherdata.sequential.SEQ_TMAXCalculator;

public class TestRun {
	private static final String SEQUENTIAL = "-sequential";
	private static final String NOLOCK = "-nolock";
	private static final String COARSE = "-coarse";
	private static final String FINE = "-fine";
	private static final String NOSHARE = "-noshare";
	private static final String DELAYED = "-d";
	private static final int NUMBER_OF_RUNS = 10;
	
	/*
	 * runs the clac function for NUMBER_OF_RUNS and stores the running times in an array,
	 * then it passes that array to RunTimeStat class for computing min/max/average running times
	 * and then prints them.
	 */
	public static void runJob(TMAXCalaculator obj, boolean isDelayed) throws InterruptedException {
		String prefix = (isDelayed)? "with" : "without";
		System.out.println(prefix+" delay version:");
		long[] runtimes = new long[NUMBER_OF_RUNS];
		for(int i = 0; i < runtimes.length; i++) {
			long start = System.currentTimeMillis();
			obj.calc(isDelayed);
			long end = System.currentTimeMillis(); 
			runtimes[i] = end-start;
		}
		RunTimeStat stats = new RunTimeStat(runtimes);
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
	 * third is optional which if present specifies we should have delays with Fib(17).
	 *  -d
	 */
	public static void main(String[] args) throws InterruptedException {
		if(args.length < 2) {
			printHelp();
			return;
		}
		String path = args[0];
		String method = args[1];
		boolean isDelayed = false;
		if(args.length > 2) {
			if(args[2].equals(DELAYED))
				isDelayed = true;
			else {
				printHelp();
				return;
			}
		}
		
		TMAXCalaculator calculator = null;
		
		switch (method) {
		case SEQUENTIAL:
			calculator = new SEQ_TMAXCalculator(path);
			break;
		case NOLOCK:
			calculator = new NOLOCK_TMAXCalculator(path);
			break;
		
		case COARSE:
			calculator = new COARSE_TMAXCalculator(path);
			break;
		
		case FINE:
			calculator = new FINE_TMAXCalculator(path);
			break;
		
		case NOSHARE:
			calculator = new NOSHARE_TMAXCalculator(path);
			break;

		default:
			printHelp();
			return;
		}
		runJob(calculator, isDelayed);
		//System.out.println(TMAXCalaculator.aveMap.get("CA007025280").getAverage());
	}
	
	public static void printHelp() {
		System.out.println("You need to provide at least two arguments in order to run this program!\n");
		System.out.println("The first argument should be the path of the file which has the weather data.\n");
		System.out.println("The second argument specifies the method of computation. It should be one of the followings:");
		System.out.println("\t -sequential");
		System.out.println("\t -nolock");
		System.out.println("\t -coarse");
		System.out.println("\t -fine");
		System.out.println("\t -noshare\n");
		System.out.println("The third argument (optional) by which the computation will be slowed down is:");
		System.out.println("\t -d\n");
		System.out.println("example: ");
		System.out.println("java -jar TMAXCalculator.jar input/1877.csv -sequential -d");
		
	}
}
