package cn.ac.istic.bayes;

import cn.ac.istic.corpus.Corpus;
import cn.ac.istic.corpus.SparseVector;

/*
 * Gaussian naive Bayes Classifier
 * 
 * @author <a href = "mailto: pzczxs@gmail.com">XU, Shuo</a>
 * @version 1.0
 * 
 * */
public class GaussianNB extends AbstractGaussianNB {
	private double[][] zeroLogValue; // size: (#of categories) x (#of features)
	
	public GaussianNB(final Corpus train, final double alpha, final boolean bFitPrior) {
		if (alpha < 0) {
			throw new IllegalArgumentException("The parameter alpha must be positive. "); 
		}
		
		this.train = train; 
		this.alpha = alpha; 
		this.bFitPrior = bFitPrior; 
	}
	
	private void updateZeroValue() {
		final int nCategories = train.getNumCategories(); 
		final int nFeatures = train.getNumFeatures(); 
		
		zeroLogValue = new double[nCategories][nFeatures]; 
		for (int c = 0; c < nCategories; c++) {
			for (int j = 0; j < nFeatures; j++) {
				zeroLogValue[c][j] =  0.5 * Math.log(2 * Math.PI * sigma[c][j])  
						+ 0.5 * mu[c][j] * mu[c][j] / sigma[c][j]; 
			}
		}
	}
	
	@Override
	public void train() {
		super.train();
		updateZeroValue(); 
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
				jll[i][c] = categoryLogPrior[c]; 
				
				for (int j = 0; j < vector.nnz(); j++) {
					if (ids[j] == -1) { // unseen feature 
						continue; 
					}
					
					final double featureVal = vector.getValue(j); 
					jll[i][c] -= 0.5 * Math.log(2 * Math.PI * sigma[c][ids[j]])  
							+ 0.5 * (featureVal - mu[c][ids[j]]) * (featureVal - mu[c][ids[j]]) / sigma[c][ids[j]]; 
				}
				
				// for zero value (sparse format)
				for (int j = 0; j < nFeatures; j++) {
					if (!flag[j]) {
						jll[i][c] -= zeroLogValue[c][j]; 
					}
				}
			}
		}
		
/*		for (int i = 0; i < nInstances; i++) {
			for (int c = 0; c < nCategories; c++) {
				System.out.print(jll[i][c] + " ");
			}
			System.out.println();
		}*/
		
		return jll; 
	}

}
