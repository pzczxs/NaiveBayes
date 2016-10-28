package cn.ac.istic.bayes;

import org.apache.commons.math3.special.Gamma;

import cn.ac.istic.corpus.Corpus;
import cn.ac.istic.corpus.SparseVector;

/*
 * Bayesian Gaussian naive Bayes Classifier
 * 
 * @author <a href = "mailto: pzczxs@gmail.com">XU, Shuo</a>
 * @version 1.0
 * 
 * */
public class BayesianGaussianNB extends AbstractGaussianNB {
	private double[] mu0; 
	private double lambda0; 
	private double a0; 
	private double b0; 
	
	private double[][] muFactor; // size: (#of categories x #of features) 
	private double[] lambdaFactor; // size: #of categories
	private double[] aFactor; // size: #of categories
	private double[][] bFactor; // size: (#of categories x #of features) 
	
	private double[][] zeroLogValue; // size: (#of categories) x (#of features)
	
	public BayesianGaussianNB(final Corpus train, final double alpha, final double[] mu, final double lambda, 
			final double a, final double b, final boolean bFitPrior) {
		if (alpha < 0) {
			throw new IllegalArgumentException("The parameter alpha must be positive. "); 
		}
		
		if (a < 0) {
			throw new IllegalArgumentException("The parameter a must be positive. "); 
		}
		
		if (b < 0) {
			throw new IllegalArgumentException("The parameter b must be positive. "); 
		}
		
		this.train = train; 
		this.alpha = alpha; 
		this.mu0 = mu; 
		this.lambda0 = lambda; 
		this.a0 = a; 
		this.b0 = b; 
		this.bFitPrior = bFitPrior; 
	}
	
	@Override
	public void train() {
		super.train();
		
		updatePriors(); 
		updateZeroValue(); 
	}
	
	private void updatePriors() {
		final int nCategories = train.getNumCategories(); 
		final int nFeatures = train.getNumFeatures(); 
		final int[] categoryCount = train.getNumInstancesByCategory(); 
		
		muFactor = new double[nCategories][nFeatures]; 
		lambdaFactor = new double[nCategories];
		aFactor = new double[nCategories]; 
		bFactor = new double[nCategories][nFeatures];
		
		for (int c = 0; c < nCategories; c++) {
			lambdaFactor[c] = lambda0 + categoryCount[c];
			aFactor[c] = a0 + 0.5 * categoryCount[c]; 
			
			for (int j = 0; j < nFeatures; j++) {
				muFactor[c][j] = (lambda0 * mu0[j] + categoryCount[c] * mu[c][j]) / (lambda0 + categoryCount[c]); 
				bFactor[c][j] = b0 + 0.5 *(categoryCount[c] * sigma[c][j] + (lambda0 * categoryCount[c] * (mu[c][j] - mu0[j]) * (mu[c][j] - mu0[j]))/(lambda0 + categoryCount[c])); 
			}
		}
	}
	
	private void updateZeroValue() {
		final int nCategories = train.getNumCategories(); 
		final int nFeatures = train.getNumFeatures(); 
		
		zeroLogValue = new double[nCategories][nFeatures]; 
		for (int c = 0; c < nCategories; c++) {
			for (int j = 0; j < nFeatures; j++) {
				zeroLogValue[c][j] = aFactor[c] * Math.log(bFactor[c][j]) 
						- (aFactor[c] + 0.5) * Math.log(bFactor[c][j] + 0.5 * lambdaFactor[c] * muFactor[c][j] * muFactor[c][j] / (lambdaFactor[c] + 1.0));
			}
		}
	}
	
	@Override
	protected double[][] jointLogLikelihood(final Corpus test) {
		final int nInstances = test.getNumInstances(); 
		final int nCategories = train.getNumCategories(); 
		final int nFeatures = train.getNumFeatures(); 
		
		double[][] jll = new double[nInstances][nCategories]; 
		for (int i = 0; i < nInstances; i++) {
			final SparseVector vector = test.getInstance(i).getVector(); 
			
			boolean[] flag = new boolean[nFeatures]; 
			int[] ids = new int[vector.nnz()]; 
			for (int j = 0; j < vector.nnz(); j++) {
				final int featureId = train.getFeatureDict().indexof(vector.getIndex(j)); 

				ids[j] = featureId; 
				if (featureId != -1) {
					flag[featureId] = true; 
				}
			}

			for (int c = 0; c < nCategories; c++) {
				final double factor = nFeatures * (Gamma.logGamma(aFactor[c] + 0.5) - Gamma.logGamma(aFactor[c]) 
						+ 0.5 * (Math.log(lambdaFactor[c]) - Math.log(lambdaFactor[c] + 1.0))); 
				jll[i][c] = categoryLogPrior[c] + factor; 
				
				for (int j = 0; j < vector.nnz(); j++) {
					if (ids[j] == -1) { // unseen feature 
						continue; 
					}
					
					final double featureVal = vector.getValue(j); 
					jll[i][c] += aFactor[c] * Math.log(bFactor[c][ids[j]]) 
							- (aFactor[c] + 0.5) * Math.log(bFactor[c][ids[j]] + 0.5 * lambdaFactor[c] * (featureVal - muFactor[c][ids[j]]) * (featureVal - muFactor[c][ids[j]]) / (lambdaFactor[c] + 1.0)); 
				}
				
				// for zero value (sparse format)
				for (int j = 0; j < nFeatures; j++) {
					if (!flag[j]) {
						jll[i][c] += zeroLogValue[c][j];
					}
				}
			}
		}
		
		return jll; 
	}
}
