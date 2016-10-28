package cn.ac.istic.corpus;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import cn.ac.istic.utils.MathUtils;
import cn.ac.istic.utils.StringUtils;

/**
 * This acts something like a Map from int to double
 * 
 * @author <a href = "mailto: pzczxs@gmail.com">XU, Shuo</a>
 * @version 1.0
 * 
 * */
public class SparseVector {
	private int[] indexes; 
	private double[] values; 
	
	public SparseVector(final int dimensions) {
		indexes = new int[dimensions];
		values = new double[dimensions]; 
	}
	
	public SparseVector(final int[] indexes, final double[] values) {
		this.indexes = indexes; 
		this.values = values; 
	}
	
	public SparseVector(final SparseVector sv1, final double p1, final SparseVector sv2, final double p2) {
		int maxDimensions = Math.max(sv1.maxIndex(), sv2.maxIndex()); 
		
		List<Integer> indexList = new ArrayList<Integer>(); 
		List<Double> valueList = new ArrayList<Double>(); 
		
		for (int i = 0; i < maxDimensions; i++) {
			double v = sv1.getValueById(i) * p1 + sv2.getValueById(i) * p2; 
			if (v != 0) {
				indexList.add(i); 
				valueList.add(v); 
			}
		}
		
		indexes = new int[indexList.size()];
		values = new double[indexes.length];
		for (int i = 0; i < indexList.size(); i++) {
			indexes[i] = indexList.get(i); 
			values[i] = valueList.get(i); 
		}
	}
	
	public int maxIndex() {
		return indexes[indexes.length - 1]; 
	}
	
	// return the number of nonzero entries
	public int nnz() {
		return indexes.length; 
	}
	
	public double getValueById(final int id) {
		final int j = Arrays.binarySearch(indexes, id); 
		
		return (j < 0)? 0: values[j]; 
	}
	
	public void setValueById(final int id, final double val) {
		final int j = Arrays.binarySearch(indexes, id); 
		
		if (j >= 0) {
			values[j] = val; 
		}
	}
	
	public void setValue(final int idx, final double val) {
		values[idx] = val; 
	}
	
	public int getIndex(final int idx) {
		return indexes[idx]; 
	}
	
	public double getValue(final int idx) {
		return values[idx]; 
	}
	
	public double sum() {
		double ret = 0.0; 
		
		for (double v: values) {
			ret += v; 
		}
		
		return ret; 
	}
	
	public double minValue() {
		return MathUtils.min(values); 
	}
	
	public double maxValue() {
		return MathUtils.max(values); 
	}
	
	public double dot(final SparseVector b) {
		SparseVector a = this; 
		double sum = 0; 
		
		for (int i = 0, j = 0; i < a.nnz() && j < b.nnz(); ) {
			if (a.indexes[i] == b.indexes[j]) {
				sum += a.values[i++] * b.values[j++]; 
			} else if (a.indexes[i] > b.indexes[j]) {
				++j; 
			} else {
				++i; 
			}
		}
		
		return sum; 
	}
	
	// return the 2-norm
	public double norm() {
		SparseVector a = this; 
		
		return Math.sqrt(a.dot(a)); 
	}
	
	// distance
	public double distance(final SparseVector b) {
		SparseVector a = this; 
		double sum = 0; 
		
		int i = 0, j = 0; 
		for (; i < a.nnz() && j < b.nnz(); ) {
			if (a.indexes[i] == b.indexes[j]) {
				double diff = a.values[i++] - b.values[j++]; 
				sum += diff * diff; 
			} else if (a.indexes[i] > b.indexes[j]) {
				sum += b.values[j] * b.values[j]; 
				++j; 
			} else {
				sum += a.values[i] * a.values[i]; 
				++i; 
			}
		}
		
		for (; i < a.nnz(); i++) {
			sum += a.values[i] * a.values[i]; 
		}
		
		for (; j < b.nnz(); j++) {
			sum += b.values[j] * b.values[j]; 
		}
		
		return sum; 
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder(); 
		
		for (int i = 0; i < nnz(); i++) {
			sb.append(indexes[i] + ":" + StringUtils.trimTrailingZeros(values[i])); 
			if (i < nnz() -1) {
				sb.append(" "); 
			}
		}
		
		return sb.toString(); 
	}
}
