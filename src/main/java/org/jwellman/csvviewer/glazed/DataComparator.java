package org.jwellman.csvviewer.glazed;

import java.util.Comparator;

/**
 * 
 * @author rwellman
 *
 */
public class DataComparator implements Comparator {

	@Override
	public int compare(Object o1, Object o2) {
		String[] objectA = (String[]) o1;
		String[] objectB = (String[]) o2;
		
		int comparison = 0;
		try {
			// index 0 is the line number (once I implement line numbers in the underlying data)
			Integer intA = Integer.parseInt( objectA[0] );
			Integer intB = Integer.parseInt( objectB[0] );
			comparison = intA - intB;
		} catch (Throwable t) {
			// If it is not a number, it's a string
			comparison = objectA[0].compareTo(objectB[0]);
		}
		
		return comparison;
	}

}
