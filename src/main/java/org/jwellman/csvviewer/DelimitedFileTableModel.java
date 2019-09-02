package org.jwellman.csvviewer;

import java.io.File;
import java.io.IOException;
import java.io.Reader;
import java.math.BigDecimal;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javax.swing.table.AbstractTableModel;

import org.apache.commons.lang3.math.NumberUtils;
import org.jwellman.swing.misc.DataHint;
import org.jwellman.swing.misc.DataHintAware;

import com.opencsv.CSVParser;
import com.opencsv.CSVParserBuilder;
import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;

/**
 * TODO Option to fix/trim leading/trailing whitespace
 * TODO Option to treat first line as headings
 * TODO Organize data "anomalies" by column; i.e. numerics that do not parse, etc.
 * 
 * @author rwellman
 *
 */
public class DelimitedFileTableModel extends AbstractTableModel implements DataHintAware {

	private static final long serialVersionUID = 1L;

	private List<String> columns = new ArrayList<>();

	private List<DataHint> dataHints = new ArrayList<>();

	private List<String[]> records;

	public DelimitedFileTableModel(File file, String delimiter) {

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

			boolean trim = true;
			if (trim) {
				for (String[] record : records) {
					for (int i=0; i < record.length; i++) {
						record[i] = record[i].trim();
					}
				}
			}
			
			// assume first record is column headings
			List<String> columnHeadings = new ArrayList<>(records.get(0).length);
			columnHeadings.add("<#>");
			columnHeadings.addAll(Arrays.asList(records.get(0)));			
			columns.addAll(columnHeadings);
			records.remove(0); // remove the column headings record
			dataHints.add(DataHint.NUMERIC); // though numeric, treat line numbers as strings
			
			// Determine data hint for remaining columns;
			final String[] record = records.get(0);
			for (String field : record) {				
				System.out.print(field);
				if (NumberUtils.isCreatable(field)) {
					dataHints.add(DataHint.NUMERIC);
					System.out.println(" appears to be NUMERIC");
				} else {
					dataHints.add(DataHint.STRING);
					System.out.println(" appears to be ALPHABETIC");
				}
			}

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
	public int getRowCount() {
		return records.size();
	}

	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {
		if (columnIndex == 0)
			return rowIndex + 1;

		final String rawvalue = ((String[]) records.get(rowIndex))[columnIndex - 1];
		if (this.getDataHints().get(columnIndex).equals(DataHint.NUMERIC)) {
			try {
				return new BigDecimal(rawvalue); // Float.parseFloat(rawvalue);
			} catch(Exception e) {
				return BigDecimal.ZERO;
			}				
		} else return rawvalue;
		
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
    public Class<?> getColumnClass(int index) {		
		System.out.print("" + index);
		final DataHint hint = this.getDataHints().get(index);
		if (hint.equals(DataHint.NUMERIC)) {
			System.out.println(" : gcc NUMERIC");
			return Float.class;			
		} else {
			System.out.println(" : gcc ALPHABETIC");
			return String.class;			
		}
    }
	
	@Override
	public List<DataHint> getDataHints() {
		return dataHints;
	}

}
