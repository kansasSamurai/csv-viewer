package org.jwellman.csvviewer.glazed;

import java.math.BigDecimal;
import java.util.Comparator;

/**
 * A comparator that treats incoming Strings as integers/BigDecimal(s).
 * 
 * Can be used as needed but mainly developed
 * for helping the table compare zero padded values.
 * 
 * @author Rick Wellman
 *
 */
public class BigDecimalComparator implements Comparator<String> {

	@Override
	public int compare(String o1, String o2) {
		final BigDecimal d1 = new BigDecimal(o1);
		final BigDecimal d2 = new BigDecimal(o2);
		return d1.compareTo(d2);
	}

}
