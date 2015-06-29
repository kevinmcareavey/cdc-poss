package merging_possibilistic_information;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;

public class PossibilityDistribution<T> extends HashMap<T, Double> {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 6378472408271493264L;
	
	private AdvancedSet<T> frame;
	
	public PossibilityDistribution(AdvancedSet<T> f) {
		frame = f;
	}
	
	public double getPossibility(T element) {
		if(!frame.contains(element)) {
			throw new IllegalArgumentException("The input must be an element of the frame of discernment.");
		}
		if(this.containsKey(element)) {
			return this.get(element);
		} else {
			return (double)0;
		}
	}
	
	public void setPossibility(T element, Double value) {
		if(value < 0 || value > 1) {
			if(value < 0 && value > -0.00000001) {
				value = (double)0;
			} else if(value > 1 && value < 1.00000001) {
				value = (double)1;
			} else {
				throw new IllegalArgumentException("The possibility value must be in the range [0, 1].");
			}
		}
		if(!frame.contains(element)) {
			throw new IllegalArgumentException("The input must be an element of the frame of discernment.");
		}
		if(value == 0) {
			this.remove(element);
		} else {
			this.put(element, value);
		}
	}
	
	public boolean isNormal() {
		for(Map.Entry<T, Double> entry : this.entrySet()) {
			if(entry.getValue() == 1) {
				return true;
			}
		}
		return false;
	}
	
	public double getNonspecificity() {
		ArrayList<T> sorted = new ArrayList<T>();
		sorted.addAll(frame);
		sorted.sort(new PossibilityComparator<T>(this));
		
		double sum = 0;
		for(int i = 0; i < sorted.size(); i++) {
			double nextPossibility = 0;
			if(i < sorted.size() - 1) {
				nextPossibility = this.getPossibility(sorted.get(i + 1));
			}
			int rank = i + 1;
			sum += (this.getPossibility(sorted.get(i)) - nextPossibility) * Utilities.log2(rank);
		}
		return sum;
	}
	
	@Override
	public String toString() {
		String output = "{";
		String delim = "";
		for(Map.Entry<T, Double> entry : this.entrySet()) {
			output += delim + "\\pi(" + entry.getKey() + ")=" + Utilities.format(entry.getValue());
			delim = ", ";
		}
		output += "}";
		return output;
	}

}

class PossibilityComparator<T> implements Comparator<T> {

	private PossibilityDistribution<T> possibilityDistribution;

	public PossibilityComparator(PossibilityDistribution<T> p) {
		possibilityDistribution = p;
	}

	public int compare(T a, T b) {
		double aPossibility = possibilityDistribution.getPossibility(a);
		double bPossibility = possibilityDistribution.getPossibility(b);
		if(aPossibility > bPossibility) {
			return -1;
		} else if(aPossibility == bPossibility) {
			return 0;
		} else {
			return 1;
		}
	}

}
