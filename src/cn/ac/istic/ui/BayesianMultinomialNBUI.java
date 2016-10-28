package cn.ac.istic.ui;

import cn.ac.istic.bayes.BayesianMultinomialNB;
import cn.ac.istic.corpus.Corpus;
import cn.ac.istic.types.ConfusionMatrix;

public class BayesianMultinomialNBUI {
	public static void main(String[] args) {
		//final String baseName = "data/WebKB/webkb"; 
		final String baseName = "data/WebKB/webkb"; 
		
		final Corpus train = new Corpus(baseName + ".train"); 
		//BayesianMultinomialNB classifier = new BayesianMultinomialNB(train, 2.0, Math.pow(2.0, -4), true); // 20 newsgroups
		BayesianMultinomialNB classifier = new BayesianMultinomialNB(train, 2.0, Math.pow(2.0, -2), true); // WebKB
		classifier.train(); 
		
		final Corpus test = new Corpus(baseName + ".test"); 
		final String[] categories = classifier.predict(test); 
		
		System.out.println("#of features: " + train.getNumFeatures());
		System.out.println("#of instances in training data: " + train.getNumInstances());
		System.out.println("#of instances in testing data: " + test.getNumInstances());
		
		final ConfusionMatrix<String> matrix = new ConfusionMatrix<String>(test.getCategories(), categories); 
		System.out.println(matrix);
		
		System.out.println(matrix.classificationReport(1.0));
	}
}
