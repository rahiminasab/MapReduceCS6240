package hw1.parallels;

public class NOSHARE_TMAX_Calculator extends ParallelTMAXCalculator {

	protected NOSHARE_TMAX_Calculator(String inputPath) {
		super(inputPath);
		name = "NO-SHARE";
	}

	public static void main(String[] args) throws InterruptedException {
		NOSHARE_TMAX_Calculator obj = new NOSHARE_TMAX_Calculator("/home/ehsan/Desktop/MapReduce/HW1/input/1877.csv");
		runJob(obj, false);
		runJob(obj, true);
		//System.out.println(TMAXCalaculator.aveMap.get("CA007025280").getAverage());
	}

}