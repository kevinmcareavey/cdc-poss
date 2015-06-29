package merging_possibilistic_information;

public class CoherenceInterval implements Comparable<CoherenceInterval> {
	
	private double left;
	private double right;
	
	public CoherenceInterval(double l, double r) {
		left = l;
		right = r;
	}
	
	public double getLeft() {
		return left;
	}
	
	public double getRight() {
		return right;
	}
	
	@Override
	public int compareTo(CoherenceInterval other) {
		if(this.equals(other)) {
			return 0;
		} else if(((left == 0) && (right == 0) && (other.getLeft() == 0) && (other.getRight() > 0 && other.getRight() < 1))
			|| ((left == 0) && (right > 0 && right < 1) && (other.getLeft() == 0) && (other.getRight() == 1))
			|| ((left == 0) && (right == 1) && (other.getLeft() > 0 && other.getLeft() < 1) && (other.getRight() == 1))
			|| ((left > 0 && left < 1) && (right == 1) && (other.getLeft() == 1) && (other.getRight() == 1))
			|| ((left < other.getLeft()) && (right == 1) && (other.getRight() == 1))
			|| ((left == 0) && (right < other.getRight()) && (other.getLeft() == 0))) {
			return -1;
		} else {
			return 1;
		}
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		long temp;
		temp = Double.doubleToLongBits(left);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		temp = Double.doubleToLongBits(right);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		CoherenceInterval other = (CoherenceInterval) obj;
		if (Double.doubleToLongBits(left) != Double
				.doubleToLongBits(other.left))
			return false;
		if (Double.doubleToLongBits(right) != Double
				.doubleToLongBits(other.right))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "[" + Utilities.format(left) + ", " + Utilities.format(right) + "]";
	}
	
}
