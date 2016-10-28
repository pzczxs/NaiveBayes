package cn.ac.istic.bayes;

import cn.ac.istic.utils.MathUtils;

abstract class AbstractGaussianNB extends AbstractNB {
	protected double[][] mu; // size: (#of categories) x (#of features)
	protected double[][] sigma; // size: (#of categories) x (#of features)
	
	private double epsilon = 1e-9; 
	
	private void updateEpsilon() {
		double[] variance = train.getGloalVariance(); 
			
		epsilon *= Math.sqrt(MathUtils.max(variance)); 
	}
	
	/*
	 * Compute Gaussian mean and variance
	 * */
	private void updateMuVariance() {
		mu = train.getLocalMean(); 
		sigma = train.getLocalVariance(); 
		
		/*
		 * If the ratio of data variance between dimensions is too small, it 
		 * will cause numerical errors. To address this, we artificially boost
		 * the variance by epsilon, a small fraction of the standard deviation
		 * of the largest dimension. 
		 * */
		updateEpsilon(); 
		sigma = MathUtils.add(sigma, epsilon); 
		
	}
	
	@Override
	public void train() {
		updateCategoryLogPrior(); 
		updateMuVariance(); 
	}
}
