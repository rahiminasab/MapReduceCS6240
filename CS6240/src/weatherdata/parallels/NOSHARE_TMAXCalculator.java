package weatherdata.parallels;

/*
 * An instance of this object tells the threads to use their own maps for saving the intermediate
 * results, and merge them at last for the final result in the parent map.
 */
public class NOSHARE_TMAXCalculator extends ParallelTMAXCalculator {

	public NOSHARE_TMAXCalculator(String inputPath) {
		super(inputPath);
		name = "NO-SHARE";
	}

}