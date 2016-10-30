package cn.ac.istic.converters;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import cn.ac.istic.corpus.Corpus;
import cn.ac.istic.corpus.Instance;
import cn.ac.istic.corpus.SparseVector;
import cn.ac.istic.types.Dictionary;

public class Converter {
	private Corpus corpusTrain; 
	private Corpus corpusTest; 
	
	private Dictionary<String> dict; 
	
	public Converter(final String fnameTrain, final String fnameTest) {
		this.dict = new Dictionary<String>(); 
		buildDict(fnameTrain);
		buildDict(fnameTest); 
		
		System.out.println("#of words: " + dict.size());
		
		corpusTrain = load(fnameTrain); 
		corpusTest = load(fnameTest); 
	}
	
	private void buildDict(final String fname) {
		BufferedReader reader = null; 
		
		try {
			reader = new BufferedReader(new InputStreamReader(new FileInputStream(fname), "UTF-8"));
			
			for (String nextLine; ((nextLine = reader.readLine()) != null); ) {
				final String[] parts = nextLine.trim().split("\\s+"); 
				
				for (int i = 1; i < parts.length; i++) {
					dict.add(parts[i].trim()); 
				}
			}
			
		} catch (Exception e) {
			System.err.println("Error while reading corpus:" + e.getMessage());
			e.printStackTrace();
		} finally {
			try {
				reader.close(); 
			} catch(IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	private Corpus load(final String fname) {
		List<Instance> corpus = new ArrayList<Instance>(); 
		
		BufferedReader reader = null; 
		try {
			reader = new BufferedReader(new InputStreamReader(new FileInputStream(fname), "UTF-8"));
			
			for (String nextLine; ((nextLine = reader.readLine()) != null); ) {
				final String[] parts = nextLine.trim().split("\\s+"); 
				
				final String category = parts[0].trim(); 
				
				Map<Integer, Double> map = new TreeMap<Integer, Double>(); 
				for (int i = 1; i < parts.length; i++) {
					final int wordId = dict.indexof(parts[i].trim()); 
					
					if (map.containsKey(wordId)) {
						map.put(wordId, map.get(wordId) + 1.0); 
					} else {
						map.put(wordId, 1.0); 
					}
				}
				
				final int length = map.size(); 
				int[] indexes = new int[length]; 
				double[] values = new double[length]; 
				Iterator<Map.Entry<Integer, Double>> it = map.entrySet().iterator(); 
				for (int i = 0; it.hasNext(); i++) {
					final Map.Entry<Integer, Double> e = it.next(); 
					indexes[i] = e.getKey(); 
					values[i] = e.getValue(); 
				}
				
				corpus.add(new Instance(category, new SparseVector(indexes, values))); 
			}
			
		} catch (Exception e) {
			System.err.println("Error while reading corpus:" + e.getMessage());
			e.printStackTrace();
		} finally {
			try {
				reader.close(); 
			} catch(IOException e) {
				e.printStackTrace();
			}
		}
		
		return new Corpus(corpus); 
	}
	
	/*
	 * calculate the document frequency
	 * */
	private int[] documentFrequency(final Corpus trainCorpus, final Corpus testCorpus) {
		final int V = dict.size(); 
		int[] df = new int[V]; 
		
		for (Instance inst: trainCorpus.getInstances()) {
			final SparseVector sv = inst.getVector(); 
			
			for (int i = 0; i < sv.nnz(); i++) {
				if (sv.getValue(i) > 0) {
					df[sv.getIndex(i)]++; 
				}
			}
		}
		
		for (Instance inst: testCorpus.getInstances()) {
			final SparseVector sv = inst.getVector(); 
			
			for (int i = 0; i < sv.nnz(); i++) {
				if (sv.getValue(i) > 0) {
					df[sv.getIndex(i)]++; 
				}
			}
		}
		
		return df; 
	}
	
	/*
	 * TF-IDF and normalize to unit length
	 * 
	 * ln(1 + TF) * log2(#docs / DF)
	 * */
	
	private Corpus TfidfVectorizer(Corpus corpus, final int[] df, final int M) {
		for (int m = 0; m < corpus.getNumInstances(); m++) {
			final String category = corpus.getInstance(m).getCategory(); 
			SparseVector sv = corpus.getInstance(m).getVector(); 
			
			for (int i = 0; i < sv.nnz(); i++) {
				sv.setValue(i, Math.log(1.0 + sv.getValue(i)) * Math.log((double)M / df[sv.getIndex(i)]) / Math.log(2.0)); 
			}
			
			double norm = sv.norm(); 
			for (int i = 0; i < sv.nnz(); i++) {
				sv.setValue(i, sv.getValue(i)/norm); 
			}
			
			corpus.setInstance(m, new Instance(category, sv)); 
		}
		
		return corpus; 
	}
	
	private void  print(final Corpus corpus, final PrintStream out) {
		for (Instance inst: corpus.getInstances()) {
			out.println(inst.getCategory() + " " + inst.getVector()); 
		}
	}
	
	public void convertWithTf(final PrintStream outTrain, final PrintStream outTest) {
		print(corpusTrain, outTrain); 
		
		print(corpusTest, outTest); 
	} 

	public void convertWithTfidf(final PrintStream outTrain, final PrintStream outTest) {
		final int[] df = documentFrequency(corpusTrain, corpusTest); 
		final int M = corpusTrain.getNumInstances() + corpusTest.getNumInstances(); 
		
		// train data set: TF-IDF and normalize to unit length
		corpusTrain = TfidfVectorizer(corpusTrain, df, M); 
		
		// test data set: TF-IDF and normalize to unit length
		corpusTest = TfidfVectorizer(corpusTest, df, M); 
		
		print(corpusTrain, outTrain); 
		
		print(corpusTest, outTest); 
	}
	
	public static void main(String[] args) throws FileNotFoundException {
		final String[] baseNames = {
				"data/news20/20ng", 
				"data/WebKB/webkb"
				}; 
		
		for (int i = 0; i < baseNames.length; i++) {
			Converter converter = new Converter(baseNames[i] + "-train-stemmed.txt", baseNames[i] + "-test-stemmed.txt"); 
			converter.convertWithTf(new PrintStream(baseNames[i] + ".train"), new PrintStream(baseNames[i] + ".test"));
			converter.convertWithTfidf(new PrintStream(baseNames[i] + ".scale.train"), new PrintStream(baseNames[i] + ".scale.test"));
		}
		
		System.out.println("done.");
	}
}
