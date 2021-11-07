package org.jwellman.csvviewer.glazed;

import java.util.Comparator;

/**
 * This really does not compare data generically; rather it compares the line numbers
 * as they are the default sort order for the table used here.
 * 
 * @author rwellman
 *
 */
@SuppressWarnings("rawtypes")
public class DataComparator implements Comparator {

	@Override
	public int compare(Object o1, Object o2) {
		String[] objectA = (String[]) o1;
		String[] objectB = (String[]) o2;
		
		int comparison = 0;
			// index 0 is the line number (once I implement line numbers in the underlying data)
			Integer intA = Integer.parseInt( objectA[0] );
			Integer intB = Integer.parseInt( objectB[0] );
			comparison = intA - intB;

		return comparison;
	}

}
