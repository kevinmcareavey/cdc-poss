package merging_possibilistic_information;

import java.util.HashMap;
import java.util.Map;

public class PIB<T> {
	
	private String label;
	private AdvancedSet<T> frame;
	private Map<AdvancedSet<T>, Double> lowerNecessities;
	
	public PIB(String l, AdvancedSet<T> f) {
		label = l;
		frame = f;
		lowerNecessities = new HashMap<AdvancedSet<T>, Double>();
	}
	
	public String getLabel() {
		return label;
	}
	
	public AdvancedSet<T> getFrame() {
		return frame;
	}
	
	public Map<AdvancedSet<T>, Double> getLowerNecessities() {
		return lowerNecessities;
	}
	
	public AdvancedSet<AdvancedSet<T>> getFocalSets() {
		AdvancedSet<AdvancedSet<T>> focalSets = new AdvancedSet<AdvancedSet<T>>();
		for(Map.Entry<AdvancedSet<T>, Double> entry : lowerNecessities.entrySet()) {
			focalSets.add(entry.getKey());
		}
		return focalSets;
	}
	
	public void addLowerNecessity(AdvancedSet<T> subset, double value) {
		if(value < 0 || value > 1) {
			if(value < 0 && value > -0.00000001) {
				value = (double)0;
			} else if(value > 1 && value < 1.00000001) {
				value = (double)1;
			} else {
				throw new IllegalArgumentException("The possibility value must be in the range [0, 1].");
			}
		}
		if(!subset.subsetOf(frame)) {
			throw new IllegalArgumentException("The input must be a subset of the frame of discernment.");
		}
		if(value == 0) {
			lowerNecessities.remove(subset);
		} else {
			lowerNecessities.put(subset, value);
		}
	}
	
	public double getLowerNecessity(AdvancedSet<T> subset) {
		double result = 0;
		if(lowerNecessities.containsKey(subset)) {
			result = lowerNecessities.get(subset);
		}
		return result;
	}
	
	public boolean isCommensurable(PIB<T> other) {
		return this.getFrame().equals(other.getFrame());
	}
	
	public PossibilityDistribution<T> getMinimumSpecificPossibilityDistribution() {
		PossibilityDistribution<T> possibilityDistribution = new PossibilityDistribution<T>(frame);
		for(T element : frame) {
			AdvancedSet<Double> values = new AdvancedSet<Double>();
			values.add((double)1);
			for(Map.Entry<AdvancedSet<T>, Double> entry : lowerNecessities.entrySet()) {
				AdvancedSet<T> focalSet = entry.getKey();
				Double lowerNecessity = entry.getValue();
				if(!focalSet.contains(element)) {
					values.add(1 - lowerNecessity);
				}
			}
			possibilityDistribution.setPossibility(element, Utilities.min(values));
		}
		return possibilityDistribution;
	}
	
	public PIB<T> getConjunctiveMerge(PIB<T> other) throws Exception {
		PIB<T> merged = null;
		if(this.isCommensurable(other)) {
			merged = new PIB<T>(this.getLabel() + " && " + other.getLabel(), frame);
			AdvancedSet<AdvancedSet<T>> focalSets = this.getFocalSets().union(other.getFocalSets());
			PossibilityDistribution<T> thisPossibilityDistribution = this.getMinimumSpecificPossibilityDistribution();
			PossibilityDistribution<T> otherPossibilityDistribution = other.getMinimumSpecificPossibilityDistribution();
			for(AdvancedSet<T> focalSet : focalSets) {
				AdvancedSet<T> difference = frame.setMinus(focalSet);
				AdvancedSet<Double> differencePossibilities = new AdvancedSet<Double>();
				for(T element : difference) {
					AdvancedSet<Double> elementPossibilities = new AdvancedSet<Double>();
					elementPossibilities.add(thisPossibilityDistribution.getPossibility(element));
					elementPossibilities.add(otherPossibilityDistribution.getPossibility(element));
					differencePossibilities.add(Utilities.min(elementPossibilities));
				}
				merged.addLowerNecessity(focalSet, 1 - Utilities.max(differencePossibilities));
			}
		}
		return merged;
	}
	
	public PIB<T> getDisjunctiveMerge(PIB<T> other) throws Exception {
		PIB<T> merged = null;
		if(this.isCommensurable(other)) {
			merged = new PIB<T>(this.getLabel() + " || " + other.getLabel(), frame);
			AdvancedSet<AdvancedSet<T>> thisFocalSets = this.getFocalSets();
			AdvancedSet<AdvancedSet<T>> otherFocalSets = this.getFocalSets();
			AdvancedSet<AdvancedSet<T>> focalSets = thisFocalSets.union(otherFocalSets);
			for(AdvancedSet<T> thisFocalSet : thisFocalSets) {
				for(AdvancedSet<T> otherFocalSet : thisFocalSets) {
					focalSets.add(thisFocalSet.union(otherFocalSet));
				}
			}
			PossibilityDistribution<T> thisPossibilityDistribution = this.getMinimumSpecificPossibilityDistribution();
			PossibilityDistribution<T> otherPossibilityDistribution = other.getMinimumSpecificPossibilityDistribution();
			for(AdvancedSet<T> focalSet : focalSets) {
				AdvancedSet<T> difference = frame.setMinus(focalSet);
				AdvancedSet<Double> differencePossibilities = new AdvancedSet<Double>();
				for(T element : difference) {
					AdvancedSet<Double> elementPossibilities = new AdvancedSet<Double>();
					elementPossibilities.add(thisPossibilityDistribution.getPossibility(element));
					elementPossibilities.add(otherPossibilityDistribution.getPossibility(element));
					differencePossibilities.add(Utilities.max(elementPossibilities));
				}
				merged.addLowerNecessity(focalSet, 1 - Utilities.max(differencePossibilities));
			}
		}
		return merged;
	}
	
	public double getInconsistency() {
		double max = Double.NEGATIVE_INFINITY;
		for(Map.Entry<T, Double> entry : this.getMinimumSpecificPossibilityDistribution().entrySet()) {
			double possibility = entry.getValue();
			if(possibility > max) {
				max = possibility;
			}
		}
		return (double)1 - max;
	}
	
	public PIB<T> getOpinionBase() {
		return this;
	}
	
	public PIB<T> getConflictBase() {
		PIB<T> opinionBase = this.getOpinionBase();
		PIB<T> conflictBase = new PIB<T>("ConflictBase(" + opinionBase.getLabel() + ")", opinionBase.getFrame());
		for(Map.Entry<AdvancedSet<T>, Double> outer : opinionBase.getLowerNecessities().entrySet()) {
			AdvancedSet<T> focalSet = outer.getKey();
			double lowerNecessity = outer.getValue();
			for(Map.Entry<AdvancedSet<T>, Double> inner : opinionBase.getLowerNecessities().entrySet()) {
				AdvancedSet<T> other = inner.getKey();
				if(!focalSet.intersects(other)) {
					conflictBase.addLowerNecessity(focalSet, lowerNecessity);
					break;
				}
			}
		}
		return conflictBase;
	}
	
	public PIB<T> getUpperConflictBase() {
		PIB<T> opinionBase = this.getOpinionBase();
		PIB<T> upperConflictBase = new PIB<T>("UpperConflictBase(" + opinionBase.getLabel() + ")", opinionBase.getFrame());
		for(Map.Entry<AdvancedSet<T>, Double> outer : opinionBase.getLowerNecessities().entrySet()) {
			AdvancedSet<T> focalSet = outer.getKey();
			double lowerNecessity = outer.getValue();
			boolean upperConflict = true;
			for(Map.Entry<AdvancedSet<T>, Double> inner : opinionBase.getLowerNecessities().entrySet()) {
				AdvancedSet<T> other = inner.getKey();
				if(!focalSet.equals(other) && focalSet.intersects(other)) {
					upperConflict = false;
					break;
				}
			}
			if(upperConflict) {
				upperConflictBase.addLowerNecessity(focalSet, lowerNecessity);
			}
		}
		return upperConflictBase;
	}
	
	public double a() {
		double sum = 0;
		for(Map.Entry<AdvancedSet<T>, Double> entry : lowerNecessities.entrySet()) {
			sum += entry.getValue();
		}
		return sum;
	}
	
	public double getCoherence() {
		return (double)1 - (this.getConflictBase().a() / this.getOpinionBase().a());
	}
	
	public double getUpperCoherence() {
		return (double)1 - (this.getUpperConflictBase().a() / this.getOpinionBase().a());
	}
	
	public CoherenceInterval getCoherenceInterval() {
		return new CoherenceInterval(this.getCoherence(), this.getUpperCoherence());
	}
	
	@Override
	public String toString() {
		String output = "{";
		String delim = "";
		for(Map.Entry<AdvancedSet<T>, Double> entry : lowerNecessities.entrySet()) {
			output += delim + "N(";
			if(entry.getKey().equals(frame)) {
				output += "...";
			} else {
				output += entry.getKey().toString();
			}
			output += ")>=" + Utilities.format(entry.getValue());
			delim = ", ";
		}
		output += "}";
		return output;
	}
	
}
