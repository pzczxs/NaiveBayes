package cn.ac.istic.cv;

import cn.ac.istic.bayes.BayesianBernoulliNB;
import cn.ac.istic.corpus.Corpus;
import cn.ac.istic.types.ConfusionMatrix;
import cn.ac.istic.utils.MathUtils;

public class BayesianBernoulliNBCrossValidationUI {
	public static void main(String[] args) {
		final Corpus corpus = new Corpus("data/news20/news20.train"); 
		final int nfold = 10; 
		final double[] abs = {-15, -14, -13, -12, -11, -10, -9, -8, -7, -6, -5, -4, -3, -2, -1, 0, 1}; 		
		
		corpus.split(nfold, 56567651L);
		for (double ab: abs) {
			System.out.print(ab);
			for (int v = 0; v < nfold; v++) {
				final Corpus train = (Corpus) corpus.getTrainCorpus(v);
				BayesianBernoulliNB classifier = new BayesianBernoulliNB(train, 2.0, Math.pow(2.0, ab), Math.pow(2.0, ab), true);
				classifier.train();

				final Corpus test = (Corpus) corpus.getTestCorpus(v);
				final String[] categories = classifier.predict(test);
				final ConfusionMatrix<String> matrix = new ConfusionMatrix<String>(test.getCategories(), categories);
				System.out.print(" " + MathUtils.average(matrix.getFscore()));
			}
			System.out.println();
		}
	}
}
