package cn.ac.istic.ui;

import cn.ac.istic.bayes.BernoulliNB;
import cn.ac.istic.corpus.Corpus;
import cn.ac.istic.types.ConfusionMatrix;

public class BernoulliNBUI {
	public static void main(String[] args) {
		//final String baseName = "data/news20/20ng"; 
		final String baseName = "data/WebKB/webkb"; 
		
		final Corpus train = new Corpus(baseName + ".train"); 
		//BernoulliNB classifier = new BernoulliNB(train, 1.0, Math.pow(2.0, -13), true); // 20 newsgroups
		BernoulliNB classifier = new BernoulliNB(train, 1.0, Math.pow(2.0, -6), true); // WebKB
		classifier.train(); 
		
		final Corpus test = new Corpus(baseName + ".test"); 
		final String[] categories = classifier.predict(test); 
		
		final ConfusionMatrix<String> matrix = new ConfusionMatrix<String>(test.getCategories(), categories); 
		System.out.println(matrix);
		
		System.out.println(matrix.classificationReport(1.0));
	}
}
