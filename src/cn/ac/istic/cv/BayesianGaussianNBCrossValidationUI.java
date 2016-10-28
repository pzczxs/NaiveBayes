package cn.ac.istic.cv;

import cn.ac.istic.bayes.BayesianGaussianNB;
import cn.ac.istic.corpus.Corpus;
import cn.ac.istic.types.ConfusionMatrix;
import cn.ac.istic.utils.MathUtils;

public class BayesianGaussianNBCrossValidationUI {
	public static void main(String[] args) {
		final Corpus corpus = new Corpus("data/WebKB/webkb.scale.train"); 
		final int nfold = 10; 
		//final double[] betas = {-20, -19, -18, -17, -16, -15, -14, -13, -12, -11, -10, -9};
		final double[] betas = {-8, -7, -6, -5, -4, -3, -2, -1, 0, 1};
		
		corpus.split(nfold, 56567651L);
		for (double beta : betas) {
			// System.out.print(mu + " " + beta);
			System.out.print(beta);
			for (int v = 0; v < nfold; v++) {
				final Corpus train = (Corpus) corpus.getTrainCorpus(v);
				BayesianGaussianNB classifier = new BayesianGaussianNB(train, 1.0, corpus.getGlobalMean(), 1.0,
						Math.pow(2.0, beta), Math.pow(2.0, beta), true);
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
