package cn.ac.istic.types;

public class StringDoublePair extends Pair<String, Double> {
	
	public StringDoublePair(final String first, final Double second) {
		super(first, second); 
	}
	
	public StringDoublePair(final String first, final double second) {
		super(first, Double.valueOf(second)); 
	}
	
}
