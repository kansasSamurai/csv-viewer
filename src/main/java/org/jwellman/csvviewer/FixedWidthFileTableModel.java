package org.jwellman.csvviewer;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

import javax.swing.table.AbstractTableModel;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.jwellman.swing.misc.DataHint;
import org.jwellman.swing.misc.DataHintAware;

/**
 * 
 * @author rwellman
 *
 */
public class FixedWidthFileTableModel extends AbstractTableModel implements DataHintAware {

	private static final long serialVersionUID = 1L;

	private List<String> columns = new ArrayList<>();

	private List<String[]> records = new ArrayList<>();

	private List<DataHint> dataHints = new ArrayList<>();

	public FixedWidthFileTableModel(File file, int[] widths) {

		BufferedReader reader = null;

		try {

			reader = Files.newBufferedReader(file.toPath(), Charset.defaultCharset());
			
			String thisLine; int lineno = 1;
			while ((thisLine = reader.readLine()) != null) {
				final String[] record = new String[widths.length+1]; 
	            records.add(record);

	            record[0] = "" + lineno++;
				
				int i = 1; int pos = 0;
	            for (int field : widths) {
	            	record[i++] = StringUtils.trim( StringUtils.substring(thisLine, pos, pos+field-1) );
	            	pos += field;
	            }
	        }
			
			// assume first record is column headings
			columns.add("Line#");
			for (int i=0; i<widths.length; i++) { columns.add("TBD"); }
			
			for (String field : records.get(0)) {
				if (NumberUtils.isCreatable(field)) {
					dataHints.add(DataHint.NUMERIC);
				} else {
					dataHints.add(DataHint.STRING);
				}
			}

		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (reader != null) reader.close();
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
		return records.get(0).length;
	}

	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {
		if (columnIndex == 0)
			return rowIndex + 1;
		else
			return ((String[]) records.get(rowIndex))[columnIndex - 1];
	}

	@Override
	public String getColumnName(int col) {
		return (columns != null) ? columns.get(col) : "TBD";
	}

	@Override
	public List<DataHint> getDataHints() {
		return dataHints;
	}
	
}
