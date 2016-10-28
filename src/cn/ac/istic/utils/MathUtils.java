package cn.ac.istic.utils;

public class MathUtils {
	/*
	 * Computes the sum of @logv assuming @logv is in the log domain. 
	 * 
	 * Returns log(sum(exp(logv))) while minimizing the possibility of over/underflow. 
	 * */
	public static double logsumexp(final double[] logv) {
		if (logv.length < 2) {
			System.err.println("logsumexp() -> canot sum less than 2 elements.");
			System.exit(-1);
		}
		
		double sum = (logv[0] > logv[1])? 
				(logv[0] + Math.log(1.0 + Math.exp(logv[1] - logv[0]))): (logv[1] + Math.log(1.0 + Math.exp(logv[0] - logv[1])));
		
		for (int n = 2; n < logv.length; n++) {
			sum = (logv[n] > sum)? 
					(logv[n] + Math.log(1.0 + Math.exp(sum - logv[n]))): (sum + Math.log(1.0 + Math.exp(logv[n] - sum))); 
		}
		
		return sum; 
	}
	
	public static double[] exp(final double[] v) {
		double[] expv = new double[v.length];
		
		for (int i = 0; i < v.length; i++) {
			expv[i] = Math.exp(v[i]); 
		}
		
		return expv; 
	}
	
	public static double[][] exp(final double[][] m) {
		double[][] expm = new double[m.length][]; 
		
		for (int i = 0; i < m.length; i++) {
			expm[i] = exp(m[i]); 
		}
		
		return expm; 
	}
	
	public static int argmax(final double[] elems) {
		double max = Double.NEGATIVE_INFINITY;
	    int idx = 0;
	    
	    for (int i = 0; i < elems.length; i++) {
	      if (elems[i] > max) {
	        max = elems[i];
	        idx = i;
	      }
	    }
	    
	    return idx;
	}
	
	public static double max(final double[] elems) {
		return elems[argmax(elems)]; 
	}
	
	public static double[] max(final double[][] matrix) {
		double[] ret = new double[matrix.length];
		
		for (int i = 0; i < matrix.length; i++) {
			ret[i] = max(matrix[i]); 
		}
		
		return ret; 
	}
	
	public static int argmax(final float[] elems) {
		double max = Double.NEGATIVE_INFINITY;
	    int idx = 0;
	    
	    for (int i = 0; i < elems.length; i++) {
	      if (elems[i] > max) {
	        max = elems[i];
	        idx = i;
	      }
	    }
	    
	    return idx;
	}
	
	public static float max(final float[] elems) {
		return elems[argmax(elems)]; 
	}
	
	public static float[] max(final float[][] matrix) {
		float[] ret = new float[matrix.length];
		
		for (int i = 0; i < matrix.length; i++) {
			ret[i] = max(matrix[i]); 
		}
		
		return ret; 
	}
	
	public static int argmin(final double[] elems) {
		double min = Double.POSITIVE_INFINITY;
	    int idx = 0;
	    
	    for (int i = 0; i < elems.length; i++) {
	      if (elems[i] < min) {
	        min = elems[i];
	        idx = i;
	      }
	    }
	    
	    return idx;
	}
	
	public static double min(final double[] elems) {
		return elems[argmin(elems)]; 
	}
	
	public static double[] min(final double[][] matrix) {
		double[] ret = new double[matrix.length];
		
		for (int i = 0; i < matrix.length; i++) {
			ret[i] = min(matrix[i]); 
		}
		
		return ret; 
	}
	
	public static int argmin(final float[] elems) {
		double min = Double.POSITIVE_INFINITY;
	    int idx = 0;
	    
	    for (int i = 0; i < elems.length; i++) {
	      if (elems[i] < min) {
	        min = elems[i];
	        idx = i;
	      }
	    }
	    
	    return idx;
	}
	
	public static float min(final float[] elems) {
		return elems[argmin(elems)]; 
	}
	
	public static float[] min(final float[][] matrix) {
		float[] ret = new float[matrix.length];
		
		for (int i = 0; i < matrix.length; i++) {
			ret[i] = min(matrix[i]); 
		}
		
		return ret; 
	}
	
	public static double sum(final double[] elems) {
		double ret = 0.0; 
		
		for (double e: elems) {
			ret += e; 
		}
		
		return ret; 
	}
	
	public static float sum(final float[] elems) {
		float ret = 0.0F; 
		
		for (float e: elems) {
			ret += e; 
		}
		
		return ret; 
	}
	
	public static double average(final double[] elems) {
		return sum(elems) / elems.length; 
	}
	
	public static double[] average(final double[][] matrix) {
		double[] avg = new double[matrix.length]; 
		
		for (int i = 0; i < matrix.length; i++) {
			avg[i] = average(matrix[i]); 
		}
		
		return avg; 
	}
	
	public static float average(final float[] elems) {
		return sum(elems) / elems.length; 
	}
	
	public static float[] average(final float[][] matrix) {
		float[] avg = new float[matrix.length]; 
		
		for (int i = 0; i < matrix.length; i++) {
			avg[i] = average(matrix[i]); 
		}
		
		return avg; 
	}
	
	public static double variance(final double[] elems) {
		final double avg = average(elems); 
		double ret = 0.0; 
		
		for (double e: elems) {
			ret += (e - avg) * (e - avg); 
		}
		
		return (ret / elems.length); 
	}
	
	public static double[] variance(final double[][] matrix) {
		double[] var = new double[matrix.length];
		
		for (int i = 0; i < matrix.length; i++) {
			var[i] = variance(matrix[i]); 
		}
		
		return var; 
	}
	
	public static float variance(final float[] elems) {
		final float avg = average(elems); 
		float ret = 0.0F; 
		
		for (float e: elems) {
			ret += (e - avg) * (e - avg); 
		}
		
		return (ret / elems.length); 
	}
	
	public static float[] variance(final float[][] matrix) {
		float[] var = new float[matrix.length];
		
		for (int i = 0; i < matrix.length; i++) {
			var[i] = variance(matrix[i]); 
		}
		
		return var; 
	}
	
	public static double[] add(final double[] elems, final double delta) {
		double[] ret = new double[elems.length];
		
		for (int i = 0; i < elems.length; i++) {
			ret[i] = elems[i] + delta; 
		}
		
		return ret; 
	}
	
	public static double[][] add(final double[][] matrix, final double delta) {
		double[][] ret = new double[matrix.length][];
		
		for (int i = 0; i < matrix.length; i++) {
			ret[i] = new double[matrix[i].length]; 
			for (int j = 0; j < matrix[i].length; j++) {
				ret[i][j] = matrix[i][j] + delta; 
			}
		}
		
		return ret; 
	}
	
	public static float[] add(final float[] elems, final float delta) {
		float[] ret = new float[elems.length];
		
		for (int i = 0; i < elems.length; i++) {
			ret[i] = elems[i] + delta; 
		}
		
		return ret; 
	}
	
	public static float[][] add(final float[][] matrix, final float delta) {
		float[][] ret = new float[matrix.length][];
		
		for (int i = 0; i < matrix.length; i++) {
			ret[i] = new float[matrix[i].length]; 
			for (int j = 0; j < matrix[i].length; j++) {
				ret[i][j] = matrix[i][j] + delta; 
			}
		}
		
		return ret; 
	}
	
	public static double[] add(final double[] a, final double[] b) {
		assert(a.length == b.length);
		
		final int len = a.length; 
		double[] ret = new double[len];
		
		for (int i = 0; i < len; i++) {
			ret[i] = a[i] + b[i]; 
		}
		
		return ret; 
	}
	
	public static float[] add(final float[] a, final float[] b) {
		assert(a.length == b.length);
		
		final int len = a.length; 
		float[] ret = new float[len];
		
		for (int i = 0; i < len; i++) {
			ret[i] = a[i] + b[i]; 
		}
		
		return ret; 
	}
	
	public static double[] product(final double[] elems, final double scale) {
		double[] ret = new double[elems.length]; 
		
		for (int i = 0; i < elems.length; i++) {
			ret[i] = elems[i] * scale; 
		}
		
		return ret; 
	}
	
	public static float[] product(final float[] elems, final float scale) {
		float[] ret = new float[elems.length]; 
		
		for (int i = 0; i < elems.length; i++) {
			ret[i] = elems[i] * scale; 
		}
		
		return ret; 
	}
}
