package cn.ac.istic.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class SetUtils {
	public static <K extends Object> int indexof(final Set<K> set, final K o) {
		final List<K> list = new ArrayList<K>(set); 
		
		return list.indexOf(o); 
	}
	
	public static <K extends Object> K get(final Set<K> set, final int idx) {
		assert(idx >= 0 && idx < set.size()); 
		
		final List<K> list = new ArrayList<K>(set); 
		
		return list.get(idx); 
	}
}
