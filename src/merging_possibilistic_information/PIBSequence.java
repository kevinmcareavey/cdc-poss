package merging_possibilistic_information;

import java.util.ArrayList;

public class PIBSequence<T> extends ArrayList<PIB<T>> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8455598628093022627L;
	
	private AdvancedSet<T> frame;
	
	public PIBSequence(AdvancedSet<T> f) {
		frame = f;
	}
	
	public PIBSequence(PIBSet<T> set) {
		frame = set.getFrame();
		this.addAll(set);
	}
	
	public PIBSequence<T> copy() {
		PIBSequence<T> copy = new PIBSequence<T>(frame);
		copy.addAll(this);
		return copy;
	}
	
	@Override
	public String toString() {
		String output = "(";
		String delim = "";
		for(PIB<T> element : this) {
			output += delim + element.getLabel();
			delim = ", ";
		}
		output += ")";
		return output;
	}
	
}
