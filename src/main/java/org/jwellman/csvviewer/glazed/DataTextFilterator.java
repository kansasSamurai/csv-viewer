package org.jwellman.csvviewer.glazed;

import java.util.List;
import java.util.ArrayList;

import org.jwellman.swing.misc.DataHint;
import org.jwellman.swing.misc.DataHintAware;

import ca.odell.glazedlists.TextFilterator;

/**
 * An implementation of TextFilterator to support our generic data viewer.
 * 
 * Note: (From the glazed lists website...)
 * The getFilterStrings() method is awkward because the List of Strings is a parameter rather than the return type. This approach allows Glazed Lists to skip creating an ArrayList each time the method is invoked.
 * We're generally averse to this kind of micro-optimization. In this case this performance improvement is worthwhile because the method is used heavily while filtering.
 * 
 * 
 * @author rwellman
 *
 */
public class DataTextFilterator implements TextFilterator {

	private Integer[] textidx;
	
	public DataTextFilterator(DataHintAware aware) {
		final List<Integer> indices = new ArrayList<>();		
		final List<DataHint> hints = aware.getDataHints();
		
		int column = 0;
		for (DataHint hint : hints) {
			if (hint == DataHint.STRING) {
				indices.add(column);
			}
			column++;
		}
		
		textidx = indices.stream().toArray(Integer[] :: new);
	}
	
	@Override
	public void getFilterStrings(List baseList, Object element) {
		final String[] asarray = (String[]) element;
		for (int ptr : textidx) {
			baseList.add(asarray[ptr-1]); // the hints are 1 greater than the data array because of the "line number"
		}
	}

}
