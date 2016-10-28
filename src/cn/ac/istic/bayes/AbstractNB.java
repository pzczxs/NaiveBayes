package cn.ac.istic.bayes;

import cn.ac.istic.corpus.Corpus;
import cn.ac.istic.utils.MathUtils;

/*
 * Abstract base class for naive Bayes classifier
 * 
 * @author <a href = "mailto: pzczxs@gmail.com">XU, Shuo</a>
 * @version 1.0
 * 
 * */
abstract class AbstractNB {
	protected Corpus train; 
	
	/*
	 * bFitPrior = true: priors are adjusted according to the @train; 
	 * bFitPrior = false: priors are assumed equiprobable categories. 
	 * */
	protected boolean bFitPrior; 
	protected double alpha; 
	
	protected double[] categoryLogPrior; // size: #of categories
	
	protected void updateCategoryLogPrior() {
		final int nCategories = train.getNumCategories(); 
		final int nInstances = train.getNumInstances(); 
		final int[] categoryCount = train.getNumInstancesByCategory(); 
		final double CAlpha = nCategories * alpha; 
		
		categoryLogPrior = new double[nCategories]; 
		for (int i = 0; i < nCategories; i++) {
			categoryLogPrior[i] = bFitPrior? 
					Math.log(categoryCount[i] + alpha) - Math.log(nInstances + CAlpha): 
					-Math.log(nCategories); 
						
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
	protected abstract double[][] jointLogLikelihood(final Corpus test); 
	
	/*
	 * train naive Bayes classifier according to @train. 
	 * */
	public abstract void train(); 
	
	/*
	 * Perform classification on each instance in @test. 
	 * */
	public String[] predict(final Corpus test) {
		final int nInstances = test.getNumInstances(); 
		final double[][] jll = jointLogLikelihood(test); 
		
		String[] results = new String[nInstances]; 
		for (int i = 0; i < nInstances; i++) {
			final int categoryId = MathUtils.argmax(jll[i]); 
			results[i] = train.getCategoryDict().get(categoryId); 
		}
		
		return results; 
	}
	
	/*
	 * Return the log-probability of the instances in @test for each category 
	 * in the model. The columns correspond to the categories in sorted order, 
	 * as they appear in the train.getCategories().  
	 * */
	public double[][] predictLogProbability(final Corpus test) {
		double[][] logProb = jointLogLikelihood(test); 
		
		// normalized by log Pr(x) = log Pr(f_1, ..., f_n), i.e.
		// log Pr(c) + log Pr(x|c) - logPr(x)
		for (int i = 0; i < logProb.length; i++) {
			final double factor = MathUtils.logsumexp(logProb[i]); 
			for (int j = 0; j < logProb[i].length; j++) {
				logProb[i][j] -= factor; 
			}
		}
		
		return logProb; 
	}
	
	/*
	 * Return the probability of the instances in @test for each category in 
	 * the model. The columns correspond to the categories in sorted order, 
	 * as they appear in the train.getCategories().  
	 * */
	public double[][] predictProbability(final Corpus test){
		return MathUtils.exp(predictLogProbability(test)); 
	}
}
