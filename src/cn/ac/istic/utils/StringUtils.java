package cn.ac.istic.utils;

public class StringUtils {
	public static String trimTrailingZeros(final String number) {
		if (!number.contains(".")) {
			return number; 
		}
		
		return number.replaceAll("\\.?0*$", ""); 
	}
	
	public static String trimTrailingZeros(final double d) {
		if (d == (long) d) {
			return String.format("%d", (long)d); 
		} else {
			return String.format("%s", d); 
		}
	}
	
	public static double[] parseDoubleArray(final String str, final String regex) {
		final String parts[] = str.split(regex); 
		double[] vals = new double[parts.length];
		
		for (int i = 0; i < parts.length; i++) {
			vals[i] = Double.parseDouble(parts[i]); 
		}
		
		return vals; 
	}
}
