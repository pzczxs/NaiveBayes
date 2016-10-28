package cn.ac.istic.bayes;

import cn.ac.istic.corpus.Corpus;
import cn.ac.istic.corpus.Instance;
import cn.ac.istic.corpus.SparseVector;

abstract class AbstractBernoulliNB extends AbstractDiscreteNB { 
	protected double threshold; 
	protected double[][] featureLogProbability; // size: (#of categories) x (#of features)
	protected double[][] negativeProbability; // size: (#of categories) x (#of features)
	
	@Override
	protected void count() {
		final int nCategories = train.getNumCategories(); 
		final int nFeatures = train.getNumFeatures(); 
		
		featureCount = new double[nCategories][nFeatures]; 
		
		for (Instance inst: train.getInstances()) {
			final String category = inst.getCategory(); 
			final SparseVector vector = inst.getVector();
			
			final int categoryId = train.getCategoryDict().indexof(category); 
			
			boolean[] flag = new boolean[nFeatures]; 
			
			for (int i = 0; i < vector.nnz(); i++) {
				final int featureId = train.getFeatureDict().indexof(vector.getIndex(i)); 
				
				if (vector.getValue(i) > threshold) {
					featureCount[categoryId][featureId]++; 
				} 
				
				flag[featureId] = true; 
			}
			
			// for zero value (sparse format)
			for (int j = 0; j < nFeatures; j++) {
				if (!flag[j] && 0 > threshold) {
					featureCount[categoryId][j]++; 
				}
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
				jll[i][c] = categoryLogPrior[c]; 
				
				for (int j = 0; j < vector.nnz(); j++) {
					if (ids[j] == -1) { // unseen feature 
						continue; 
					}
					
					jll[i][c] += (vector.getValue(j) > threshold)? featureLogProbability[c][ids[j]]: negativeProbability[c][ids[j]]; 
				}
				
				// for zero value (sparse format)
				for (int j = 0; j < nFeatures; j++) {
					if (!flag[j]) {
						jll[i][c] += (0 > threshold)? featureLogProbability[c][j]: negativeProbability[c][j]; 
					}
				}
			}
		}
		
		return jll; 
	}
}
