package cn.ac.istic.bayes;

import cn.ac.istic.corpus.Corpus;
import cn.ac.istic.corpus.SparseVector;

/*
 * Naive Bayes classifier for multinomial models. 
 * 
 * The multinomial naive Bayes classifier is suitable for classification with discrete features 
 * (e.g., word counts for text classification). The multinomial distribution normally requires 
 * integer feature counts. However, in practice, fractional counts such as TF-IDF may also work. 
 * 
 * @author <a href = "mailto: pzczxs@gmail.com">XU, Shuo</a>
 * @version 1.0
 * 
 * */
public class MultinomialNB extends AbstractMultinomialNB {
	private double[][] featureLogProbability; // size: (#of categories) x (#of features)
	
	public MultinomialNB(final Corpus train, final double alpha, final double beta, final boolean bFitPrior) {
		if (alpha < 0) {
			throw new IllegalArgumentException("The parameter alpha must be positive. "); 
		}
		
		if (beta < 0) {
			throw new IllegalArgumentException("The parameter beta must be positive. "); 
		}
		
		this.train = train; 
		this.alpha = alpha; 
		this.beta = beta; 
		this.bFitPrior = bFitPrior; 
	}

	/*
	 * Apply smoothing to raw counts and recompute log probabilities.
	 * */
	@Override
	protected void updateFeatureLogProbability() {
		final int nCategories = train.getNumCategories(); 
		final int nFeatures = train.getNumFeatures(); 
		final double VBeta = nFeatures * beta; 
		
		featureLogProbability = new double[nCategories][nFeatures]; 
		for (int i = 0; i < nCategories; i++) {
			final double factor = Math.log(featureCountSum[i] + VBeta); // only for speeding up
			for (int j = 0; j < nFeatures; j++) {
				featureLogProbability[i][j] = Math.log(featureCount[i][j] + beta) - factor; 
			}
		}
	}

	/*
	 * Compute the unnormalized posterior log probability of the instances in @test
	 * for each category in the model. The columns corresponds to the categories in 
	 * sorted order, as they appear in the train.getCategories(). 
	 *  
	 * I.e. log Pr(c) + log Pr(x|c)
	 * 
	 * @return size: (#of instances) x (#of categories)
	 * */
	@Override
	protected double[][] jointLogLikelihood(final Corpus test) {
		final int nInstances = test.getNumInstances(); 
		final int nCategories = train.getNumCategories(); 
		
		double[][] jll = new double[nInstances][nCategories]; 
		for (int i = 0; i < nInstances; i++) {
			final SparseVector vector = test.getInstance(i).getVector(); 

			for (int j = 0; j < nCategories; j++) {
				jll[i][j] = categoryLogPrior[j]; 
				
				for (int k = 0; k < vector.nnz(); k++) {
					final int featureId = train.getFeatureDict().indexof(vector.getIndex(k)); 
					if (featureId == -1) { // unseen feature 
						continue; 
					}
					
					final double featureVal = vector.getValue(k); 
					jll[i][j] += featureVal * featureLogProbability[j][featureId]; 
				}
			}
		}
		
		return jll; 
	}
}
