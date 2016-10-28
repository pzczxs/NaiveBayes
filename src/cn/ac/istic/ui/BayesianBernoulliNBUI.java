package cn.ac.istic.ui;

import cn.ac.istic.bayes.BayesianBernoulliNB;
import cn.ac.istic.corpus.Corpus;
import cn.ac.istic.types.ConfusionMatrix;

public class BayesianBernoulliNBUI {
	public static void main(String[] args) {
		final String baseName = "data/news20/news20"; 
		
		final Corpus train = new Corpus(baseName + ".train"); 
		BayesianBernoulliNB classifier = new BayesianBernoulliNB(train, 1.0, Math.pow(2.0, -10), Math.pow(2.0, -10), true);
		classifier.train(); 
		
		final Corpus test = new Corpus(baseName + ".test"); 
		final String[] categories = classifier.predict(test); 
		
		final ConfusionMatrix<String> matrix = new ConfusionMatrix<String>(test.getCategories(), categories); 
		System.out.println(matrix);
		
		System.out.println(matrix.classificationReport(1.0));
	}
}
