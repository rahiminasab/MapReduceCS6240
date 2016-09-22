package weatherdata.parallels;

/*
 * An object of this class uses the same computeAverge function as its parent which has
 * no lock anywhere.
 */
public class NOLOCK_TMAXCalculator extends ParallelTMAXCalculator {
	
	public NOLOCK_TMAXCalculator(String inputPath) {
		super(inputPath);
		name = "NO-LOCK";
	}

}
