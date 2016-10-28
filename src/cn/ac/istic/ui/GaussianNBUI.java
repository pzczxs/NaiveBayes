package cn.ac.istic.ui;

import cn.ac.istic.bayes.GaussianNB;
import cn.ac.istic.corpus.Corpus;
import cn.ac.istic.types.ConfusionMatrix;

public class GaussianNBUI {
	public static void main(String[] args) {
		//final String baseName = "data/news20/20ng";
		final String baseName = "data/WebKB/webkb"; 
		
		final Corpus train = new Corpus(baseName + ".scale.train"); 
		GaussianNB classifier = new GaussianNB(train, 1.0, true);
		classifier.train(); 
		
		final Corpus test = new Corpus(baseName + ".scale.test"); 
		final String[] categories = classifier.predict(test); 
		
		final ConfusionMatrix<String> matrix = new ConfusionMatrix<String>(test.getCategories(), categories); 
		System.out.println(matrix);
		
		System.out.println(matrix.classificationReport(1.0));
	}
}
