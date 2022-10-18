package org.jwellman.csvviewer.glazed;

import java.io.File;
import java.io.IOException;
import java.io.Reader;
import java.math.BigDecimal;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

import org.apache.commons.lang3.math.NumberUtils;
import org.apache.commons.lang3.StringUtils;
import org.jwellman.swing.misc.DataHint;
import org.jwellman.swing.misc.DataHintAware;

import com.opencsv.CSVParser;
import com.opencsv.CSVParserBuilder;
import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;

import ca.odell.glazedlists.BasicEventList;
import ca.odell.glazedlists.EventList;
import ca.odell.glazedlists.GlazedLists;
import ca.odell.glazedlists.gui.AdvancedTableFormat;
// import ca.odell.glazedlists.gui.TableFormat;

/**
 * 
 * @author rwellman
 *
 */
public class GlazedListTableModel implements DataHintAware, AdvancedTableFormat<Object> {

    private List<String> columns = new ArrayList<>();

    private List<DataHint> dataHints = new ArrayList<>();

    private List<String[]> records;

    private EventList<Object> glazedrecords = new BasicEventList<>();

    private final Comparator<String> paddedComparator = new BigDecimalComparator();

    @SuppressWarnings("rawtypes")
    private final Comparator<Comparable> defaultComparator = GlazedLists.comparableComparator();

    /**
     * The only constructor for this class.  All parameters are required.
     * 
     * @param file
     * @param delimiter
     */
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

			List<String[]> raw = csvReader.readAll();
			records = new ArrayList<>();

			// This is to "fix" the need for line number to be part of the record
			// TODO curious... how does this affect performance for large datasets?
			boolean fix = true;
			if (fix) {
				int recindex = 1;
				for (String[] record : raw) {
					// create a new record with the new length
					final int newlength = record.length + 1;
					final String[] fixed = new String[newlength];
					// populate the first field with the line number
					fixed[0] = Integer.toString(recindex++);
					// populate the rest of the fields with the original data
					for (int i = 1; i < newlength; i++) {
						fixed[i] = record[i - 1].trim();
					}
					records.add(fixed);
				}
			}

			// Assume first record is column headings; get them, remove them from dataset.
			// TODO create application option for column headings; i.e. if the file does
			// not have column headings, then use defaults (like 'A', 'B', ...)
			List<String> columnHeadings = new ArrayList<>();
			columnHeadings.add("<LINE>"); // TODO make this an application property
			columnHeadings.addAll(Arrays.asList(raw.get(0)));			
			columns.addAll(columnHeadings);
			records.remove(0); // remove the column headings record

			// Determine data hint for remaining columns;
			// TODO this current implementation has the drawback that it only looks at the first record which may have blank fields
			// and therefore it may categorize those fields incorrectly.
			final String[] record = records.get(0);
			for (String field : record) {
				System.out.print(field);
				if (StringUtils.isNumeric(field)) {
					// Since decimal points and field separators are non-numeric, this condition only handles integer data
					if (field.startsWith("0")) {
						dataHints.add(DataHint.ZEROPADDED_INTEGER);
						System.out.println(" appears to be ZEROPADDED_INTEGER");
					} else {
						dataHints.add(DataHint.NUMERIC);
						System.out.println(" appears to be NUMERIC");
					}
				} else if (NumberUtils.isCreatable(field)) {
					// This condition handles data that contains other non-numeric characters such as decimal points and field separators.
					// Therefore, we don't really know if it is floating point or integers with field separators.
					// Finally, I should note, that I don't think I really have a plan for field separators... in practice,
					// this condition is so far meant for floating point values with decimal point only.
					dataHints.add(DataHint.NUMERIC);
					System.out.println(" appears to be NUMERIC");
				} else {
					dataHints.add(DataHint.STRING);
					System.out.println(" appears to be ALPHABETIC");
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
	
	// getRowCount() is handled by GlazedList base class

	// getValueAt() is handled by GlazedList base class; see getColumnValue() below

	@Override
	public int getColumnCount() {
		return records.get(0).length;
	}

	@Override
	public String getColumnName(int col) {
		return (columns != null) ? columns.get(col) : "TBD";
	}

	@Override
	@SuppressWarnings("rawtypes")
	public Class getColumnClass(int column) {
		return Object.class;
	}

	@Override
	@SuppressWarnings("rawtypes")
	public Comparator getColumnComparator(int column) {
		switch (this.getDataHints().get(column)) {
			case ZEROPADDED_INTEGER:
				return paddedComparator;
			default:
				return defaultComparator;
		}
	}

	@Override
	public Object getColumnValue(Object baseObject, int column) {
		if (column == -1) // I have disabled this since I fixed line numbers
			return 1; // TODO how to map to row index now that I'm using glazed lists?
			// idea; probably have to modify each underlying String[] object to have the record number
			// not really what I *want* to do, but it might now be my only option
		else {
			final String[] valuearray = (String[])baseObject;
			final String rawvalue = valuearray[column]; // column - 1

			// Since we have to compare to at least one datahint, we choose
			// string to very slightly optimize this since it is "anecdotally"
			// the most common type -- obviously certain files may be different.
			switch (this.getDataHints().get(column)) {
				case STRING:
					return rawvalue;
				case NUMERIC:
					if (StringUtils.isEmpty(rawvalue)) {
						return BigDecimal.ZERO;
					} else {
						try {
							return new BigDecimal(rawvalue); // Float.parseFloat(rawvalue);
						} catch (Exception e) {
							System.out.println( "Error converting > '" + rawvalue + "' < to a numeric/BigDecimal");
							return BigDecimal.ZERO;
						}
					}
				default:
					return rawvalue;
				
			}
		}
	}

	@Override
	public List<DataHint> getDataHints() {
		return dataHints;
	}

	public EventList<Object> getEventList() {
		return glazedrecords;
	}

}
