package merging_possibilistic_information;

import java.util.Comparator;

public class PIBSet<T> extends AdvancedSet<PIB<T>> {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 5058454026322089987L;
	
	private AdvancedSet<T> frame;
	
	public PIBSet(AdvancedSet<T> f) {
		frame = f;
	}
	
	public AdvancedSet<T> getFrame() {
		return frame;
	}
	
	@Override
	public boolean add(PIB<T> pib) {
		if(!pib.getFrame().equals(frame)) {
			throw new IllegalArgumentException("All BBAs in set must share the same frame.");
		}
		return super.add(pib);
	}
	
	public PIB<T> getConjunctiveMerge() throws Exception {
		if(this.isEmpty()) {
			throw new Exception("No BBAs to merge.");
		}
		
		PIB<T> reference = null;
		for(PIB<T> next : this) {
			if(reference == null) {
				reference = next;
			} else {
				reference = reference.getConjunctiveMerge(next);
			}
		}
		
		return reference;
	}
	
	public PIB<T> getDisjunctiveMerge() throws Exception {
		if(this.isEmpty()) {
			throw new Exception("No BBAs to merge.");
		}
		
		PIB<T> reference = null;
		for(PIB<T> next : this) {
			if(reference == null) {
				reference = next;
			} else {
				reference = reference.getDisjunctiveMerge(next);
			}
		}
		
		return reference;
	}
	
	public double getInformationLoss() throws Exception {
		AdvancedSet<AdvancedSet<T>> si = new AdvancedSet<AdvancedSet<T>>();
		for(PIB<T> pib : this) {
			si.addAll(pib.getFocalSets());
		}
		AdvancedSet<AdvancedSet<T>> sdm = this.getDisjunctiveMerge().getFocalSets();
		return (double)1 - ((double)sdm.intersection(si).size() / (double)si.size());
	}
	
	public boolean isRelaxationFeasible(double inconsistencyThreshold, double coherenceThreshold, double informationLossThreshold) throws Exception {
		PIB<T> conjunctiveMerge = this.getConjunctiveMerge();
		double inconsistencyConjunctiveMerge = conjunctiveMerge.getInconsistency();
		if(inconsistencyConjunctiveMerge == 0) {
			return true;
		} else if((inconsistencyConjunctiveMerge > 0 && inconsistencyConjunctiveMerge <= inconsistencyThreshold)
				&& (conjunctiveMerge.getCoherence() >= coherenceThreshold)
				&& (this.getInformationLoss() >= informationLossThreshold)) {
			return true;
		} else {
			return false;
		}
	}
	
	public PIBSet<T> copy() {
		PIBSet<T> copy = new PIBSet<T>(frame);
		copy.addAll(this);
		return copy;
	}
	
	public PIB<T> getLPMCSMerge(double inconsistencyThreshold, double coherenceThreshold, double informationLossThreshold) throws Exception {
		if(this.isEmpty()) {
			throw new Exception("No BBAs to merge.");
		}
		
		PIBSequence<T> heuristicRanking = new PIBSequence<T>(this);
		heuristicRanking.sort(new HeuristicComparator<T>());
		
		PIB<T> reference = heuristicRanking.remove(0);
		
		PIBSequence<T> distanceRanking = heuristicRanking.copy();
		distanceRanking.sort(new DistanceComparator<T>(reference));
		
		AdvancedSet<PIBSet<T>> lpmcses = new AdvancedSet<PIBSet<T>>();
		PIBSet<T> lpmcs = new PIBSet<T>(frame);
		lpmcs.add(reference);
		while(!distanceRanking.isEmpty()) {
			PIBSet<T> partialLpmcs = lpmcs.copy();
			PIB<T> next = distanceRanking.get(0);
			partialLpmcs.add(next);
			if(partialLpmcs.isRelaxationFeasible(inconsistencyThreshold, coherenceThreshold, informationLossThreshold)) {
				lpmcs = partialLpmcs;
				heuristicRanking.remove(next);
				distanceRanking.remove(0);
			} else {
				lpmcses.add(lpmcs);
				reference = heuristicRanking.remove(0);
				distanceRanking.remove(reference);
				distanceRanking.sort(new DistanceComparator<T>(reference));
				lpmcs = new PIBSet<T>(frame);
				lpmcs.add(reference);
			}
		}
		lpmcses.add(lpmcs);
		
		PIBSet<T> conjunctiveMerges = new PIBSet<T>(frame);
		for(PIBSet<T> set : lpmcses) {
			conjunctiveMerges.add(set.getConjunctiveMerge());
		}
		
		return conjunctiveMerges.getDisjunctiveMerge();
	}
	
	@Override
	public String toString() {
		AdvancedSet<String> output = new AdvancedSet<String>();
		for(PIB<T> element : this) {
			output.add(element.getLabel());
		}
		return output.toString();
	}
	
}

class HeuristicComparator<T> implements Comparator<PIB<T>> {
	
	public int compare(PIB<T> a, PIB<T> b) {
		double aNonspecificity = a.getMinimumSpecificPossibilityDistribution().getNonspecificity();
		double bNonspecificity = b.getMinimumSpecificPossibilityDistribution().getNonspecificity();
		
		double aInconsistency = a.getInconsistency();
		double bInconsistency = b.getInconsistency();
		
		CoherenceInterval aCoherenceInterval = a.getCoherenceInterval();
		CoherenceInterval bCoherenceInterval = b.getCoherenceInterval();
		
		if((aInconsistency == 0 && bInconsistency == 0 && aNonspecificity < bNonspecificity)
				|| (aInconsistency < bInconsistency)
				|| (aInconsistency == bInconsistency && bCoherenceInterval.compareTo(aCoherenceInterval) < 0)) {
			return -1;
		} else if(aInconsistency == bInconsistency && aNonspecificity == bNonspecificity && aCoherenceInterval.equals(bCoherenceInterval)) {
			return 0;
		} else {
			return 1;
		}
	}
	
}

class DistanceComparator<T> implements Comparator<PIB<T>> {
	
	private PIB<T> reference;
	
	public DistanceComparator(PIB<T> r) {
		reference = r;
	}
	
	public int compare(PIB<T> a, PIB<T> b) {
		PIB<T> aMerge = null;
		PIB<T> bMerge = null;
		try {
			aMerge = reference.getConjunctiveMerge(a);
			bMerge = reference.getConjunctiveMerge(b);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		PossibilityDistribution<T> aMergePossibilityDistribution = aMerge.getMinimumSpecificPossibilityDistribution();
		PossibilityDistribution<T> bMergePossibilityDistribution = bMerge.getMinimumSpecificPossibilityDistribution();
		
		double aMergeNonspecificity = aMergePossibilityDistribution.getNonspecificity();
		double bMergeNonspecificity = bMergePossibilityDistribution.getNonspecificity();
		
		double aMergeInconsistency = aMerge.getInconsistency();
		double bMergeInconsistency = bMerge.getInconsistency();
		
		CoherenceInterval aMergeCoherenceInterval = aMerge.getCoherenceInterval();
		CoherenceInterval bMergeCoherenceInterval = bMerge.getCoherenceInterval();
		
		if((aMergePossibilityDistribution.isNormal() && bMergePossibilityDistribution.isNormal() && aMergeNonspecificity < bMergeNonspecificity)
				|| (aMergeInconsistency < bMergeInconsistency)
				|| (aMergeInconsistency == bMergeInconsistency && bMergeCoherenceInterval.compareTo(aMergeCoherenceInterval) < 0)) {
			return -1;
		} else if(aMergeNonspecificity == bMergeNonspecificity && aMergeInconsistency == bMergeInconsistency && aMergeCoherenceInterval.equals(bMergeCoherenceInterval)) {
			return 0;
		} else {
			return 1;
		}
	}
	
}
