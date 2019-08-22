# scratchpad.md
As the filename implies, this is a scratchpad... its contents will vary over time
and are to be considered unimportant to the build/execution of this application.

## 8/22/2019

https://www.javaworld.com/article/2073002/jtable-filtering-with-glazed-lists.html
### EventSelectionModel
Java's DefaultListSelectionModel has a flaw that prevents it from cooperating with filtering. When a row is inserted into a JTable, it becomes selected if immediately inserted before a selected row. This proves problematic if the rows of a JTable are selected when its filter changes. When the filter is removed, some restored rows may incorrectly become selected.

Glazed Lists includes EventSelectionModel, which has more predictable behavior. This class also has a method, getEventList(), which contains the JTable's current selection. As rows are selected, the corresponding elements are automatically added to that list.

### DataBrowser
```
        case 4: // case 3 but refactored to use glazed lists column sorting
        {
        	GlazedListTableModel tm = new GlazedListTableModel(file, this.dataBrowserAware.getDelimiter());
        	dataHintAware = tm;

        	final SortedList sorted = new SortedList(tm.getEventList(), new DataComparator());
        	
        	final FilterList filtered = new FilterList(sorted, new TextComponentMatcherEditor(txtFilter, new DataTextFilterator(dataHintAware)));
        	
        	AdvancedTableModel etm = GlazedListsSwing.eventTableModelWithThreadProxyList(filtered, tm);
        	csvTableModel = etm;
            tblCsvData.setModel(csvTableModel); // for the next line to work, you must set the model first

        	TableComparatorChooser<Object> tcc = TableComparatorChooser.install(tblCsvData, sorted, TableComparatorChooser.SINGLE_COLUMN);

        }
        	break;
        default:
        	;
        }
        
        switch(modeldesign) {
        case 4:
        	// already setModel() in case 4 above
        	break;
        default:
        	tblCsvData.setModel(csvTableModel);
        }
```

### org.jwellman.csvviewer.glazed.DataComparator
```
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
```

## 8/21/2019
Filtered JTable
* new GlazedListTableModel (org.jwellman.csvviewer)
* new DataTextFilterator (org.jwellman.csvviewer.glazed)
* mod DataBrowser

### DataBrowser
```
	private TableModel csvTableModel;
	
	// This was not originally present; however, with the addition of glazed lists
	// it became necessary to differentiate between DelimitedFileTableModel and GlazedListTableModel
	private DataHintAware dataHintAware;
...
    private JTextField txtFilter;
...
        int modeldesign = 3;
        switch(modeldesign) {
        case 1:
        	csvTableModel = new DelimitedFileTableModel(file, this.dataBrowserAware.getDelimiter());
        	dataHintAware = (DataHintAware) csvTableModel;

        	break;
        case 2:
        {
        	GlazedListTableModel tm = new GlazedListTableModel(file, this.dataBrowserAware.getDelimiter());
        	dataHintAware = tm;
        	DefaultEventTableModel etm = new DefaultEventTableModel(tm.getEventList(), tm);
        	csvTableModel = etm;
        }
        	break;
        case 3: // case 2 + filter list
        {
        	GlazedListTableModel tm = new GlazedListTableModel(file, this.dataBrowserAware.getDelimiter());
        	dataHintAware = tm;
        	FilterList filteredData = new FilterList(tm.getEventList(), new TextComponentMatcherEditor(txtFilter, new DataTextFilterator(dataHintAware)));
        	DefaultEventTableModel etm = new DefaultEventTableModel(filteredData, tm);
        	csvTableModel = etm;
        }
        	break;
        default:
        	;
        }
        tblCsvData.setModel(csvTableModel);
		    tblCsvData.setShowVerticalLines(false);
...    
    	    north.add(txtFilter = new JTextField("enter-filter-here"));

```

### org.jwellman.csvviewer.GlazedListTableModel
```
package org.jwellman.csvviewer;

import java.io.File;
import java.io.IOException;
import java.io.Reader;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.lang3.math.NumberUtils;
import org.jwellman.swing.misc.DataHint;
import org.jwellman.swing.misc.DataHintAware;

import com.opencsv.CSVParser;
import com.opencsv.CSVParserBuilder;
import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;

import ca.odell.glazedlists.BasicEventList;
import ca.odell.glazedlists.EventList;
import ca.odell.glazedlists.gui.TableFormat;

/**
 * 
 * @author rwellman
 *
 */
public class GlazedListTableModel implements DataHintAware, TableFormat<Object> {
	
	private static final long serialVersionUID = 1L;

	private List<String> columns = new ArrayList<>();

	private List<DataHint> dataHints = new ArrayList<>();

	private List<String[]> records;
	
	private EventList<Object> glazedrecords = new BasicEventList<>();
	
	public GlazedListTableModel(File file, String delimiter) {

		Reader reader = null;
		CSVReader csvReader = null;

		try {

			reader = Files.newBufferedReader(file.toPath(), Charset.defaultCharset());

			final CSVParser parser = new CSVParserBuilder()
			.withSeparator((delimiter.equals("\\t")) ? '\t' : delimiter.charAt(0))
			.withIgnoreQuotations(false)
			.build();

			csvReader = new CSVReaderBuilder(reader)
			.withSkipLines(0)
			.withCSVParser(parser)
			.build();

			records = csvReader.readAll();

			// TODO curious... how does this affect performance for large datasets?
			boolean fix = true;
			if (fix) {
				for (String[] record : records) {
					for (int i=0; i < record.length; i++) {
						String field = record[i];
						record[i] = field.trim();
					}
				}
			}
			
			// Assume first record is column headings; get them, remove them from dataset, specify NUMERIC data hint
			List<String> columnHeadings = new ArrayList<>(records.get(0).length);
			columnHeadings.add("<#>"); // TODO make this an application property
			columnHeadings.addAll(Arrays.asList(records.get(0)));			
			columns.addAll(columnHeadings);
			records.remove(0); // remove the column headings record
			dataHints.add(DataHint.NUMERIC); // though numeric, treat line numbers as strings

			// Determine data hint for remaining columns;
			final String[] record = records.get(0);
			for (String field : record) {
				if (NumberUtils.isCreatable(field)) {
					dataHints.add(DataHint.NUMERIC);
				} else {
					dataHints.add(DataHint.STRING);
				}
			}

			// GlazedLists
			glazedrecords.addAll(records);
			
		} catch (IOException e) {
			e.printStackTrace();
		} finally {

			try {
				if (csvReader != null)
					csvReader.close();

				if (reader != null)
					reader.close();
			} catch (IOException e) {
				e.printStackTrace();
			}

		}

	}

	@Override
	public List<DataHint> getDataHints() {
		return dataHints;
	}

	@Override
	public int getColumnCount() {
		return records.get(0).length + 1;
	}

	@Override
	public String getColumnName(int col) {
		return (columns != null) ? columns.get(col) : "TBD";
	}

	@Override
	public Object getColumnValue(Object baseObject, int column) {
		if (column == 0)
			return 1; // TODO how to map to row index now that I'm using glazed lists?
			// idea; probably have to modify each underlying String[] object to have the record number
			// not really what I *want* to do, but it might now be my only option
		else {
			String[] asarray = (String[])baseObject;
			return asarray[column - 1]; 			
		}
	}

	public EventList<Object> getEventList() {
		return glazedrecords;
	}
	
}
```

### org.jwellman.csvviewer.glazed.DataTextFilterator
```
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

```
