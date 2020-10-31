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
 * The getFilterStrings() method is awkward because 
 * the List of Strings is a parameter rather than the return type. 
 * This approach allows Glazed Lists to skip creating an ArrayList 
 * each time the method is invoked. We're generally averse to this 
 * kind of micro-optimization, however, in this case this performance 
 * improvement is worthwhile because the method is used heavily while filtering.
 * 
 * @author rwellman
 *
 */
public class DataTextFilterator implements TextFilterator<String[]> {

    // The column indices of TEXT fields; initialized/final in the constructor 
	private List<Integer> textidx = new ArrayList<>();
	
	// The column indices of "user chosen" fields; can expand/contract via the add()/remove() methods
	private List<Integer> filteridx = new ArrayList<>();
	
	public DataTextFilterator(DataHintAware aware) {

        int column = 0;
		final List<DataHint> hints = aware.getDataHints();		
		for (DataHint hint : hints) {
//			if (hint == DataHint.STRING) indices.add(column);
// p.s.  This was originally ONLY the TEXT fields based on the DataHint(s);
// however, it will be fairly common the want to search for numeric values
// (which ARE actually Strings underneath) so this is really just ALL fields.
			if (column > 0) textidx.add(column);

			column++;
		}

	}
	
	@Override
	public void getFilterStrings(List<String> baseList, String[] element) {
	    final List<Integer> listToUse = filteridx.isEmpty() ? textidx : filteridx; 
        for (int ptr : listToUse) {
            baseList.add(element[ptr]); // [ptr-1] the hints are 1 greater than the data array because of the "line number"
            // 6/24/2020:  I think now that I have implemented line number in the grid/data, this needs to be [ptr]
        }           
	}

	public void add(int index) {
		filteridx.add(index);
	}

	public void remove(int index) {
		filteridx.remove(filteridx.indexOf(index));
	}

}
