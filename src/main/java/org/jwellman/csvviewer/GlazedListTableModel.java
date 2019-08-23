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
