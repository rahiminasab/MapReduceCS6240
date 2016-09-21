package hw1.parallels;

public class NOLOCK_TMAX_Calculator extends ParallelTMAXCalculator {
	
	NOLOCK_TMAX_Calculator(String inputPath) {
		super(inputPath);
		name = "NO-LOCK";
	}

	public static void main(String[] args) throws InterruptedException {
		NOLOCK_TMAX_Calculator obj = new NOLOCK_TMAX_Calculator("/home/ehsan/Desktop/MapReduce/HW1/input/1877.csv");
		runJob(obj, false);
		runJob(obj, true);
		//System.out.println(TMAXCalaculator.aveMap.get("CA007025280").getAverage());
	}

}
