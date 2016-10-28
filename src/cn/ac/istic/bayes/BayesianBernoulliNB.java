package cn.ac.istic.bayes;

import cn.ac.istic.corpus.Corpus;

/*
 * Bayesian Naive Bayes classifier for multivariate Bernoulli models. 
 * 
 * Like BayesianMultinomialNB, this classifier is suitable for discrete data. The 
 * difference is that while BayesianMultinomialNB works with occurrence counts, 
 * BayesianBernoulliNB is designed for binary/boolearn features. 
 * 
 * @author <a href = "mailto: pzczxs@gmail.com">XU, Shuo</a>
 * @version 1.0
 * 
 * */
public class BayesianBernoulliNB extends AbstractBernoulliNB {
	private double a; 
	private double b; 
	
	public BayesianBernoulliNB(final Corpus train, final double alpha, final double a, final double b, final boolean bFitPrior, final double threshold) {
		if (alpha < 0) {
			throw new IllegalArgumentException("The parameter alpha must be positive. "); 
		}
		
		if (a < 0 || b < 0) {
			throw new IllegalArgumentException("The parameters a and b must be positive. "); 
		}
		
		this.train = train; 
		this.alpha = alpha; 
		this.a = a; 
		this.b = b; 
		this.bFitPrior = bFitPrior; 
		this.threshold = threshold; 
	}
	
	public BayesianBernoulliNB(final Corpus train, final double alpha, final double a, final double b, final boolean bFitPrior) {
		this(train, alpha, a, b, bFitPrior, 0.0); 
	}
	
	@Override
	protected void updateFeatureLogProbability() {
		final int nCategories = train.getNumCategories(); 
		final int nFeatures = train.getNumFeatures(); 
		final int[] categoryCount = train.getNumInstancesByCategory(); 
		final double abPlus = a + b; 
		
		featureLogProbability = new double[nCategories][nFeatures]; 
		negativeProbability = new double[nCategories][nFeatures]; 
		for (int i = 0; i < nCategories; i++) {
			final double factor = Math.log(categoryCount[i] + abPlus); // only for speeding up
			for (int j = 0; j < nFeatures; j++) {
				featureLogProbability[i][j] = Math.log(featureCount[i][j] + a) - factor; 
				negativeProbability[i][j] = Math.log(categoryCount[i] - featureCount[i][j] + b) - factor; 
			}
		}
	}
}
