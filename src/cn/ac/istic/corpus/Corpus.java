package cn.ac.istic.corpus;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import cn.ac.istic.types.Dictionary;
import cn.ac.istic.utils.CokusRandom;
import cn.ac.istic.utils.RandomSamplers;

/**
 * 
 * @author XU, Shuo (pzczxs@gmail.com)
 * @version 1.0
 * 
 */
public class Corpus implements ICorpus, ISplitCorpus {
	private List<Instance> instances; 
	
	private Dictionary<String> categories = null; 
	private Dictionary<Integer> features = null;
	
	private int[] categoryCount; // size: #of categories
	
	private double[] globalMu; 
	private double[][] localMu; 
	private double[] globalSigma; 
	private double[][] localSigma; 
	
	private int nfold;
	// indexes of instance for each category
	private int[][] indexes; // size: (#of categories) x (*)
	// permutation of the corpus used for splitting
	private int[][] perm; // size: (#of categories) x (*)
	// starting points of the corpus segments
	private int[][] starts; // size: (#of categories) x (*)

	public Corpus(final List<Instance> instances) {
		this.instances = instances; 
	}
	
	public Corpus(final Instance[] instances) {
		this.instances = new ArrayList<Instance>(); 
		for (Instance inst: instances) {
			this.instances.add(inst); 
		}
	}
	
	public Corpus(final String fname) {
		load(fname); 
	}
	
	private void load(final String fname) {
		instances = new ArrayList<Instance>(); 
		
		BufferedReader reader = null; 
		try {
			reader = new BufferedReader(new InputStreamReader(new FileInputStream(fname), "UTF-8"));
			
			int nLine = 0; 
			for (String nextLine; ((nextLine = reader.readLine()) != null); nLine++) {
				final String[] parts = nextLine.trim().split("\\s+"); 
				final String category = parts[0]; 
				
				int[] indexes = new int[parts.length - 1];
				double[] values = new double[parts.length - 1]; 
				for (int i = 1; i < parts.length; i++) {
					String[] node = parts[i].split(":"); 
					if (node.length != 2) {
						throw new IllegalArgumentException("The format of file \"" + fname + "\" is wrong at #line: "+ nLine); 
					}
					indexes[i - 1] = Integer.parseInt(node[0]); 
					values[i - 1] = Double.parseDouble(node[1]); 
					
					/*if (values[i - 1] < 0) {
						throw new IllegalArgumentException("The feature at #line: "+ nLine + " must be non-negative."); 
					}*/
				}
				
				instances.add(new Instance(category, new SparseVector(indexes, values))); 
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
	
	@Override
	public String[] getCategories() {
		final int nInstances = instances.size(); 
		String[] targets = new String[nInstances];
		
		for (int i = 0; i < nInstances; i++) {
			targets[i] = instances.get(i).getCategory(); 
		}
		
		return targets; 
	}
	
	@Override
	public List<Instance> getInstances() {
		return this.instances; 
	}
	
	@Override
	public void setInstance(final int idx, final Instance inst) {
		assert(idx >= 0 && idx < instances.size()); 
		
		this.instances.set(idx, inst); 
		
		categories = null; 
		features = null; 
		categoryCount = null; 
		globalMu = null; 
		localMu = null; 
		globalSigma = null; 
		localSigma = null; 
	}
	
	@Override
	public Instance getInstance(final int idx) {
		assert(idx >= 0 && idx < instances.size()); 
		
		return this.instances.get(idx); 
	}
	
	@Override
	public int getNumInstances() {
		return instances.size(); 
	}
	
	@Override
	public int getNumInstances(final String category) {
		if (categoryCount == null) {
			setCategoryCount(); 
		}
		
		final int categoryId = categories.indexof(category); 
		return categoryCount[categoryId]; 
	}
	
	@Override
	public int[] getNumInstancesByCategory() {
		if (categoryCount == null) {
			setCategoryCount(); 
		}
		
		return categoryCount; 
	}
	
	private void setCategoryCount() {
		final int nCategories = getNumCategories(); 
		
		categoryCount = new int[nCategories];
		
		for (Instance inst: instances) {
			final String category = inst.getCategory(); 
			
			final int categoryId = categories.indexof(category); 
			categoryCount[categoryId]++; 
		}
	}
	
	@Override
	public int getNumCategories() {
		if (categories == null) {
			setCategoryDict(); 
		}
		
		return categories.size(); 
	}
	
	@Override
	public Dictionary<String> getCategoryDict() {
		if (categories == null) {
			setCategoryDict(); 
		}
		
		return categories; 
	}
	
	private void setCategoryDict() {
		categories = new Dictionary<String>(); 
		
		for (Instance inst: instances) {
			categories.add(inst.getCategory()); 
		}
	}
	
	@Override
	public int getNumFeatures() {
		if (features == null) {
			setFeatureDict(); 
		}
		
		return features.size(); 
	}
	
	@Override
	public Dictionary<Integer> getFeatureDict() {
		if (features == null) {
			setFeatureDict(); 
		}
		
		return features; 
	}
	
	private void setFeatureDict() {
		features = new Dictionary<Integer>(); 
		
		for (Instance inst: instances) {
			for (int i = 0; i < inst.getVector().nnz(); i++) {
				features.add(inst.getVector().getIndex(i)); 
			}
		}
	}
	
	private void setGlobalMean() {
		final int nFeatures = getNumFeatures(); 
		final int nInstances = getNumInstances(); 
		
		globalMu = new double[nFeatures]; 
		
		// Mean of all instances
		for (Instance inst: instances) {
			final SparseVector vector = inst.getVector();
			
			for (int j = 0; j < vector.nnz(); j++) {
				final int featureId = getFeatureDict().indexof(vector.getIndex(j)); 
				final double featureVal = vector.getValue(j); 
				
				globalMu[featureId] += featureVal; 
			}
		}
		
		for (int j = 0; j < nFeatures; j++) {
			globalMu[j] /= nInstances; 
		}
	}
	
	@Override
	public double[] getGlobalMean() {
		if (globalMu == null) {
    		setGlobalMean(); 
    	}
    	
    	return globalMu; 
	}
	
	private void setLocalMean() {
		final int nCategories = getNumCategories(); 
		final int nFeatures = getNumFeatures(); 
		final int[] categoryCount = getNumInstancesByCategory(); 
		
		localMu = new double[nCategories][nFeatures]; 
		for (Instance inst: instances) {
			final String category = inst.getCategory(); 
			final SparseVector vector = inst.getVector();
			final int categoryId = getCategoryDict().indexof(category); 
			
			for (int j = 0; j < vector.nnz(); j++) {
				final int featureId = getFeatureDict().indexof(vector.getIndex(j)); 
				final double featureVal = vector.getValue(j); 
				
				localMu[categoryId][featureId] += featureVal; 
			}
		}
		
		for (int c = 0; c < nCategories; c++) {
			for (int j = 0; j < nFeatures; j++) {
				localMu[c][j] /= categoryCount[c]; 
			}
		}
	}
    
	@Override
    public double[][] getLocalMean() {
    	if (localMu == null) {
    		setLocalMean(); 
    	}
    	
    	return localMu; 
    } 
	
	// Variance of all instances
	private void setGlobalVariance() {
		final int nInstances = getNumInstances(); 
		final int nFeatures = getNumFeatures(); 
		final double[] globalMu = getGlobalMean(); 
		
		globalSigma = new double[nFeatures]; 
		for (Instance inst: instances) {
			final SparseVector vector = inst.getVector();

			boolean[] flag = new boolean[nFeatures];
			int[] ids = new int[vector.nnz()];
			for (int j = 0; j < vector.nnz(); j++) {
				final int featureId = getFeatureDict().indexof(vector.getIndex(j));

				ids[j] = featureId;
				flag[featureId] = true;
			}

			for (int j = 0; j < vector.nnz(); j++) {
				final double featureVal = vector.getValue(j);

				globalSigma[ids[j]] += (featureVal - globalMu[ids[j]]) * (featureVal - globalMu[ids[j]]);
			}

			// for zero value (sparse format)
			for (int j = 0; j < nFeatures; j++) {
				if (!flag[j]) {
					globalSigma[j] += globalMu[j] * globalMu[j];
				}
			}
		}

		for (int j = 0; j < nFeatures; j++) {
			globalSigma[j] /= nInstances;
		}
	}
    
	@Override
    public double[] getGloalVariance() {
    	if (globalSigma == null) {
    		setGlobalVariance(); 
    	}
		
		return globalSigma; 
    }
	
	private void setLocalVariance() {
		final int nCategories = getNumCategories(); 
		final int nFeatures = getNumFeatures(); 
		final double[][] localMu = getLocalMean(); 
		final int[] categoryCount = getNumInstancesByCategory(); 
		
		localSigma = new double[nCategories][nFeatures]; 
		for (Instance inst: instances) {
			final String category = inst.getCategory(); 
			final SparseVector vector = inst.getVector();
			final int categoryId = getCategoryDict().indexof(category); 
			
			boolean[] flag = new boolean[nFeatures]; 
			int[] ids = new int[vector.nnz()]; 
			for (int j = 0; j < vector.nnz(); j++) {
				final int featureId = getFeatureDict().indexof(vector.getIndex(j)); 

				ids[j] = featureId; 
				flag[featureId] = true; 
			}
			
			for (int j = 0; j < vector.nnz(); j++) {
				final double featureVal = vector.getValue(j); 
				
				localSigma[categoryId][ids[j]] += (featureVal - localMu[categoryId][ids[j]]) * (featureVal - localMu[categoryId][ids[j]]); 
			}
			
			// for zero value (sparse format)
			for (int j = 0; j < nFeatures; j++) {
				if (!flag[j]) {
					localSigma[categoryId][j] += localMu[categoryId][j] * localMu[categoryId][j]; 
				}
			}
		}
		
		for (int c = 0; c < nCategories; c++) {
			for (int j = 0; j < nFeatures; j++) {
				localSigma[c][j] /= categoryCount[c]; 
			}
		}
	}
    
	@Override
    public double[][] getLocalVariance() {
    	if (localSigma == null) {
    		setLocalVariance(); 
    	}
    	
    	return localSigma; 
    } 
	
	@Override
	public void split(final int nfold, final long seed) {
		final int nInstances = getNumInstances();
		final int nCategories = getNumCategories(); 
		this.nfold = nfold;
		
		indexes = new int[nCategories][]; 
		for (int i = 0; i < nCategories; i++) {
			indexes[i] = new int[getNumInstances(categories.get(i))]; 
		}
		int[] idx = new int[nCategories]; 
		for (int i = 0; i < nInstances; i++) {
			final int categoryId = categories.indexof(instances.get(i).getCategory()); 
			indexes[categoryId][idx[categoryId]] = i; 
			idx[categoryId]++; 
		}

		Random rand = new CokusRandom(seed);
		RandomSamplers rs = new RandomSamplers(rand);
		perm = new int[nCategories][];
		for (int i = 0; i < nCategories; i++) {
			perm[i] = rs.randPerm(indexes[i].length);
		}
		
		starts = new int[nCategories][nfold + 1];
		for (int i = 0; i < nCategories; i++) {
			for (int v = 0; v <= nfold; v++) {
				starts[i][v] = Math.round(indexes[i].length * (v / (float) nfold));
			}
		}
	}

	@Override
	public ICorpus getTrainCorpus(final int split) {
		assert(split < nfold && split >= 0); 
		
		final int nCategories = getNumCategories(); 
		List<Instance> trainInstances = new ArrayList<Instance>(); 
		
		for (int i = 0; i < nCategories; i++) {
			// before test split
			for (int j = 0; j < starts[i][split]; j++) {
				trainInstances.add(instances.get(indexes[i][perm[i][j]]));
			}
			
			// after test split
			for (int j = starts[i][split + 1]; j < indexes[i].length; j++) {
				trainInstances.add(instances.get(indexes[i][perm[i][j]])); 
			}
		}
		
		return new Corpus(trainInstances);
	}

	@Override
	public ICorpus getTestCorpus(final int split) {
		assert(split < nfold && split >= 0);
		
		final int nCategories = getNumCategories(); 
		List<Instance> testInstances = new ArrayList<Instance>(); 
		for (int i = 0; i < nCategories; i++) {
			for (int j = starts[i][split]; j < starts[i][split + 1]; j++) {
				testInstances.add(instances.get(indexes[i][perm[i][j]]));
			}
		}

		return new Corpus(testInstances);
	}	
}
