package cn.ac.istic.ui;

import cn.ac.istic.bayes.BayesianGaussianNB;
import cn.ac.istic.corpus.Corpus;
import cn.ac.istic.types.ConfusionMatrix;

public class BayesianGaussianNBUI {
	public static void main(String[] args) {
		//final String baseName = "data/news20/20ng"; 
		final String baseName = "data/WebKB/webkb";
		
		final Corpus train = new Corpus(baseName + ".scale.train"); 
		//BayesianGaussianNB classifier = new BayesianGaussianNB(train, 2.0, train.getGlobalMean(), 1.0, Math.pow(2.0, -12), Math.pow(2.0, -12), true); // 20 newsgroups
		BayesianGaussianNB classifier = new BayesianGaussianNB(train, 2.0, train.getGlobalMean(), 1.0, Math.pow(2.0, -7), Math.pow(2.0, -7), true); // WebKB
		classifier.train(); 
		
		final Corpus test = new Corpus(baseName + ".scale.test"); 
		final String[] categories = classifier.predict(test); 
		
		final ConfusionMatrix<String> matrix = new ConfusionMatrix<String>(test.getCategories(), categories); 
		System.out.println(matrix);
		
		System.out.println(matrix.classificationReport(1.0));
	}
}
