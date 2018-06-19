package org.jwellman.csvviewer;

import java.io.File;
import java.io.IOException;
import java.io.Reader;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javax.swing.table.AbstractTableModel;
import com.opencsv.CSVParser;
import com.opencsv.CSVParserBuilder;
import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;

public class DelimitedFileTableModel extends AbstractTableModel {

	private static final long serialVersionUID = 1L;

	private List<String> columns = new ArrayList<>();

	private List<String[]> records;

	public DelimitedFileTableModel(File file, String delimiter) {

		Reader reader = null;

		CSVReader csvReader = null;

		try {

			reader = Files.newBufferedReader(file.toPath(), Charset.defaultCharset());

			final CSVParser parser = new CSVParserBuilder()

			.withSeparator(delimiter.charAt(0))

			.withIgnoreQuotations(false)

			.build();

			csvReader = new CSVReaderBuilder(reader)

			.withSkipLines(0)

			.withCSVParser(parser)

			.build();

			records = csvReader.readAll();

			// assume first record is column headings

			List<String> columnHeadings = new ArrayList<>(records.get(0).length);

			columnHeadings.add("Line #");

			columnHeadings.addAll(Arrays.asList(records.get(0)));

			columns.addAll(columnHeadings);

			records.remove(0);

		} catch (IOException e) {

			e.printStackTrace();

		} finally {

			try {

				if (csvReader != null)
					csvReader.close();

				if (csvReader != null)
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
	public int getColumnCount() {
		return records.get(0).length + 1;
	}

	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {

		if (columnIndex == 0)
			return rowIndex + 1;

		return ((String[]) records.get(rowIndex))[columnIndex - 1];

	}

	@Override
	public String getColumnName(int col) {
		return (columns != null) ? columns.get(col) : "TBD";
	}

}