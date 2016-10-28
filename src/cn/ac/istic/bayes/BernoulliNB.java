package cn.ac.istic.bayes;

import cn.ac.istic.corpus.Corpus;

/*
 * Naive Bayes classifier for multivariate Bernoulli models. 
 * 
 * Like MultinomialNB, this classifier is suitable for discrete data. The difference 
 * is that while MultinomialNB works with occurrence counts, BernoulliNB is designed
 * for binary/boolearn features. 
 * 
 * @author <a href = "mailto: pzczxs@gmail.com">XU, Shuo</a>
 * @version 1.0
 * 
 * */
public class BernoulliNB extends AbstractBernoulliNB {
	private double beta; 
	
	
	public BernoulliNB(final Corpus train, final double alpha, final double beta, final boolean bFitPrior, final double threshold) {
		if (alpha < 0) {
			throw new IllegalArgumentException("The parameter alpha must be positive. "); 
		}
		
		if (beta < 0) {
			throw new IllegalArgumentException("The parameters beta must be positive. "); 
		}
		
		this.train = train; 
		this.alpha = alpha; 
		this.beta = beta; 
		this.bFitPrior = bFitPrior; 
		this.threshold = threshold; 
	}
	
	public BernoulliNB(final Corpus train, final double alpha, final double beta, final boolean bFitPrior) {
		this(train, alpha, beta, bFitPrior, 0.0); 
	}

	@Override
	protected void updateFeatureLogProbability() {
		final int nCategories = train.getNumCategories(); 
		final int nFeatures = train.getNumFeatures(); 
		final int[] categoryCount = train.getNumInstancesByCategory(); 
		final double doubleBeta = 2 * beta; 
		
		featureLogProbability = new double[nCategories][nFeatures]; 
		negativeProbability = new double[nCategories][nFeatures]; 
		for (int c = 0; c < nCategories; c++) {
			final double factor = Math.log(categoryCount[c] + doubleBeta); // only for speeding up
			for (int j = 0; j < nFeatures; j++) {
				featureLogProbability[c][j] = Math.log(featureCount[c][j] + beta) - factor; 
				negativeProbability[c][j] = Math.log(categoryCount[c] - featureCount[c][j] + beta) - factor; 
			}
		}
	}
}
