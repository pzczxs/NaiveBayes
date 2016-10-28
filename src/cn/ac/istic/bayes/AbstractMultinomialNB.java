package cn.ac.istic.bayes;

import cn.ac.istic.corpus.Instance;
import cn.ac.istic.corpus.SparseVector;

abstract class AbstractMultinomialNB extends AbstractDiscreteNB {
	protected double[] featureCountSum; // size: #of categories
	protected double beta; 
	
	/*
	 * count the feature occurrences. 
	 * */
	@Override
	protected void count() {
		final int nCategories = train.getNumCategories(); 
		final int nFeatures = train.getNumFeatures(); 
		
		featureCount = new double[nCategories][nFeatures]; 
		featureCountSum = new double[nCategories]; 
		
		for (Instance inst: train.getInstances()) {
			final String category = inst.getCategory(); 
			final SparseVector vector = inst.getVector();
			
			final int categoryId = train.getCategoryDict().indexof(category); 
			
			for (int i = 0; i < vector.nnz(); i++) {
				final int featureId = train.getFeatureDict().indexof(vector.getIndex(i)); 
				
				featureCount[categoryId][featureId] += vector.getValue(i); 
				featureCountSum[categoryId] += vector.getValue(i); 
			}
		}
	}
}
