package cn.ac.istic.corpus;

import java.util.List;

import cn.ac.istic.types.Dictionary;

public interface ICorpus {
    
    int getNumInstances();
    
    int getNumInstances(final String category); 
    
    int[] getNumInstancesByCategory(); 
    
    int getNumCategories(); 
    
    int getNumFeatures();
    
    Dictionary<String> getCategoryDict(); 
    
    Dictionary<Integer> getFeatureDict(); 
    
    List<Instance> getInstances(); 
   
    Instance getInstance(final int idx); 
    
    void setInstance(final int idx, final Instance inst); 
   
    String[] getCategories(); 
    
    double[] getGlobalMean(); 
    
    double[][] getLocalMean(); 
    
    double[] getGloalVariance();
    
    double[][] getLocalVariance(); 
}
