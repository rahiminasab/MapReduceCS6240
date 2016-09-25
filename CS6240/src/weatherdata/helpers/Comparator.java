package weatherdata.helpers;

import java.util.HashMap;
import java.util.concurrent.ConcurrentHashMap;

import weatherdata.TMAXCalaculator;
import weatherdata.parallels.COARSE_TMAXCalculator;
import weatherdata.parallels.FINE_TMAXCalculator;
import weatherdata.parallels.NOLOCK_TMAXCalculator;
import weatherdata.parallels.NOSHARE_TMAXCalculator;
import weatherdata.sequential.SEQ_TMAXCalculator;

/*
 * For comparing the results of each calculator.
 */
public class Comparator {

	public static void main(String[] args) throws InterruptedException {
		String path = args[0];
		TMAXCalaculator.readInput(path);
		HashMap<String, AverageInfo> correct;
		TMAXCalaculator calculator = new SEQ_TMAXCalculator();
		calculator.calc(false);
		correct =  copyMap(TMAXCalaculator.aveMap);
		
		TMAXCalaculator[] pars = {new SEQ_TMAXCalculator(), new NOLOCK_TMAXCalculator(), new COARSE_TMAXCalculator(), new FINE_TMAXCalculator(), new NOSHARE_TMAXCalculator()};
		
		for(TMAXCalaculator par : pars) {
			par.calc(false);
			System.out.println("equal results SEQUENTIAL and "+par.name+"?");
			if(!(par instanceof FINE_TMAXCalculator)) {
				System.out.println(compare(correct, TMAXCalaculator.aveMap)+"\n");
			} else {
				System.out.println(compareConcurrent(correct, FINE_TMAXCalculator.aveMap)+"\n");
			}
		}
	}
	private static HashMap<String, AverageInfo> copyMap(HashMap<String, AverageInfo> aveMap) {
		HashMap<String, AverageInfo> correct = new HashMap<String, AverageInfo>();
		for(String key : aveMap.keySet()) {
			correct.put(key, aveMap.get(key));
		}
		return correct;
	}
	static boolean compare(HashMap<String, AverageInfo> correct, HashMap<String, AverageInfo> test) {
		for(String key: correct.keySet()) {
			if(!test.containsKey(key)){
				System.out.println("no key in test");
				return false;
			}
			double corAve = correct.get(key).getAverage();
			double testAve = test.get(key).getAverage();
			if(corAve != testAve){
				System.out.println("diff avs "+corAve+" "+testAve+" for station: "+key);
				return false;
			}
		}
		return true;
	}
	
	static boolean compareConcurrent(HashMap<String, AverageInfo> correct, ConcurrentHashMap<String, AverageInfo> test) {
		for(String key: correct.keySet()) {
			if(!test.containsKey(key)){
				System.out.println("no key in test");
				return false;
			}
			double corAve = correct.get(key).getAverage();
			double testAve = test.get(key).getAverage();
			if(corAve != testAve){
				System.out.println("diff avs "+corAve+" "+testAve+" for station: "+key);
				return false;
			}
		}
		return true;
	}
}
