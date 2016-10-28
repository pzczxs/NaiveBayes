package cn.ac.istic.bayes;

abstract class AbstractDiscreteNB extends AbstractNB {
	protected double[][] featureCount; // size: (#of categories) x (#of features)
	
	protected abstract void count(); 
	
	protected abstract void updateFeatureLogProbability(); 
	
	/*
	 * train naive Bayes classifier according to @train. 
	 * */
	public void train() {
		count(); 
		updateCategoryLogPrior(); 
		updateFeatureLogProbability(); 
	}
}
