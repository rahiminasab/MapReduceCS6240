package hw1.parallels;

/*
 * An object of this class uses the same computeAverge function as its parent which has
 * no lock anywhere.
 */
public class NOLOCK_TMAX_Calculator extends ParallelTMAXCalculator {
	
	public NOLOCK_TMAX_Calculator(String inputPath) {
		super(inputPath);
		name = "NO-LOCK";
	}

}
