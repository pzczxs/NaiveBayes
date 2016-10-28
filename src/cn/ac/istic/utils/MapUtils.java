package cn.ac.istic.utils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class MapUtils {
	public static <K, V extends Comparable<? super V>> Map<K, V> sortByValue(Map<K, V> map) {
		List<Map.Entry<K, V>> list = new LinkedList<Map.Entry<K, V>>(map.entrySet());
		
		Collections.sort(list, new Comparator<Map.Entry<K, V>>() {
			public int compare(Map.Entry<K, V> o1, Map.Entry<K, V> o2) {
				return (o1.getValue()).compareTo(o2.getValue());
			}
		});

		Map<K, V> result = new LinkedHashMap<K, V>();
		for (Map.Entry<K, V> entry: list) {
			result.put(entry.getKey(), entry.getValue());
		}

		return result;
	}
	
	/*public static K argmax(final Map<K, V> map) {
		return map.entrySet().stream().max((e1, e2) -> e1.getValue() > e2.getValue() ? 1 : -1).get().getKey(); 
	}*/
	
	public static void main(String[] args) {
		Map<String, Double> test = new HashMap<String, Double>(); 
		test.put("-1", 0.4); 
		test.put("1", 0.5); 
		test.put("++1", 0.6);

		Set<String> set = new LinkedHashSet<String>(); 
		set.add("bb"); 
		set.add("ac"); 
		set.add("aa"); 
		set.add("123"); 
		System.out.println(new ArrayList<String>(set)); 
		
		/*String keyOfMaxValue = Collections.max(
				test.entrySet(), 
				new Comparator<Map.Entry<String, Double>>(){
					@Override
					public int compare(<Map.Entry<String, Double> o1, Map.Entry<String, Double> o2>) {
						return Double.compare(o1.getValue(), o2.getValue()); 
					}
				}).getKey(); */
	}
}
