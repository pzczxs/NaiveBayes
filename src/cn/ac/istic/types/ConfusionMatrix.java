package cn.ac.istic.types;

import java.util.List;

import cn.ac.istic.utils.DoubleFormat;
import cn.ac.istic.utils.MathUtils;

public class ConfusionMatrix<T> {
	private Dictionary<T> dict; 
	private int[][] matrix; 
	
	public ConfusionMatrix(final T[] yTrue, final T[] yPrediction) {
		assert(yTrue.length == yPrediction.length);
		
		dict = new Dictionary<T>(); 
		dict.add(yTrue);
		final int nLabels = dict.size(); 
		matrix = new int[nLabels][nLabels];
		for (int i = 0; i < yTrue.length; i++) {
			matrix[dict.indexof(yTrue[i])][dict.indexof(yPrediction[i])]++; 
		}
	}
	
	public ConfusionMatrix(final T[] yTrue, final T[] yPrediction, final Dictionary<T> dict) { 
		this(yTrue, yPrediction);
		
		this.dict = dict; 
	}
	
	public ConfusionMatrix(final List<T> yTrue, final List<T> yPrediction) {
		assert(yTrue.size() == yPrediction.size());
		
		dict.add(yTrue);
		final int nLabels = dict.size(); 
		matrix = new int[nLabels][nLabels];
		for (int i = 0; i < yTrue.size(); i++) {
			matrix[dict.indexof(yTrue.get(i))][dict.indexof(yPrediction.get(i))]++; 
		}
	}
	
	public ConfusionMatrix(final List<T> yTrue, final List<T> yPrediction, final Dictionary<T> dict) {
		this(yTrue, yPrediction);
		
		this.dict = dict; 
	}
	
	public double[] getRecall() {
		final int nLabels = dict.size(); 
		double[] recall = new double[nLabels];
		
		for (int i = 0; i < nLabels; i++) {
			int sum = 0; 
			for (int j = 0; j < nLabels; j++) {
				sum += matrix[i][j]; 
			}
			
			recall[i] = (sum > 0)? (double)matrix[i][i] / (double)sum: 0.0; 
		}
		
		return recall; 
	}
	
	public double[] getPrecision() {
		final int nLabels = dict.size(); 
		double[] precision = new double[nLabels];
		
		for (int i = 0; i < nLabels; i++) {
			int sum = 0; 
			for (int j = 0; j < nLabels; j++) {
				sum += matrix[j][i]; 
			}
			
			precision[i] = (sum > 0)? (double)matrix[i][i] / (double)sum: 0.0; 
		}
		
		return precision; 
	}
	
	public double[] getFscore(final double beta) {
		final double[] p = getPrecision();
		final double[] r = getRecall(); 
		final double beta2 = beta * beta; 
		
		final int nLabels = dict.size(); 
		final double[] f = new double[nLabels];
		
		for (int i = 0; i < nLabels; i++) {
			f[i] = (1 + beta2) * p[i] * r[i] / (beta2 * p[i] + r[i]); 
			
			if (Double.isNaN(f[i])) {
				f[i] = 0.0; 
			}
		}
		
		return f; 
	}
	
	public double[] getFscore() {
		return getFscore(1.0); 
	}
	
	public String classificationReport(final double beta) {
		final int ndigits = 4; 
		final double[] p = getPrecision(); 
		final double[] r = getRecall(); 
		final double[] f = getFscore(beta); 
		
		StringBuilder sb = new StringBuilder(); 
		
		sb.append("\tprecision\trecall\tf1-score\n"); 
		for (int i = 0; i < dict.size(); i++) {
			sb.append(dict.get(i) + "\t")
			  .append(DoubleFormat.format(p[i], ndigits) + "\t")
			  .append(DoubleFormat.format(r[i], ndigits) + "\t")
			  .append(DoubleFormat.format(f[i], ndigits) + "\n"); 
		}
		sb.append("average\t")
		  .append(DoubleFormat.format(MathUtils.average(p), ndigits) + "\t")
		  .append(DoubleFormat.format(MathUtils.average(r), ndigits) + "\t")
		  .append(DoubleFormat.format(MathUtils.average(f), ndigits) + "\n"); 
		
		return sb.toString(); 
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder(); 
		
		for (int i = 0; i < matrix.length; i++) {
			for (int j = 0; j < matrix[i].length; j++) {
				sb.append(matrix[i][j] + "\t"); 
			}
			sb.append("\n"); 
		}
		
		return sb.toString();
	}
}
