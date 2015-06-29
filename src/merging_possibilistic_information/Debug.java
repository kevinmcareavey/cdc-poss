package merging_possibilistic_information;

public class Debug {
	
	public static void example1() {
		try {
			AdvancedSet<String> frame = new AdvancedSet<String>("w1", "w2", "w3", "w4", "w5");
			
			PIB<String> k1 = new PIB<String>("k1", frame);
			k1.addLowerNecessity(new AdvancedSet<String>("w1", "w2"), 0.2);
			k1.addLowerNecessity(new AdvancedSet<String>("w3", "w4"), 0.3);
			k1.addLowerNecessity(new AdvancedSet<String>("w5"), 0.3);
			
			PIB<String> k2 = new PIB<String>("k2", frame);
			k2.addLowerNecessity(new AdvancedSet<String>("w1", "w2"), 0.2);
			k2.addLowerNecessity(new AdvancedSet<String>("w2", "w3"), 0.3);
			k2.addLowerNecessity(new AdvancedSet<String>("w4", "w5"), 0.3);
			
			System.out.println(k1.getLabel() + " = " + k1);
			System.out.println(k2.getLabel() + " = " + k2);
			
			PossibilityDistribution<String> pik1 = k1.getMinimumSpecificPossibilityDistribution();
			PossibilityDistribution<String> pik2 = k2.getMinimumSpecificPossibilityDistribution();
			
			System.out.println("\\pi_" + k1.getLabel() + " = " + pik1);
			System.out.println("\\pi_" + k2.getLabel() + " = " + pik2);
			
			PIB<String> conjunctiveMerge = k1.getConjunctiveMerge(k2);
			PIB<String> disjunctiveMerge = k1.getDisjunctiveMerge(k2);
			
			System.out.println(conjunctiveMerge.getLabel() + " = " + k1);
			System.out.println(disjunctiveMerge.getLabel() + " = " + k2);
			
			PossibilityDistribution<String> piconjunctiveMerge = conjunctiveMerge.getMinimumSpecificPossibilityDistribution();
			PossibilityDistribution<String> pidisjunctiveMerge = disjunctiveMerge.getMinimumSpecificPossibilityDistribution();
			
			System.out.println("\\pi_" + conjunctiveMerge.getLabel() + " = " + piconjunctiveMerge);
			System.out.println("\\pi_" + disjunctiveMerge.getLabel() + " = " + pidisjunctiveMerge);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void example2() {
		try {
			AdvancedSet<String> frame = new AdvancedSet<String>("w1", "w2", "w3", "w4", "w5");
			
			PIB<String> k11 = new PIB<String>("k11", frame);
			k11.addLowerNecessity(new AdvancedSet<String>("w1", "w2"), 0.4);
			k11.addLowerNecessity(new AdvancedSet<String>("w2", "w3", "w4"), 0.5);
			k11.addLowerNecessity(new AdvancedSet<String>("w2"), 0.4);
			
			PIB<String> k12 = new PIB<String>("k12", frame);
			k12.addLowerNecessity(new AdvancedSet<String>("w1", "w2"), 0.3);
			k12.addLowerNecessity(new AdvancedSet<String>("w1", "w2", "w3"), 0.5);
			k12.addLowerNecessity(new AdvancedSet<String>("w1", "w4"), 0.4);
			
			PIB<String> k13 = new PIB<String>("k13", frame);
			k13.addLowerNecessity(new AdvancedSet<String>("w1", "w3"), 0.4);
			k13.addLowerNecessity(new AdvancedSet<String>("w2", "w3", "w4"), 0.5);
			k13.addLowerNecessity(new AdvancedSet<String>("w3"), 0.4);
			
			PIB<String> k14 = new PIB<String>("k14", frame);
			k14.addLowerNecessity(new AdvancedSet<String>("w2", "w4"), 0.3);
			k14.addLowerNecessity(new AdvancedSet<String>("w1", "w3", "w4"), 0.5);
			k14.addLowerNecessity(new AdvancedSet<String>("w1", "w4"), 0.4);
			
			System.out.println(k11.getLabel() + " = " + k11);
			System.out.println(k12.getLabel() + " = " + k12);
			System.out.println(k13.getLabel() + " = " + k13);
			System.out.println(k14.getLabel() + " = " + k14);
			
			PossibilityDistribution<String> pik11 = k11.getMinimumSpecificPossibilityDistribution();
			PossibilityDistribution<String> pik12 = k12.getMinimumSpecificPossibilityDistribution();
			PossibilityDistribution<String> pik13 = k13.getMinimumSpecificPossibilityDistribution();
			PossibilityDistribution<String> pik14 = k14.getMinimumSpecificPossibilityDistribution();
			
			System.out.println("\\pi_" + k11.getLabel() + " = " + pik11);
			System.out.println("\\pi_" + k12.getLabel() + " = " + pik12);
			System.out.println("\\pi_" + k13.getLabel() + " = " + pik13);
			System.out.println("\\pi_" + k14.getLabel() + " = " + pik14);
			
			System.out.println("H(\\pi_" + k11.getLabel() + ") = " + Utilities.format(pik11.getNonspecificity()));
			System.out.println("H(\\pi_" + k12.getLabel() + ") = " + Utilities.format(pik12.getNonspecificity()));
			System.out.println("H(\\pi_" + k13.getLabel() + ") = " + Utilities.format(pik13.getNonspecificity()));
			System.out.println("H(\\pi_" + k14.getLabel() + ") = " + Utilities.format(pik14.getNonspecificity()));
			
			PIB<String> k11andk12 = k11.getConjunctiveMerge(k12);
			PIB<String> k11andk13 = k11.getConjunctiveMerge(k13);
			PIB<String> k11andk14 = k11.getConjunctiveMerge(k14);
			
			PIB<String> k12andk11 = k12.getConjunctiveMerge(k11);
			PIB<String> k12andk13 = k12.getConjunctiveMerge(k13);
			PIB<String> k12andk14 = k12.getConjunctiveMerge(k14);
			
			PIB<String> k13andk11 = k13.getConjunctiveMerge(k11);
			PIB<String> k13andk12 = k13.getConjunctiveMerge(k12);
			PIB<String> k13andk14 = k13.getConjunctiveMerge(k14);
			
			PIB<String> k14andk11 = k14.getConjunctiveMerge(k11);
			PIB<String> k14andk12 = k14.getConjunctiveMerge(k12);
			PIB<String> k14andk13 = k14.getConjunctiveMerge(k13);
			
			System.out.println("CoherenceRange(" + k11andk12.getLabel() + ") = " + k11andk12.getCoherenceInterval());
			System.out.println("CoherenceRange(" + k11andk13.getLabel() + ") = " + k11andk13.getCoherenceInterval());
			System.out.println("CoherenceRange(" + k11andk14.getLabel() + ") = " + k11andk14.getCoherenceInterval());
			
			System.out.println("CoherenceRange(" + k12andk11.getLabel() + ") = " + k12andk11.getCoherenceInterval());
			System.out.println("CoherenceRange(" + k12andk13.getLabel() + ") = " + k12andk13.getCoherenceInterval());
			System.out.println("CoherenceRange(" + k12andk14.getLabel() + ") = " + k12andk14.getCoherenceInterval());
			
			System.out.println("CoherenceRange(" + k13andk11.getLabel() + ") = " + k13andk11.getCoherenceInterval());
			System.out.println("CoherenceRange(" + k13andk12.getLabel() + ") = " + k13andk12.getCoherenceInterval());
			System.out.println("CoherenceRange(" + k13andk14.getLabel() + ") = " + k13andk14.getCoherenceInterval());
			
			System.out.println("CoherenceRange(" + k14andk11.getLabel() + ") = " + k14andk11.getCoherenceInterval());
			System.out.println("CoherenceRange(" + k14andk12.getLabel() + ") = " + k14andk12.getCoherenceInterval());
			System.out.println("CoherenceRange(" + k14andk13.getLabel() + ") = " + k14andk13.getCoherenceInterval());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void example3() {
		try {
			AdvancedSet<String> frame = new AdvancedSet<String>("w1", "w2", "w3", "w4", "w5");
			
			PIB<String> k21 = new PIB<String>("k21", frame);
			k21.addLowerNecessity(new AdvancedSet<String>("w2", "w3", "w4"), 0.5);
			k21.addLowerNecessity(new AdvancedSet<String>("w2"), 0.4);
			
			PIB<String> k22 = new PIB<String>("k22", frame);
			k22.addLowerNecessity(new AdvancedSet<String>("w1"), 0.3);
			k22.addLowerNecessity(new AdvancedSet<String>("w1", "w3"), 0.5);
			k22.addLowerNecessity(new AdvancedSet<String>("w1", "w4"), 0.4);
			
			PIB<String> k23 = new PIB<String>("k23", frame);
			k23.addLowerNecessity(new AdvancedSet<String>("w2", "w3"), 0.4);
			k23.addLowerNecessity(new AdvancedSet<String>("w2", "w3", "w4"), 0.5);
			
			PIB<String> k24 = new PIB<String>("k24", frame);
			k24.addLowerNecessity(new AdvancedSet<String>("w1", "w2"), 0.3);
			k24.addLowerNecessity(new AdvancedSet<String>("w1", "w3", "w4"), 0.5);
			k24.addLowerNecessity(new AdvancedSet<String>("w1", "w4"), 0.4);
			
			System.out.println(k21.getLabel() + " = " + k21);
			System.out.println(k22.getLabel() + " = " + k22);
			System.out.println(k23.getLabel() + " = " + k23);
			System.out.println(k24.getLabel() + " = " + k24);
			
			PossibilityDistribution<String> pik21 = k21.getMinimumSpecificPossibilityDistribution();
			PossibilityDistribution<String> pik22 = k22.getMinimumSpecificPossibilityDistribution();
			PossibilityDistribution<String> pik23 = k23.getMinimumSpecificPossibilityDistribution();
			PossibilityDistribution<String> pik24 = k24.getMinimumSpecificPossibilityDistribution();
			
			System.out.println("\\pi_" + k21.getLabel() + " = " + pik21);
			System.out.println("\\pi_" + k22.getLabel() + " = " + pik22);
			System.out.println("\\pi_" + k23.getLabel() + " = " + pik23);
			System.out.println("\\pi_" + k24.getLabel() + " = " + pik24);
			
			System.out.println("H(\\pi_" + k21.getLabel() + ") = " + Utilities.format(pik21.getNonspecificity()));
			System.out.println("H(\\pi_" + k22.getLabel() + ") = " + Utilities.format(pik22.getNonspecificity()));
			System.out.println("H(\\pi_" + k23.getLabel() + ") = " + Utilities.format(pik23.getNonspecificity()));
			System.out.println("H(\\pi_" + k24.getLabel() + ") = " + Utilities.format(pik24.getNonspecificity()));
			
			PIB<String> k21andk22 = k21.getConjunctiveMerge(k22);
			PIB<String> k21andk23 = k21.getConjunctiveMerge(k23);
			PIB<String> k21andk24 = k21.getConjunctiveMerge(k24);
			
			PIB<String> k22andk21 = k22.getConjunctiveMerge(k21);
			PIB<String> k22andk23 = k22.getConjunctiveMerge(k23);
			PIB<String> k22andk24 = k22.getConjunctiveMerge(k24);
			
			PIB<String> k23andk21 = k23.getConjunctiveMerge(k21);
			PIB<String> k23andk22 = k23.getConjunctiveMerge(k22);
			PIB<String> k23andk24 = k23.getConjunctiveMerge(k24);
			
			PIB<String> k24andk21 = k24.getConjunctiveMerge(k21);
			PIB<String> k24andk22 = k24.getConjunctiveMerge(k22);
			PIB<String> k24andk23 = k24.getConjunctiveMerge(k23);
			
			System.out.println("CoherenceRange(" + k21andk22.getLabel() + ") = " + k21andk22.getCoherenceInterval());
			System.out.println("CoherenceRange(" + k21andk23.getLabel() + ") = " + k21andk23.getCoherenceInterval());
			System.out.println("CoherenceRange(" + k21andk24.getLabel() + ") = " + k21andk24.getCoherenceInterval());
			
			System.out.println("CoherenceRange(" + k22andk21.getLabel() + ") = " + k22andk21.getCoherenceInterval());
			System.out.println("CoherenceRange(" + k22andk23.getLabel() + ") = " + k22andk23.getCoherenceInterval());
			System.out.println("CoherenceRange(" + k22andk24.getLabel() + ") = " + k22andk24.getCoherenceInterval());
			
			System.out.println("CoherenceRange(" + k23andk21.getLabel() + ") = " + k23andk21.getCoherenceInterval());
			System.out.println("CoherenceRange(" + k23andk22.getLabel() + ") = " + k23andk22.getCoherenceInterval());
			System.out.println("CoherenceRange(" + k23andk24.getLabel() + ") = " + k23andk24.getCoherenceInterval());
			
			System.out.println("CoherenceRange(" + k24andk21.getLabel() + ") = " + k24andk21.getCoherenceInterval());
			System.out.println("CoherenceRange(" + k24andk22.getLabel() + ") = " + k24andk22.getCoherenceInterval());
			System.out.println("CoherenceRange(" + k24andk23.getLabel() + ") = " + k24andk23.getCoherenceInterval());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void nonspecificity() {
		try {
			AdvancedSet<String> frame = new AdvancedSet<String>("w1", "w2", "w3", "w4", "w5");
			
			PossibilityDistribution<String> precise = new PossibilityDistribution<String>(frame);
			precise.setPossibility("w1", (double)1);
			
			PossibilityDistribution<String> uniform = new PossibilityDistribution<String>(frame);
			uniform.setPossibility("w1", (double)1);
			uniform.setPossibility("w2", (double)1);
			uniform.setPossibility("w3", (double)1);
			uniform.setPossibility("w4", (double)1);
			uniform.setPossibility("w5", (double)1);
			
			System.out.println("H(" + precise + ") = " + Utilities.format(precise.getNonspecificity()));
			System.out.println("H(" + uniform + ") = " + Utilities.format(uniform.getNonspecificity()));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void lpmcs() {
		try {
			AdvancedSet<String> frame = new AdvancedSet<String>("w1", "w2", "w3", "w4", "w5");
			
			PIB<String> k11 = new PIB<String>("k11", frame);
			k11.addLowerNecessity(new AdvancedSet<String>("w1", "w2"), 0.4);
			k11.addLowerNecessity(new AdvancedSet<String>("w2", "w3", "w4"), 0.5);
			k11.addLowerNecessity(new AdvancedSet<String>("w2"), 0.4);
			
			PIB<String> k12 = new PIB<String>("k12", frame);
			k12.addLowerNecessity(new AdvancedSet<String>("w1", "w2"), 0.3);
			k12.addLowerNecessity(new AdvancedSet<String>("w1", "w2", "w3"), 0.5);
			k12.addLowerNecessity(new AdvancedSet<String>("w1", "w4"), 0.4);
			
			PIB<String> k13 = new PIB<String>("k13", frame);
			k13.addLowerNecessity(new AdvancedSet<String>("w1", "w3"), 0.4);
			k13.addLowerNecessity(new AdvancedSet<String>("w2", "w3", "w4"), 0.5);
			k13.addLowerNecessity(new AdvancedSet<String>("w3"), 0.4);
			
			PIB<String> k14 = new PIB<String>("k14", frame);
			k14.addLowerNecessity(new AdvancedSet<String>("w2", "w4"), 0.3);
			k14.addLowerNecessity(new AdvancedSet<String>("w1", "w3", "w4"), 0.5);
			k14.addLowerNecessity(new AdvancedSet<String>("w1", "w4"), 0.4);
			
			PIBSet<String> set = new PIBSet<String>(frame);
			set.add(k11);
			set.add(k12);
			set.add(k13);
			set.add(k14);
			
			System.out.println(set.getLPMCSMerge(0.5, 0.5, 0.8).getLabel());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
//		example1();
//		example2();
//		example3();
//		nonspecificity();
		lpmcs();
	}
	
}
