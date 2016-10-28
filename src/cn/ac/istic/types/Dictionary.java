package cn.ac.istic.types;

import java.util.List;
import java.util.Set;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;

public class Dictionary<T> {
	private HashBiMap<T, Integer> wordMap; 
	private BiMap<Integer, T> wordMapInv; 
	
	
	public Dictionary() {
		this.wordMap = HashBiMap.create();
		this.wordMapInv = wordMap.inverse(); 
	}
	
	public T get(final int id) {
		return wordMapInv.get(id);
	}
	
	public int indexof(final T o) {
		return wordMap.containsKey(o)? wordMap.get(o): -1;
	}

	public boolean contains(final T o) {
		return wordMap.containsKey(o);
	}

	public int add(final T o){
		if (!contains(o)){
			final int id = wordMap.size();
			wordMap.put(o, id);
			
			return id;
		} 
		
		return indexof(o);
	}
	
	public void add(final List<T> list) {
		for (T o: list) {
			add(o); 
		}
	}
	
	public void add(final T[] v) {
		for (T o: v) {
			add(o); 
		}
	}
	
	public int size() {
		return wordMap.size(); 
	}
	
	public Set<T> keySet() {
		return wordMap.keySet(); 
	}
	
	public void clear() {
		wordMap.clear();
		wordMapInv.clear();
	}
}
