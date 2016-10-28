package cn.ac.istic.ui;

import cn.ac.istic.bayes.MultinomialNB;
import cn.ac.istic.corpus.Corpus;
import cn.ac.istic.types.ConfusionMatrix;

public class MultinomialNBUI {
	public static void main(String[] args) {
		//final String baseName = "data/news20/20ng"; 
		final String baseName = "data/WebKB/webkb"; 
		
		final Corpus train = new Corpus(baseName + ".scale.train"); 
		// MultinomialNB classifier = new MultinomialNB(train, 1.0, Math.pow(2.0, -6), true); // 20 newsgroups
		MultinomialNB classifier = new MultinomialNB(train, 1.0, Math.pow(2.0, -3), true); // WebKB
		classifier.train(); 
		
		final Corpus test = new Corpus(baseName + ".scale.test"); 
		final String[] categories = classifier.predict(test); 
		
		final ConfusionMatrix<String> matrix = new ConfusionMatrix<String>(test.getCategories(), categories); 
		System.out.println(matrix);
		
		System.out.println(matrix.classificationReport(1.0));
	}
}
