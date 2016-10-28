package cn.ac.istic.corpus;

/**
 * 
 * @author XU, Shuo (pzczxs@gmail.com)
 * @version 1.0
 * 
 */
public class Instance {
	private String category;
	private SparseVector vector; 
	
	public Instance(final String category, final SparseVector vector) {
		this.category = category; 
		this.vector = vector; 
	}

	public String getCategory() {
		return this.category;
	}

	public void setCategory(final String category) {
		this.category = category;
	}

	public SparseVector getVector() {
		return this.vector;
	}

	public void setVector(final SparseVector vector) {
		this.vector = vector;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder(); 
		
		sb.append(category)
		  .append(" ")
		  .append(vector); 
		
		return sb.toString(); 
	}
}
