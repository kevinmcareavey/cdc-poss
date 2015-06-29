package merging_possibilistic_information;

import java.text.DecimalFormat;

public class Utilities {
	
	private static DecimalFormat formatter = new DecimalFormat("#.###");
	
	public static String format(double d) {
		return formatter.format(d);
	}
	
	public static double min(AdvancedSet<Double> set) {
		double min = Double.POSITIVE_INFINITY;
		for(Double element : set) {
			if(element < min) {
				min = element;
			}
		}
		return min;
	}
	
	public static double max(AdvancedSet<Double> set) {
		double max = Double.NEGATIVE_INFINITY;
		for(Double element : set) {
			if(element > max) {
				max = element;
			}
		}
		return max;
	}
	
	public static double log2(int n){
		return Math.log(n) / Math.log(2);
	}
	
}
